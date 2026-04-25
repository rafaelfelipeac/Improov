package com.rafaelfelipeac.improov.core.persistence.database

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomDatabaseMigrationTest {
    @get:Rule
    val helper =
        MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            RoomDatabase::class.java,
        )

    @Test
    fun currentSchemaCanBeCreatedFromExportedSchema() {
        helper
            .createDatabase(TEST_DATABASE_NAME, DATABASE_VERSION_49)
            .close()
    }

    private companion object {
        const val TEST_DATABASE_NAME = "improov-migration-test"
    }
}
