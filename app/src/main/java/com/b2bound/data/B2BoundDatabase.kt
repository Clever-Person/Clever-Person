package com.b2bound.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WordEntity::class], version = 1, exportSchema = false)
abstract class B2BoundDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}
