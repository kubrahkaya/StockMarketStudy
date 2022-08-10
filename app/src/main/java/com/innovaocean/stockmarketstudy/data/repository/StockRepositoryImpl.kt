package com.innovaocean.stockmarketstudy.data.repository

import com.innovaocean.stockmarketstudy.data.csv.CSVParser
import com.innovaocean.stockmarketstudy.data.local.StockDatabase
import com.innovaocean.stockmarketstudy.data.mapper.toCompanyListing
import com.innovaocean.stockmarketstudy.data.mapper.toCompanyListingEntity
import com.innovaocean.stockmarketstudy.data.remote.StockApi
import com.innovaocean.stockmarketstudy.domain.model.CompanyListing
import com.innovaocean.stockmarketstudy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>,
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean, query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(localListings.map { it.toCompanyListing() }))

            val isDBEmpty = localListings.isEmpty() && query.isBlank()
            val shouldLoadFromCache = !isDBEmpty && !fetchFromRemote
            if (shouldLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListing = try {
                val response = api.getListings()
                companyListingsParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            remoteListing?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(listings.map { it.toCompanyListingEntity() })
                emit(
                    Resource.Success(
                        data = dao.searchCompanyListing("").map { it.toCompanyListing() })
                )
                emit(Resource.Loading(false))
            }
        }
    }
}