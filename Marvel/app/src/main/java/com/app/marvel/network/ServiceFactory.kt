package com.app.marvel.network

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

internal interface ServiceFactory {
    fun <T> create(clazz: Class<T>): T
}

internal class ServiceFactoryImpl @Inject constructor(
    private val moshi: Moshi
): ServiceFactory {

    override fun <T> create(clazz: Class<T>): T {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .client(createOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi))

        return retrofit.build().create(clazz) as T
    }

    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal class ServiceFactoryModule {
    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    fun providesServiceFactory(moshi: Moshi): ServiceFactory = ServiceFactoryImpl(moshi)
}

