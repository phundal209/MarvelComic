package com.app.marvel

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*

fun onComicFragmentView(actions: ComicFragmentViewInteraction.() -> Unit) {
    ComicFragmentViewInteraction.apply { actions() }
}

object ComicFragmentViewInteraction {
    fun assertErrorToastShown(error: String) {
        onView(withId(R.id.title)).check(matches(withText(error)));
    }

    fun assertImageShown() {
        onView(withId(R.id.expandedImage)).check(matches(isDisplayed()))
    }

    fun assertTitleShown() {
        onView(withId(R.id.title)).check(matches(isDisplayed()))
    }

    fun assertDescriptionShown() {
        onView(withId(R.id.description)).check(matches(isDisplayed()))
    }
}