package com.rafaelfelipeac.improov.features.settings.data.repository

import com.rafaelfelipeac.improov.base.DataProviderTest.shouldBeEqualTo
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LanguageRepositoryImplTest {

    @Mock
    internal lateinit var preferences: Preferences

    private lateinit var languageRepositoryImpl: LanguageRepositoryImpl

    @Before
    fun setup() {
        languageRepositoryImpl = LanguageRepositoryImpl(preferences)
    }

    @Test
    fun `GIVEN a certain language WHEN GetLanguage is called THEN return the same language`() {
        runBlocking {
            // given
            val language = "pt_br"

            given(preferences.language)
                .willReturn(language)

            // when
            val result = languageRepositoryImpl.getLanguage()

            // then
            result shouldBeEqualTo language
        }
    }

    @Test
    fun `GIVEN a saved new language WHEN GetLanguage is called THEN the new language must be returned`() {
        runBlocking {
            // given
            val language = "en"

            doNothing().`when`(preferences).language = language
            given(preferences.language)
                .willReturn(language)

            // when
            val resultOfSave = languageRepositoryImpl.save(language)
            val returnOfGet = languageRepositoryImpl.getLanguage()

            // then
            resultOfSave shouldBeEqualTo Unit
            returnOfGet shouldBeEqualTo language
        }
    }
}
