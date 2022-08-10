package com.innovaocean.stockmarketstudy.data.repository

import com.innovaocean.stockmarketstudy.domain.model.CompanyListing
import com.innovaocean.stockmarketstudy.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean, query: String
    ): Flow<Resource<List<CompanyListing>>>

}