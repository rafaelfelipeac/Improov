package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.key.FragmentKey
import com.rafaelfelipeac.improov.core.di.key.ViewModelKey
import com.rafaelfelipeac.improov.features.backup.data.repository.DatabaseRepositoryImpl
import com.rafaelfelipeac.improov.features.backup.domain.repository.DatabaseRepository
import com.rafaelfelipeac.improov.features.backup.presentation.BackupFragment
import com.rafaelfelipeac.improov.features.backup.presentation.BackupViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BackupModule {

    @Binds
    abstract fun databaseRepository(databaseRepositoryImpl: DatabaseRepositoryImpl): DatabaseRepository

    @Binds
    @IntoMap
    @FragmentKey(BackupFragment::class)
    abstract fun bindBackupFragment(backupFragment: BackupFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(BackupViewModel::class)
    abstract fun bindBackupViewModel(backupViewModel: BackupViewModel): ViewModel
}
