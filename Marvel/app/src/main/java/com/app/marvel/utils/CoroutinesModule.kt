package com.app.marvel.utils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
internal class CoroutinesModule {
    @Provides
    fun providesDefaultCoroutineDispatcher() = Dispatchers.Default

    @Provides
    fun provvidesDefaultCoroutineScope(dispatcher: CoroutineDispatcher) =
        CoroutineScope(dispatcher)
}