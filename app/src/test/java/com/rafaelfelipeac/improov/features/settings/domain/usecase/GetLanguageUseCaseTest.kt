package com.rafaelfelipeac.improov.features.settings.domain.usecase

import com.rafaelfelipeac.improov.base.DataProvider.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.settings.domain.repository.LanguageRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetLanguageUseCaseTest {

    @Mock
    internal lateinit var mockLanguageRepository: LanguageRepository

    private lateinit var getLanguageUseCase: GetLanguageUseCase

    @Before
    fun setup() {
        getLanguageUseCase = GetLanguageUseCase(mockLanguageRepository)
    }

    @Test
    fun `GIVEN a language WHEN use getLanguageUseCase THEN return the same language`() {
        runBlocking {
            // given
            val language = "en"

            given(mockLanguageRepository.getLanguage())
                .willReturn(language)

            // when
            val result = getLanguageUseCase.execute()

            // then
            result shouldBeEqualTo language
        }
    }
}
