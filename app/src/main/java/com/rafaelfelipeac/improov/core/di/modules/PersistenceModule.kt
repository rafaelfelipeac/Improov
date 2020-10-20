package com.rafaelfelipeac.improov.core.di.modules

import android.content.Context
import androidx.room.Room
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.persistence.database.MIGRATION_48_49
import com.rafaelfelipeac.improov.core.persistence.database.RoomDatabase
import com.rafaelfelipeac.improov.features.commons.data.dao.GoalDao
import com.rafaelfelipeac.improov.features.commons.data.dao.HistoricDao
import com.rafaelfelipeac.improov.features.commons.data.dao.ItemDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object PersistenceModule {

    @Provides
    @Singleton
    fun providesDatabase(context: Context): RoomDatabase =
        Room.databaseBuilder(context, RoomDatabase::class.java, context.getString(R.string.database_name))
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .addMigrations(MIGRATION_48_49)
            .build()

    @Provides
    fun provideGoalDao(appDatabase: RoomDatabase): GoalDao = appDatabase.goalDao()

    @Provides
    fun provideItemDao(appDatabase: RoomDatabase): ItemDao = appDatabase.itemDao()

    @Provides
    fun provideHistoricDao(appDatabase: RoomDatabase): HistoricDao = appDatabase.historicDao()
}
