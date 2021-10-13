package com.app.marvel

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.app.marvel.data.Comic
import com.app.marvel.ui.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
internal class ComicViewTest {
    private val comicView: ComicView = ComicViewImpl()

    @Test
    fun test_AllViewsShown_WhenComicViewState_Loaded() {
        val scenario = launchFragmentInContainer<TestComicFragment>(
            initialState = Lifecycle.State.INITIALIZED,
            themeResId = R.style.Theme_AppCompat
        )
        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onFragment {
            comicView.render(it.requireContext(), ComicViewState.LoadedData(
                object : Comic {
                    override val id: Int
                        get() = 1
                    override val issueNumber: Double
                        get() = 1.0
                    override val title: String
                        get() = "Test Title"
                    override val description: String
                        get() = "Test Description"
                    override val thumbnail: String
                        get() = "test.jpg"

                }
            ), it.binding)
        }

        onComicFragmentView {
            assertImageShown()
            assertTitleShown()
            assertDescriptionShown()
        }
    }
}