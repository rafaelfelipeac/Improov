package com.rafaelfelipeac.improov.features.settings.data.repository

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.settings.data.LanguageDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LanguageDataSourceTest {

    @Mock
    internal lateinit var preferences: Preferences

    private lateinit var languageDataSource: LanguageDataSource

    @Before
    fun setup() {
        languageDataSource = LanguageDataSource(preferences)
    }

    @Test
    fun `GIVEN a certain language WHEN getLanguage is called THEN return the same language`() {
        runBlocking {
            // given
            val language = "pt_br"

            given(preferences.language)
                .willReturn(language)

            // when
            val result = languageDataSource.getLanguage()

            // then
            result equalTo language
        }
    }

    @Test
    fun `GIVEN a saved new language WHEN getLanguage is called THEN the new language must be returned`() {
        runBlocking {
            // given
            val language = "en"

            doNothing().`when`(preferences).language = language
            given(preferences.language)
                .willReturn(language)

            // when
            val resultOfSave = languageDataSource.save(language)
            val returnOfGet = languageDataSource.getLanguage()

            // then
            resultOfSave equalTo Unit
            returnOfGet equalTo language
        }
    }
}
