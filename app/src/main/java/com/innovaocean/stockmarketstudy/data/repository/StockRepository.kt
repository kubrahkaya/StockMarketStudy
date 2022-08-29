package com.innovaocean.stockmarketstudy.data.repository

import com.innovaocean.stockmarketstudy.domain.model.CompanyListing
import com.innovaocean.stockmarketstudy.util.Result
import kotlinx.coroutines.flow.Flow

interface StockRepository  {

     fun getCompanyListings(
        fetchFromRemote: Boolean, query: String
    ): Flow<Result<List<CompanyListing>>>

}