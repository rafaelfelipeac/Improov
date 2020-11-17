package com.rafaelfelipeac.improov.features.profile.data.repository

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.profile.data.NameDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NameDataSourceTest {

    @Mock
    internal lateinit var preferences: Preferences

    private lateinit var nameDataSource: NameDataSource

    @Before
    fun setup() {
        nameDataSource = NameDataSource(preferences)
    }

    @Test
    fun `GIVEN a certain name WHEN getName is called THEN return the same name`() {
        runBlocking {
            // given
            val name = "User Name"

            given(preferences.name)
                .willReturn(name)

            // when
            val result = nameDataSource.getName()

            // then
            result equalTo name
        }
    }

    @Test
    fun `GIVEN a saved new name WHEN getName is called THEN the new name must be returned`() {
        runBlocking {
            // given
            val name = "New User Name"

            doNothing().`when`(preferences).name = name
            given(preferences.name)
                .willReturn(name)

            // when
            val resultOfSave = nameDataSource.save(name)
            val returnOfGet = nameDataSource.getName()

            // then
            resultOfSave equalTo Unit
            returnOfGet equalTo name
        }
    }
}
