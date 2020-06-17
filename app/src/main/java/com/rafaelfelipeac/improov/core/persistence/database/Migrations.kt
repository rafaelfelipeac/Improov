package com.rafaelfelipeac.improov.core.persistence.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_48_49 = object : Migration(DATABASE_VERSION_48, DATABASE_VERSION_49) {

    override fun migrate(database: SupportSQLiteDatabase) {
        // no need for migration code
    }
}
