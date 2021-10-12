package com.app.marvel.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
internal interface ComicDao {
    @Query("SELECT * FROM comic WHERE id IS :comicId")
    fun getSavedComic(comicId: Int): ComicEntity?

    @Insert
    fun insertComic(vararg comic: ComicEntity)
}