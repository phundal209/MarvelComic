package com.app.marvel.data

sealed class ComicResult {
    data class Success(val data: Comic): ComicResult()
    data class Error(val error: Throwable): ComicResult()
}