package com.rafaelfelipeac.improov.core.di.modules

import android.content.Context
import androidx.room.Room
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.persistence.database.RoomDatabase
import com.rafaelfelipeac.improov.features.goal.data.HistoricDAO
import com.rafaelfelipeac.improov.features.goal.data.ItemDAO
import com.rafaelfelipeac.improov.features.commons.data.GoalDAO
import com.rafaelfelipeac.improov.features.commons.data.HabitDAO
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object PersistenceModule {

    @Provides
    @Singleton
    @JvmStatic
    fun providesDatabase(context: Context): RoomDatabase =
        Room.databaseBuilder(context, RoomDatabase::class.java, context.getString(R.string.database_name))
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @JvmStatic
    fun provideGoalDAO(appDatabase: RoomDatabase): GoalDAO = appDatabase.goalDAO()

    @Provides
    @JvmStatic
    fun provideHabitDAO(appDatabase: RoomDatabase): HabitDAO = appDatabase.habitDAO()

    @Provides
    @JvmStatic
    fun provideItemDAO(appDatabase: RoomDatabase): ItemDAO = appDatabase.itemDAO()

    @Provides
    @JvmStatic
    fun provideHistoricDAO(appDatabase: RoomDatabase): HistoricDAO = appDatabase.historicDAO()
}
