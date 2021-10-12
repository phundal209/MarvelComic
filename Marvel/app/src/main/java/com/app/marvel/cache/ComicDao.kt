package com.app.marvel.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
internal interface ComicDao {
    @Query("SELECT * FROM comic WHERE id IS :comicId")
    suspend fun getSavedComic(comicId: Int): ComicEntity?

    @Insert
    suspend fun insertComic(vararg comic: ComicEntity)
}