package com.rafaelfelipeac.improov.features.profile.data.repository

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.profile.data.FirstTimeListDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FirstTimeListDataSourceTest {

    @Mock
    internal lateinit var preferences: Preferences

    private lateinit var firstTimeListDataSource: FirstTimeListDataSource

    @Before
    fun setup() {
        firstTimeListDataSource = FirstTimeListDataSource(preferences)
    }

    @Test
    fun `GIVEN a boolean value WHEN getFirstTimeList is called THEN return the same boolean value`() {
        runBlocking {
            // given
            val booleanValue = false

            given(preferences.firstTimeList)
                .willReturn(booleanValue)

            // when
            val result = firstTimeListDataSource.getFirstTimeList()

            // then
            result equalTo booleanValue
        }
    }

    @Test
    fun `GIVEN a saved new boolean value WHEN getFirstTimeList is called THEN the boolean value must be returned`() {
        runBlocking {
            // given
            val booleanValue = true

            doNothing().`when`(preferences).firstTimeList = booleanValue
            given(preferences.firstTimeList)
                .willReturn(booleanValue)

            // when
            val resultOfSave = firstTimeListDataSource.save(booleanValue)
            val returnOfGet = firstTimeListDataSource.getFirstTimeList()

            // then
            resultOfSave equalTo Unit
            returnOfGet equalTo booleanValue
        }
    }
}
