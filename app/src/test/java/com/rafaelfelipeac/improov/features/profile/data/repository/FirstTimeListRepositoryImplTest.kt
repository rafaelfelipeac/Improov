package com.rafaelfelipeac.improov.features.profile.data.repository

import com.rafaelfelipeac.improov.core.extension.equalTo
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.profile.data.respository.FirstTimeListRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FirstTimeListRepositoryImplTest {

    @Mock
    internal lateinit var preferences: Preferences

    private lateinit var firstTimeListRepositoryImp: FirstTimeListRepositoryImpl

    @Before
    fun setup() {
        firstTimeListRepositoryImp = FirstTimeListRepositoryImpl(preferences)
    }

    @Test
    fun `GIVEN a boolean value WHEN Get is called THEN return the same boolean value`() {
        runBlocking {
            // given
            val booleanValue = false

            given(preferences.firstTimeList)
                .willReturn(booleanValue)

            // when
            val result = firstTimeListRepositoryImp.getFirstTimeList()

            // then
            result equalTo booleanValue
        }
    }

    @Test
    fun `GIVEN a saved new boolean value WHEN Get is called THEN the new boolean value must be returned`() {
        runBlocking {
            // given
            val booleanValue = true

            doNothing().`when`(preferences).firstTimeList = booleanValue
            given(preferences.firstTimeList)
                .willReturn(booleanValue)

            // when
            val resultOfSave = firstTimeListRepositoryImp.save(booleanValue)
            val returnOfGet = firstTimeListRepositoryImp.getFirstTimeList()

            // then
            resultOfSave equalTo Unit
            returnOfGet equalTo booleanValue
        }
    }
}
