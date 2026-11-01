package com.b2bound.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WordEntity::class, StudyVideoEntity::class, VideoNoteEntity::class], version = 2, exportSchema = false)
abstract class B2BoundDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun studyDao(): StudyDao
}
