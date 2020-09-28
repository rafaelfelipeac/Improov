package com.rafaelfelipeac.improov.features.backup.domain.usecase

import com.rafaelfelipeac.improov.base.DataProviderTest.getDate
import com.rafaelfelipeac.improov.core.extension.equalTo
import com.rafaelfelipeac.improov.features.backup.domain.repository.DatabaseRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetImportDateUseCaseTest {

    @Mock
    internal lateinit var mockDatabaseRepository: DatabaseRepository

    private lateinit var getImportDateUseCase: GetImportDateUseCase

    @Before
    fun setup() {
        getImportDateUseCase = GetImportDateUseCase(mockDatabaseRepository)
    }

    @Test
    fun `GIVEN a Long value WHEN use getImportDateUseCase THEN return the same Long value`() {
        runBlocking {
            // given
            val importDate = getDate()
            given(mockDatabaseRepository.getImportDate())
                .willReturn(importDate)

            // when
            val result = getImportDateUseCase()

            // then
            result equalTo importDate
        }
    }
}
