package com.app.marvel.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [ComicEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun comicDao(): ComicDao
}

@Module
@InstallIn(SingletonComponent::class)
internal class RoomModule {
    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "comic_database").build()

    @Provides
    fun providesComicDao(appDatabase: AppDatabase): ComicDao =
        appDatabase.comicDao()
}