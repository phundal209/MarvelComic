package com.app.marvel.data

import com.app.marvel.cache.ComicDao
import com.app.marvel.cache.ComicEntity
import com.app.marvel.network.ComicService
import com.app.marvel.utils.HashGenerator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal interface ComicRepository {
    suspend fun getComicData(): ComicResult
}

internal class ComicRepositoryImpl @Inject constructor(
  private val comicService: ComicService,
  private val hashGenerator: HashGenerator,
  private val cache: ComicDao,
  private val coroutineDispatcher: CoroutineDispatcher
) : ComicRepository {
    override suspend fun getComicData(): ComicResult {
        return withContext(coroutineDispatcher) {
            val cachedComic = cache.getSavedComic(28764)
            if (cachedComic != null) {
                return@withContext ComicResult.Success(cachedComic)
            }
            //cad7327ff1f17db9cc5e638c126667a5
            val result = comicService.getComicInformation(hash = hashGenerator.generate())
            if (result.code == 200) {
                val networkModel = result.data.results[0]
                val comicModel = networkModel.toComic()
                cache.insertComic(ComicEntity(
                        id = comicModel.id,
                        issueNumber = comicModel.issueNumber,
                        title = comicModel.title,
                        description = comicModel.description,
                        thumbnail = comicModel.thumbnail
                ))
                return@withContext ComicResult.Success(comicModel)
            } else {
                return@withContext ComicResult.Error(IllegalAccessException("Unable to retrieve comic information"))
            }
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ComicRepositoryModule {
    @Binds
    abstract fun bindsComicRepository(impl: ComicRepositoryImpl): ComicRepository
}