package com.rafaelfelipeac.improov.features.settings.domain.usecase

import com.rafaelfelipeac.improov.core.extension.equalTo
import com.rafaelfelipeac.improov.features.settings.domain.repository.LanguageRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SaveLanguageUseCaseTest {

    @Mock
    internal lateinit var mockLanguageRepository: LanguageRepository

    private lateinit var saveLanguageUseCase: SaveLanguageUseCase

    @Before
    fun setup() {
        saveLanguageUseCase = SaveLanguageUseCase(mockLanguageRepository)
    }

    @Test
    fun `GIVEN a language WHEN use saveLanguageUseCase THEN return just a Unit value`() {
        runBlocking {
            // given
            val language = "pt_br"

            given(mockLanguageRepository.save(language))
                .willReturn(Unit)

            // when
            val result = saveLanguageUseCase(language)

            // then
            result equalTo Unit
        }
    }
}
