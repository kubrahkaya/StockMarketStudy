package com.innovaocean.stockmarketstudy.data.repository

import com.innovaocean.stockmarketstudy.data.csv.CSVParser
import com.innovaocean.stockmarketstudy.data.local.CompanyListingEntity
import com.innovaocean.stockmarketstudy.data.local.StockDao
import com.innovaocean.stockmarketstudy.data.mapper.toCompanyListing
import com.innovaocean.stockmarketstudy.data.mapper.toCompanyListingEntity
import com.innovaocean.stockmarketstudy.data.remote.StockApi
import com.innovaocean.stockmarketstudy.domain.model.CompanyListing
import com.innovaocean.stockmarketstudy.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val dao: StockDao,
    private val companyListingsParser: CSVParser<CompanyListing>,
) : StockRepository {

    override fun getCompanyListings(
        fetchFromRemote: Boolean, query: String
    ): Flow<Result<List<CompanyListing>>> = flow {
        try {
            val response = api.getListings()
            val companyData = companyListingsParser.parse(response.byteStream()).take(10)
            synchronize(companyData)
            emit(Result.Success(companyData))
        } catch (e: Exception) {
            dao.searchCompanyListing(query).map {
                Result.Success(
                    it.map(
                        CompanyListingEntity::toCompanyListing
                    )
                )
            }
        }
    }

    private suspend fun synchronize(data: List<CompanyListing>) {
        withContext(Dispatchers.IO) {
            dao.clearCompanyListings()
            val values = data.map { it.toCompanyListingEntity() }
            dao.insertCompanyListings(values)
        }
    }
}
