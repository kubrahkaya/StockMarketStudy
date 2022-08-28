package com.innovaocean.stockmarketstudy.data.repository

import com.innovaocean.stockmarketstudy.domain.model.CompanyListing
import kotlinx.coroutines.flow.Flow

interface StockRepository  {

     fun getCompanyListings(
        fetchFromRemote: Boolean, query: String
    ): Flow<List<CompanyListing>>

}