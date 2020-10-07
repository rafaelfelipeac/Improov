package com.rafaelfelipeac.improov.features.profile.data.repository

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.profile.data.respository.FirstTimeAddRepositoryImpl
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
    fun `GIVEN a boolean value WHEN Get is called THEN return the same boolean value`() {
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
    fun `GIVEN a saved new boolean value WHEN Get is called THEN the new boolean value must be returned`() {
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
