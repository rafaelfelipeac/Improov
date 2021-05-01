package com.rafaelfelipeac.improov.features.goal.data.repository

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.goal.data.FirstTimeAddDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FirstTimeAddDataSourceTest {

    @Mock
    internal lateinit var preferences: Preferences

    private lateinit var firstTimeAddDataSource: FirstTimeAddDataSource

    @Before
    fun setup() {
        firstTimeAddDataSource = FirstTimeAddDataSource(preferences)
    }

    @Test
    fun `GIVEN a boolean value WHEN getFirstTimeAdd is called THEN return the same boolean value`() {
        runBlocking {
            // given
            val booleanValue = false

            given(preferences.firstTimeAdd)
                .willReturn(booleanValue)

            // when
            val result = firstTimeAddDataSource.getFirstTimeAdd()

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
            val resultOfSave = firstTimeAddDataSource.save(booleanValue)
            val returnOfGet = firstTimeAddDataSource.getFirstTimeAdd()

            // then
            resultOfSave equalTo Unit
            returnOfGet equalTo booleanValue
        }
    }
}
