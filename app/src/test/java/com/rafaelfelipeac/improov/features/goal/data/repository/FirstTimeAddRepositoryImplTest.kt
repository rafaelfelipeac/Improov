package com.rafaelfelipeac.improov.features.goal.data.repository

import com.rafaelfelipeac.improov.base.equalTo
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
class FirstTimeAddRepositoryImplTest {

    @Mock
    internal lateinit var preferences: Preferences

    private lateinit var firstTimeAddRepositoryImp: FirstTimeAddRepositoryImpl

    @Before
    fun setup() {
        firstTimeAddRepositoryImp = FirstTimeAddRepositoryImpl(preferences)
    }

    @Test
    fun `GIVEN a boolean value WHEN getFirstTimeAdd is called THEN return the same boolean value`() {
        runBlocking {
            // given
            val booleanValue = false

            given(preferences.firstTimeAdd)
                .willReturn(booleanValue)

            // when
            val result = firstTimeAddRepositoryImp.getFirstTimeAdd()

            // then
            result equalTo booleanValue
        }
    }

    @Test
    fun `GIVEN a saved boolean value WHEN getFirstTimeAdd is called THEN the same boolean value must be returned`() {
        runBlocking {
            // given
            val booleanValue = true

            doNothing().`when`(preferences).firstTimeAdd = booleanValue
            given(preferences.firstTimeAdd)
                .willReturn(booleanValue)

            // when
            val resultOfSave = firstTimeAddRepositoryImp.save(booleanValue)
            val returnOfGet = firstTimeAddRepositoryImp.getFirstTimeAdd()

            // then
            resultOfSave equalTo Unit
            returnOfGet equalTo booleanValue
        }
    }
}
