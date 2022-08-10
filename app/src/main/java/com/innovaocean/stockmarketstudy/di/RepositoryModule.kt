package com.innovaocean.stockmarketstudy.di

import com.innovaocean.stockmarketstudy.data.csv.CSVParser
import com.innovaocean.stockmarketstudy.data.csv.CompanyListingParser
import com.innovaocean.stockmarketstudy.data.repository.StockRepository
import com.innovaocean.stockmarketstudy.data.repository.StockRepositoryImpl
import com.innovaocean.stockmarketstudy.domain.model.CompanyListing
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Module
@InstallIn
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindStockRepository(stockRepositoryImpl: StockRepositoryImpl): StockRepository

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingParser
    ): CSVParser<CompanyListing>
}