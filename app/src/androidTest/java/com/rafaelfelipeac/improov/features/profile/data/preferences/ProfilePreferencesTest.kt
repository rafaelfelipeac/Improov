package com.rafaelfelipeac.improov.features.profile.data.preferences

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.rafaelfelipeac.improov.base.BaseInstTest
import com.rafaelfelipeac.improov.base.equal
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ProfilePreferencesTest : BaseInstTest() {

    var preferences = Preferences(context)

    @Test
    fun givenANewFirstTimeAddWhenGetIsCalledThenTheSameFirstTimeAddIsReturned() {
        // given
        val firstTimeAdd = true

        preferences.firstTimeAdd = firstTimeAdd

        // when
        val result = preferences.firstTimeAdd

        // then
        result equal firstTimeAdd
    }

    @Test
    fun givenANewFirstListAddWhenGetIsCalledThenTheSameFirstTimeListIsReturned() {
        // given
        val firstTimeList = true

        preferences.firstTimeList = firstTimeList

        // when
        val result = preferences.firstTimeList

        // then
        result equal firstTimeList
    }

    @Test
    fun givenANewNameWhenGetIsCalledThenTheSameNameIsReturned() {
        // given
        val name = "User Name"

        preferences.name = name

        // when
        val result = preferences.name

        // then
        result equal name
    }

    @Test
    fun givenANewWelcomeWhenGetIsCalledThenTheSameWelcomeIsReturned() {
        // given
        val welcome = true

        preferences.welcome = welcome

        // when
        val result = preferences.welcome

        // then
        result equal welcome
    }
}
