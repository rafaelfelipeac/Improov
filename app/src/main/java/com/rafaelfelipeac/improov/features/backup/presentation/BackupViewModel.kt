package com.rafaelfelipeac.improov.features.backup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.features.backup.domain.usecase.ExportDatabaseUseCase
import com.rafaelfelipeac.improov.features.backup.domain.usecase.GetExportDateUseCase
import com.rafaelfelipeac.improov.features.backup.domain.usecase.GetImportDateUseCase
import com.rafaelfelipeac.improov.features.backup.domain.usecase.ImportDatabaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class BackupViewModel @Inject constructor(
    private val exportDatabaseUseCase: ExportDatabaseUseCase,
    private val importDatabaseUseCase: ImportDatabaseUseCase,
    private val getExportDateUseCase: GetExportDateUseCase,
    private val getImportDateUseCase: GetImportDateUseCase
) : ViewModel() {

    val export: Flow<String> get() = _export.filterNotNull()
    private val _export = MutableStateFlow<String?>(null)
    val import: Flow<Boolean> get() = _import.filterNotNull()
    private val _import = MutableStateFlow<Boolean?>(null)
    val exportDate: Flow<Long> get() = _exportDate.filterNotNull()
    private val _exportDate = MutableStateFlow<Long?>(null)
    val importDate: Flow<Long> get() = _importDate.filterNotNull()
    private val _importDate = MutableStateFlow<Long?>(null)

    fun loadData() {
        getExportDate()
        getImportDate()
    }

    fun exportDatabase() {
        viewModelScope.launch {
            _export.value = exportDatabaseUseCase()
        }
    }

    fun importDatabase(databaseBackup: String) {
        viewModelScope.launch {
            _import.value = importDatabaseUseCase(databaseBackup)
        }
    }

    fun getExportDate() {
        viewModelScope.launch {
            _exportDate.value = getExportDateUseCase()
        }
    }

    fun getImportDate() {
        viewModelScope.launch {
            _importDate.value = getImportDateUseCase()
        }
    }
}
