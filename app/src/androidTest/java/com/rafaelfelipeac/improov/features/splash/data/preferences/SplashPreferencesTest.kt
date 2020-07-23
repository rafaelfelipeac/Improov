package com.rafaelfelipeac.improov.features.splash.data.preferences

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.rafaelfelipeac.improov.base.BaseInstTest
import com.rafaelfelipeac.improov.base.DataProviderAndroidTest.shouldBeEqualTo
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SplashPreferencesTest : BaseInstTest() {

    var preferences = Preferences(context)

    @Test
    fun givenANewWelcomeWhenGetIsCalledThenTheSameWelcomeIsReturned() {
        // given
        val welcome = false

        preferences.welcome = welcome

        // when
        val result = preferences.welcome

        // then
        result shouldBeEqualTo welcome
    }
}
