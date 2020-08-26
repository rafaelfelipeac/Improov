package com.rafaelfelipeac.improov.features.profile.data.repository

import com.rafaelfelipeac.improov.core.extension.equalTo
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.profile.data.respository.NameRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NameRepositoryImplTest {

    @Mock
    internal lateinit var preferences: Preferences

    private lateinit var nameRepositoryImpl: NameRepositoryImpl

    @Before
    fun setup() {
        nameRepositoryImpl = NameRepositoryImpl(preferences)
    }

    @Test
    fun `GIVEN a certain name WHEN GetName is called THEN return the same name`() {
        runBlocking {
            // given
            val name = "User Name"

            given(preferences.name)
                .willReturn(name)

            // when
            val result = nameRepositoryImpl.getName()

            // then
            result equalTo name
        }
    }

    @Test
    fun `GIVEN a saved new name WHEN GetName is called THEN the new name must be returned`() {
        runBlocking {
            // given
            val name = "New User Name"

            doNothing().`when`(preferences).name = name
            given(preferences.name)
                .willReturn(name)

            // when
            val resultOfSave = nameRepositoryImpl.save(name)
            val returnOfGet = nameRepositoryImpl.getName()

            // then
            resultOfSave equalTo Unit
            returnOfGet equalTo name
        }
    }
}
