package com.app.marvel.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.marvel.data.Comic

@Entity(tableName = "comic")
internal data class ComicEntity(
    @PrimaryKey override val id: Int,
    @ColumnInfo(name = "issueNumber") override val issueNumber: Double,
    @ColumnInfo(name = "title") override val title: String,
    @ColumnInfo(name = "description") override val description: String,
    @ColumnInfo(name = "thumbnail") override val thumbnail: String
) : Comic