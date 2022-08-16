package com.innovaocean.stockmarketstudy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CompanyListingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StockDatabase: RoomDatabase() {
    abstract fun dao(): StockDao
}