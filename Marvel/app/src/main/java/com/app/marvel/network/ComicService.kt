package com.app.marvel.network

import com.app.marvel.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ComicService {
    @GET("/v1/public/comics/28764")
    suspend fun getComicInformation(@Query("apikey") key: String = BuildConfig.PUBLIC_KEY,
                                    @Query("ts") timestamp: String = "1633993835762",
                                    @Query("hash") hash: String): ComicNetworkWrapper

    @GET("/v1/public/comics/28764")
    fun getTestComicInformation(@Query("apikey") key: String = BuildConfig.PUBLIC_KEY,
                            @Query("ts") timestamp: String = "1633993835762",
                            @Query("hash") hash: String): ComicNetworkWrapper
}
//1633993835762

@Module
@InstallIn(SingletonComponent::class)
internal class ComicServiceModule {
    @Provides
    fun providesComicService(serviceFactory: ServiceFactory): ComicService =
        serviceFactory.create(ComicService::class.java)
}