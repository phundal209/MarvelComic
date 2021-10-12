package com.app.marvel.ui

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.app.marvel.databinding.FragmentComicBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import javax.inject.Inject

internal interface ComicView {
    fun render(context: Context, state: ComicViewState, binding: FragmentComicBinding)
}

internal class ComicViewImpl @Inject constructor() : ComicView {
    override fun render(context: Context, state: ComicViewState, binding: FragmentComicBinding) {
        when(state) {
            ComicViewState.Initial -> {
                binding.loader.visibility = View.VISIBLE
            }
            is ComicViewState.ErrorLoading -> {
                binding.loader.visibility = View.GONE
                Log.e("ComicView", "error while loading", state.error)
                Toast.makeText(binding.root.context, "Error getting comic data", Toast.LENGTH_LONG).show()
            }
            is ComicViewState.LoadedData -> {
                binding.loader.visibility = View.GONE
                binding.title.text = state.data.title
                binding.description.text = state.data.description
                Log.d("ComicView", "url to load = " + state.data.thumbnail)
                Glide.with(context)
                    .load(state.data.thumbnail)
                    .apply(RequestOptions().override(620, 350))
                    .into(binding.expandedImage)
            }
        }
    }
}

@Module
@InstallIn(FragmentComponent::class)
internal abstract class ComicViewModule {
    @Binds
    internal abstract fun bindComicView(impl: ComicViewImpl): ComicView
}