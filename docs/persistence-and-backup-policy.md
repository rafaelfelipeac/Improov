# Persistence and Backup Policy

Last updated: 2026-04-25

## Goal

Improov v2 must preserve user data while the legacy app is migrated to a modern architecture.

This policy defines the persistence and backup rules that should guide the refactor. The current codebase does not satisfy every rule yet; this document is the target contract for the revival work.

## Current Persistence Snapshot

Current Room database:

- Database class: `com.rafaelfelipeac.improov.core.persistence.database.RoomDatabase`
- Current version: `49`
- Schema export path: `app/schemas/com.rafaelfelipeac.improov.core.persistence.database.RoomDatabase/49.json`
- Tables:
  - `goal`
  - `item`
  - `historic`

Current known schema constraints:

- `goal`, `item`, and `historic` use auto-generated primary keys.
- `item.goalId` and `historic.goalId` are plain integer columns today.
- There are no declared foreign keys.
- There are no declared indices.
- `MIGRATION_48_49` exists and is a no-op migration.

## Persistence Rules

- Room schema export must stay enabled.
- Destructive migrations are not allowed.
- Main-thread database queries are not allowed in production database builders.
- Every Room version bump must commit the generated schema JSON.
- Every schema-changing migration must have a migration test.
- Data deletion, import, and replace flows should be transactional.
- Foreign keys and indices should be added when the v2 schema is redesigned.
- Room remains the source of truth for goals, items, and historic progress.
- DataStore should be used for settings only.

## Backup Compatibility Contract

Backup compatibility must be decided by backup schema version, not app version.

The legacy backup format currently has no explicit schema version. During v2, backup should move to an explicit format:

```json
{
  "schemaVersion": 1,
  "appVersion": "2.0.0",
  "settings": {},
  "goals": [],
  "items": [],
  "historics": []
}
```

Rules for backup v2:

- Unknown future `schemaVersion` values must fail with a friendly unsupported-version error.
- Invalid JSON must fail without changing existing data.
- Missing required sections must fail without changing existing data.
- Replace-mode import must be transactional.
- Legacy unversioned backups must be handled explicitly:
  - either through a `LegacyBackupDecoder`;
  - or through a friendly unsupported-legacy-backup error.

The decision must be made before the v2 Backup screen ships.

## Current Risks

- Legacy import deletes current rows before validating and inserting all replacement rows.
- Legacy import is not transactional.
- Legacy backup has no explicit schema version.
- Legacy backup serializes Room data models directly.
- Settings are still stored in SharedPreferences.
- Relationship integrity between goals, items, and historic rows is implicit.

## Refactor Direction

Recommended order:

1. Keep Room schema export enabled.
2. Add migration-test infrastructure.
3. Add a backup compatibility codec with explicit result types.
4. Introduce a versioned backup model separate from Room entities.
5. Make import transactional in the repository/data layer.
6. Migrate settings from SharedPreferences to DataStore.
7. Redesign Room relationships with foreign keys and indices.

## Learning Note

Persistence work should precede complex UI migration because Compose can make the app look modern while still preserving unsafe data behavior underneath. For v2, data safety is part of the product, not an implementation detail.
