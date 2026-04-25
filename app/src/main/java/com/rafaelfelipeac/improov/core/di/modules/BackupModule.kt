package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.rafaelfelipeac.improov.core.di.key.FragmentKey
import com.rafaelfelipeac.improov.core.di.key.ViewModelKey
import com.rafaelfelipeac.improov.core.persistence.database.RoomDatabase
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.backup.data.DatabaseDataSource
import com.rafaelfelipeac.improov.features.backup.domain.repository.DatabaseRepository
import com.rafaelfelipeac.improov.features.backup.presentation.BackupFragment
import com.rafaelfelipeac.improov.features.backup.presentation.BackupViewModel
import com.rafaelfelipeac.improov.features.commons.data.dao.GoalDao
import com.rafaelfelipeac.improov.features.commons.data.dao.HistoricDao
import com.rafaelfelipeac.improov.features.commons.data.dao.ItemDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class BackupModule {
    @Binds
    abstract fun databaseRepository(databaseDataSource: DatabaseDataSource): DatabaseRepository

    @Binds
    @IntoMap
    @FragmentKey(BackupFragment::class)
    abstract fun bindBackupFragment(backupFragment: BackupFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(BackupViewModel::class)
    abstract fun bindBackupViewModel(backupViewModel: BackupViewModel): ViewModel

    companion object {
        @Provides
        @Suppress("LongParameterList")
        fun provideDatabaseDataSource(
            roomDatabase: RoomDatabase,
            goalDao: GoalDao,
            historicDao: HistoricDao,
            itemDao: ItemDao,
            preferences: Preferences,
            gson: Gson,
        ): DatabaseDataSource =
            DatabaseDataSource(
                roomDatabase,
                goalDao,
                historicDao,
                itemDao,
                preferences,
                gson,
            )
    }
}
