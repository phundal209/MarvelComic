package com.app.marvel

import android.view.LayoutInflater
import com.app.marvel.data.Comic
import com.app.marvel.databinding.FragmentComicBinding
import com.app.marvel.ui.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
internal class ComicFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val comicView: ComicView = ComicViewImpl()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_AllViewsShown_WhenComicViewState_Loaded() {
        launchFragmentInHiltContainer<ComicFragment> {

//            val binding: FragmentComicBinding = FragmentComicBinding.inflate(LayoutInflater.from(requireContext()))
//            comicView.render(requireContext(), ComicViewState.LoadedData(object : Comic {
//                override val id: Int
//                    get() = 1
//                override val issueNumber: Double
//                    get() = 1.0
//                override val title: String
//                    get() = "Mock Title"
//                override val description: String
//                    get() = "Mock Description"
//                override val thumbnail: String
//                    get() = "https://i.annihil.us/u/prod/marvel/i/mg/e/d0/5bc78ba0cc41e"
//
//            }), binding)
        }

//        onComicFragmentView {
//            assertImageShown()
//            assertTitleShown()
//            assertDescriptionShown()
//        }
    }
}