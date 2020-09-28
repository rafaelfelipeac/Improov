package com.rafaelfelipeac.improov.features.backup.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.backup.domain.usecase.ExportDatabaseUseCase
import com.rafaelfelipeac.improov.features.backup.domain.usecase.GetExportDateUseCase
import com.rafaelfelipeac.improov.features.backup.domain.usecase.GetImportDateUseCase
import com.rafaelfelipeac.improov.features.backup.domain.usecase.ImportDatabaseUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class BackupViewModel @Inject constructor(
    private val exportDatabaseUseCase: ExportDatabaseUseCase,
    private val importDatabaseUseCase: ImportDatabaseUseCase,
    private val getExportDateUseCase: GetExportDateUseCase,
    private val getImportDateUseCase: GetImportDateUseCase
) : BaseViewModel() {

    val export: LiveData<String> get() = _export
    private val _export = MutableLiveData<String>()
    val import: LiveData<Boolean> get() = _import
    private val _import = MutableLiveData<Boolean>()
    val exportDate: LiveData<Long> get() = _exportDate
    private val _exportDate = MutableLiveData<Long>()
    val importDate: LiveData<Long> get() = _importDate
    private val _importDate = MutableLiveData<Long>()

    override fun loadData() {
        getExportDate()
        getImportDate()
    }

    fun exportDatabase() {
        viewModelScope.launch {
            _export.postValue(exportDatabaseUseCase())
        }
    }

    fun importDatabase(databaseBackup: String) {
        viewModelScope.launch {
            _import.postValue(importDatabaseUseCase(databaseBackup))
        }
    }

    fun getExportDate() {
        viewModelScope.launch {
            _exportDate.postValue(getExportDateUseCase())
        }
    }

    fun getImportDate() {
        viewModelScope.launch {
            _importDate.postValue(getImportDateUseCase())
        }
    }
}
