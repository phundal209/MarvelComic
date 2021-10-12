package com.app.marvel.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.marvel.data.Comic
import com.app.marvel.data.ComicRepository
import com.app.marvel.data.ComicResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val comicRepository: ComicRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _comicData: MutableStateFlow<ComicViewState> = MutableStateFlow(ComicViewState.Initial)
    val comicData: Flow<ComicViewState>
        get() = _comicData

    fun getComicData() {
        viewModelScope.launch(dispatcher) {
            when(val comicData = comicRepository.getComicData()) {
                is ComicResult.Error -> {
                    _comicData.emit(ComicViewState.ErrorLoading(comicData.error))
                }
                is ComicResult.Success -> {
                    _comicData.emit(ComicViewState.LoadedData(comicData.data))
                }
            }
        }
    }
}

sealed class ComicViewState {
    object Initial : ComicViewState()
    data class LoadedData(val data: Comic): ComicViewState()
    data class ErrorLoading(val error: Throwable): ComicViewState()
}