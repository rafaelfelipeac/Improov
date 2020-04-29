package com.rafaelfelipeac.improov.base

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.rafaelfelipeac.improov.core.persistence.database.RoomDatabase
import org.junit.After
import org.junit.Before

open class BaseInstTest {

    lateinit var roomDatabase: RoomDatabase

    @Before
    fun initDb() {
        roomDatabase = Room
            .inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().context,
                RoomDatabase::class.java
            )
            .build()
    }

    @After
    fun closeDb() {
        roomDatabase.close()
    }
}
