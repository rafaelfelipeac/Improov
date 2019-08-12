package com.rafaelfelipeac.mountains.di.modules

import android.content.Context
import androidx.room.Room
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.database.RoomDatabase
import com.rafaelfelipeac.mountains.database.goal.GoalDAO
import com.rafaelfelipeac.mountains.database.habit.HabitDAO
import com.rafaelfelipeac.mountains.database.historic.HistoricDAO
import com.rafaelfelipeac.mountains.database.item.ItemDAO
import com.rafaelfelipeac.mountains.database.user.UserDAO
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
    fun provideUserDAO(appDatabase: RoomDatabase): UserDAO = appDatabase.userDAO()

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
