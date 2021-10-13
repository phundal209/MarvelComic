package com.app.marvel

import com.app.marvel.cache.ComicDao
import com.app.marvel.cache.ComicEntity
import com.app.marvel.data.ComicRepository
import com.app.marvel.data.ComicRepositoryImpl
import com.app.marvel.data.ComicResult
import com.app.marvel.network.*
import com.app.marvel.utils.HashGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class ComicRepositoryTest {
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private val comicService: ComicService = Mockito.mock(ComicService::class.java)
    private val hashGenerator: HashGenerator = Mockito.mock(HashGenerator::class.java)
    private val cache: ComicDao = Mockito.mock(ComicDao::class.java)
    private val comicRepository: ComicRepository = ComicRepositoryImpl(comicService, hashGenerator, cache, coroutinesTestRule.testDispatcher)

    @Test
    fun `GIVEN no cache and valid hash and valid network WHEN getComicData() called THEN success returned`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            Mockito.`when`(cache.getSavedComic(28764)).thenReturn(null)
            Mockito.`when`(hashGenerator.generate()).thenReturn("mockHash")
            Mockito.`when`(comicService.getTestComicInformation(hash = "mockHash")).thenReturn(
                getMockComicNetworkModel(200)
            )

            val result = comicRepository.getComicDataForTest()

            assert(result is ComicResult.Success)
        }
    }

    @Test
    fun `GIVEN no cache and valid hash and valid network WHEN getComicData() called THEN data saved to cache`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            Mockito.`when`(cache.getSavedComic(28764)).thenReturn(null)
            Mockito.`when`(hashGenerator.generate()).thenReturn("mockHash")
            Mockito.`when`(comicService.getTestComicInformation(hash = "mockHash")).thenReturn(
                getMockComicNetworkModel(200)
            )

            comicRepository.getComicDataForTest()

            Mockito.verify(cache).insertComic(ComicEntity(id=1, issueNumber=1.0, title="Title", description="Description", thumbnail="thumbnail.jpg"))
        }
    }

    @Test
    fun `GIVEN no cache and valid hash invalid network WHEN getComicData() called THEN failure returned`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            Mockito.`when`(cache.getSavedComic(28764)).thenReturn(null)
            Mockito.`when`(hashGenerator.generate()).thenReturn("mockHash")
            Mockito.`when`(comicService.getTestComicInformation(hash = "mockHash")).thenReturn(
                getMockComicNetworkModel(401)
            )

            val result = comicRepository.getComicDataForTest()

            assert(result is ComicResult.Error)
        }
    }

    @Test
    fun `GIVEN no cache and valid hash invalid network WHEN getComicData() called THEN no data cached`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            Mockito.`when`(cache.getSavedComic(28764)).thenReturn(null)
            Mockito.`when`(hashGenerator.generate()).thenReturn("mockHash")
            Mockito.`when`(comicService.getTestComicInformation(hash = "mockHash")).thenReturn(
                getMockComicNetworkModel(401)
            )

            comicRepository.getComicDataForTest()

            Mockito.verify(cache).getSavedComic(28764)
            Mockito.verifyNoMoreInteractions(cache)
        }
    }

    @Test
    fun `GIVEN cache and valid has WHEN getComicData() called THEN cache returned and network not called`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            Mockito.`when`(cache.getSavedComic(28764)).thenReturn(getComicEntity())

            val result = comicRepository.getComicDataForTest()

            assert(result is ComicResult.Success)

            Mockito.verifyZeroInteractions(comicService)
            Mockito.verifyZeroInteractions(hashGenerator)
        }
    }

    private fun getMockComicNetworkModel(requestCode: Int): ComicNetworkWrapper {
        return ComicNetworkWrapper(
            code = requestCode,
            attributionText = "attribution",
            data = ComicDataContainer(
                results = listOf(ComicNetworkResults(
                    _id = 1,
                    _issueNumber = 1.0,
                    _title = "Title",
                    _description = "Description",
                    _thumbnail = ComicThumbnail(
                        path = "thumbnail",
                        ext = "jpg"
                    )
                ))
            )
        )
    }

    private fun getComicEntity(): ComicEntity {
        return ComicEntity(
            id = 1,
            issueNumber = 1.0,
            title = "Title",
            description = "Description",
            thumbnail = "thumbnail.jp"
        )
    }
}

@ExperimentalCoroutinesApi
class CoroutineTestRule(val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}