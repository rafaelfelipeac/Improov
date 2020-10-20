package com.rafaelfelipeac.improov.features.backup.domain.usecase

import com.rafaelfelipeac.improov.base.DataProviderTest.getDate
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.backup.domain.repository.DatabaseRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetExportDateUseCaseTest {

    @Mock
    internal lateinit var mockDatabaseRepository: DatabaseRepository

    private lateinit var getExportDateUseCase: GetExportDateUseCase

    @Before
    fun setup() {
        getExportDateUseCase = GetExportDateUseCase(mockDatabaseRepository)
    }

    @Test
    fun `GIVEN a Long value WHEN use getExportDateUseCase THEN return the same Long value`() {
        runBlocking {
            // given
            val exportDate = getDate()
            given(mockDatabaseRepository.getExportDate())
                .willReturn(exportDate)

            // when
            val result = getExportDateUseCase()

            // then
            result equalTo exportDate
        }
    }
}
