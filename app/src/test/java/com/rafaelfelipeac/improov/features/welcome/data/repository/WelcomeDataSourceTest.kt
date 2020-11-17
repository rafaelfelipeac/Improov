package com.rafaelfelipeac.improov.features.welcome.data.repository

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.welcome.data.WelcomeDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WelcomeDataSourceTest {

    @Mock
    internal lateinit var preferences: Preferences

    private lateinit var welcomeDataSource: WelcomeDataSource

    @Before
    fun setup() {
        welcomeDataSource = WelcomeDataSource(preferences)
    }

    @Test
    fun `GIVEN a saved new boolean value WHEN save is called THEN a Unit must be returned`() {
        runBlocking {
            // given
            val booleanValue = true

            doNothing().`when`(preferences).welcome = booleanValue

            // when
            val resultOfSave = welcomeDataSource.save(booleanValue)

            // then
            resultOfSave equalTo Unit
        }
    }
}
