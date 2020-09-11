package com.rafaelfelipeac.improov.features.backup.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.backup.domain.usecase.ExportDatabaseUseCase
import com.rafaelfelipeac.improov.features.backup.domain.usecase.ImportDatabaseUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class BackupViewModel @Inject constructor(
    private val exportDatabaseUseCase: ExportDatabaseUseCase,
    private val importDatabaseUseCase: ImportDatabaseUseCase
) : BaseViewModel() {
    val export: LiveData<String> get() = _export
    private val _export = MutableLiveData<String>()
    val import: LiveData<Boolean> get() = _import
    private val _import = MutableLiveData<Boolean>()

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
}
