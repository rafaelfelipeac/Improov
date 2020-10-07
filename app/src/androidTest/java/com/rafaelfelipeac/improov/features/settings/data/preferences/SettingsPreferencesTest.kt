package com.rafaelfelipeac.improov.features.settings.data.preferences

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.rafaelfelipeac.improov.base.BaseInstTest
import com.rafaelfelipeac.improov.base.equal
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SettingsPreferencesTest : BaseInstTest() {

    var preferences = Preferences(context)

    @Test
    fun givenANewLanguageWhenGetIsCalledThenTheSameLanguageIsReturned() {
        // given
        val language = "en"

        preferences.language = language

        // when
        val result = preferences.language

        // then
        result equal language
    }
}
