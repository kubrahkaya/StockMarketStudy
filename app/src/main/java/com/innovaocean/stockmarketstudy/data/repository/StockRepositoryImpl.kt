package com.innovaocean.stockmarketstudy.data.repository

import com.innovaocean.stockmarketstudy.data.local.CompanyListingEntity
import com.innovaocean.stockmarketstudy.data.local.StockDao
import com.innovaocean.stockmarketstudy.data.mapper.toCompanyListing
import com.innovaocean.stockmarketstudy.data.mapper.toCompanyListingEntity
import com.innovaocean.stockmarketstudy.di.IoDispatcher
import com.innovaocean.stockmarketstudy.domain.model.CompanyListing
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val dao: StockDao,
    private val remoteDataSource: RemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : StockRepository {

    override fun getCompanyListings(
        fetchFromRemote: Boolean, query: String
    ): Flow<List<CompanyListing>> =
        dao.searchCompanyListing(query)
            .takeIf { !fetchFromRemote }
            ?.let { localList ->
            localList.map {
                it.map(
                    CompanyListingEntity::toCompanyListing
                )
            }
        } ?: run {
            remoteDataSource.remoteList
                .filterNotNull()
                .onEach {
                    synchronize(it)
                }.flowOn(ioDispatcher)
        }

    private suspend fun synchronize(data: List<CompanyListing>) {
            dao.clearCompanyListings()
            val values = data.map { it.toCompanyListingEntity() }
            dao.insertCompanyListings(values)
    }
}
