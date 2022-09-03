package com.innovaocean.stockmarketstudy.data.repository

import com.innovaocean.stockmarketstudy.data.csv.CSVParser
import com.innovaocean.stockmarketstudy.data.remote.StockApi
import com.innovaocean.stockmarketstudy.domain.model.CompanyListing
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: StockApi,
    private val companyListingsParser: CSVParser<CompanyListing>
) {
    val remoteList = flow {
        val response = api.getListings()
        val companyData = companyListingsParser.parse(response.byteStream()).take(20)
        emit(companyData)
    }
}