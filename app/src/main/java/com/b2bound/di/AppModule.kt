package com.b2bound.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.b2bound.data.B2BoundDatabase
import com.b2bound.data.WordDao
import com.b2bound.data.StudyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS study_videos (videoId TEXT NOT NULL PRIMARY KEY, title TEXT NOT NULL, topic TEXT NOT NULL, studyBand TEXT NOT NULL, addedAtEpochMillis INTEGER NOT NULL)")
            database.execSQL("CREATE TABLE IF NOT EXISTS video_notes (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, videoId TEXT NOT NULL, text TEXT NOT NULL, timestampSeconds INTEGER NOT NULL, createdAtEpochMillis INTEGER NOT NULL)")
            database.execSQL("CREATE INDEX IF NOT EXISTS index_video_notes_videoId ON video_notes (videoId)")
        }
    }

    @Provides @Singleton fun database(@ApplicationContext context: Context): B2BoundDatabase =
        Room.databaseBuilder(context, B2BoundDatabase::class.java, "b2bound.db").addMigrations(MIGRATION_1_2).build()
    @Provides fun wordDao(database: B2BoundDatabase): WordDao = database.wordDao()
    @Provides fun studyDao(database: B2BoundDatabase): StudyDao = database.studyDao()
}
