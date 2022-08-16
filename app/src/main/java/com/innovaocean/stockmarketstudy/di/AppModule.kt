package com.innovaocean.stockmarketstudy.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.innovaocean.stockmarketstudy.data.local.StockDao
import com.innovaocean.stockmarketstudy.data.local.StockDatabase
import com.innovaocean.stockmarketstudy.data.remote.StockApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStockApi(): StockApi {
        return Retrofit.Builder()
            .baseUrl(StockApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(StockApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStockDatabase(@ApplicationContext context: Context): StockDatabase {
        return Room.databaseBuilder(
            context,
            StockDatabase::class.java,
            "stockdb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(db: StockDatabase): StockDao {
        return db.dao()
    }
}