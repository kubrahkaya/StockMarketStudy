package com.innovaocean.stockmarketstudy.data.repository

import com.innovaocean.stockmarketstudy.data.csv.CSVParser
import com.innovaocean.stockmarketstudy.data.local.CompanyListingEntity
import com.innovaocean.stockmarketstudy.data.local.StockDao
import com.innovaocean.stockmarketstudy.data.mapper.toCompanyListing
import com.innovaocean.stockmarketstudy.data.mapper.toCompanyListingEntity
import com.innovaocean.stockmarketstudy.data.remote.StockApi
import com.innovaocean.stockmarketstudy.domain.model.CompanyListing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val dao: StockDao,
    private val companyListingsParser: CSVParser<CompanyListing>,
) : StockRepository {

    override fun getCompanyListings(
        fetchFromRemote: Boolean, query: String
    ): Flow<List<CompanyListing>> {
        return dao.searchCompanyListing(query)
            .map { it.map(CompanyListingEntity::toCompanyListing) }
    }

    private suspend fun synchronize() {
        val companyData = try {
            val response = api.getListings()
            companyListingsParser.parse(response.byteStream())
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        }

        dao.clearCompanyListings()
        dao.insertCompanyListings(companyData!!.map { it.toCompanyListingEntity() })
    }
}
