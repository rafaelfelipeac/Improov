package com.rafaelfelipeac.improov.features.welcome.data.repository

import com.rafaelfelipeac.improov.base.DataProvider.shouldBeEqualTo
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
class WelcomeRepositoryImplTest {

    @Mock
    internal lateinit var preferences: Preferences

    private lateinit var welcomeRepositoryImpl: WelcomeRepositoryImpl

    @Before
    fun setup() {
        welcomeRepositoryImpl = WelcomeRepositoryImpl(preferences)
    }

    @Test
    fun `GIVEN a boolean value WHEN Get is called THEN return the same boolean value`() {
        runBlocking {
            // given
            val booleanValue = false

            given(preferences.welcome)
                .willReturn(booleanValue)

            // when
            val result = welcomeRepositoryImpl.getWelcome()

            // then
            result shouldBeEqualTo booleanValue
        }
    }

    @Test
    fun `GIVEN a saved new boolean value WHEN Get is called THEN the new boolean value must be returned`() {
        runBlocking {
            // given
            val booleanValue = true

            doNothing().`when`(preferences).welcome = booleanValue
            given(preferences.welcome)
                .willReturn(booleanValue)

            // when
            val resultOfSave = welcomeRepositoryImpl.save(booleanValue)
            val returnOfGet = welcomeRepositoryImpl.getWelcome()

            // then
            resultOfSave shouldBeEqualTo Unit
            returnOfGet shouldBeEqualTo booleanValue
        }
    }
}
