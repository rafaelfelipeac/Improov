package com.rafaelfelipeac.mountains.di.modules

import android.content.Context
import androidx.room.Room
import com.rafaelfelipeac.mountains.database.RoomDatabase
import com.rafaelfelipeac.mountains.database.goal.GoalDAO
import com.rafaelfelipeac.mountains.database.habit.HabitDAO
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object PersistenceModule {

    @Provides
    @Singleton
    @JvmStatic
    fun providesDatabase(context: Context): RoomDatabase =
        Room.databaseBuilder(context, RoomDatabase::class.java, "db-mountains")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @JvmStatic
    fun provideGoalDAO(appDatabase: RoomDatabase): GoalDAO = appDatabase.goalDAO()

    @Provides
    @JvmStatic
    fun provideHabitDAO(appDatabase: RoomDatabase): HabitDAO = appDatabase.habitDAO()
}
