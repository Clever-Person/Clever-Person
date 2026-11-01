package com.b2bound.di

import android.content.Context
import androidx.room.Room
import com.b2bound.data.B2BoundDatabase
import com.b2bound.data.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton fun database(@ApplicationContext context: Context): B2BoundDatabase =
        Room.databaseBuilder(context, B2BoundDatabase::class.java, "b2bound.db").build()
    @Provides fun wordDao(database: B2BoundDatabase): WordDao = database.wordDao()
}
