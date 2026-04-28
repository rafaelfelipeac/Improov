# Persistence and Backup Policy

Last updated: 2026-04-25

## Goal

Improov v2 must preserve user data while the legacy app is migrated to a modern architecture.

This policy defines the persistence and backup rules that should guide the refactor. The current codebase does not satisfy every rule yet; this document is the target contract for the revival work.

Persistence and backup changes should be made in small, independently testable sessions. Each session should stop at a coherent boundary and end in a commit when the diff is ready.

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

- Historical Room schemas before version `49` were not preserved in the repository.
- Legacy import still uses the unversioned backup shape.
- Backup preference writes still happen outside the Room transaction because settings are still written through DataStore after the Room replacement work.
- Legacy backup has no explicit schema version.
- Legacy backup serializes Room data models directly.
- Settings are now stored in DataStore.
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

Execution rule:

- Do not bundle multiple persistence layers into one session unless the change is trivially small.
- Prefer one backup or persistence boundary per session so failures stay easy to isolate.

## Learning Note

Persistence work should precede complex UI migration because Compose can make the app look modern while still preserving unsafe data behavior underneath. For v2, data safety is part of the product, not an implementation detail.

## Migration Testing Status

Migration-test infrastructure is now available through `androidx.room:room-testing`.

The first instrumentation test verifies that the current schema export can be consumed by Room's `MigrationTestHelper`. It does not claim to validate historical migrations because schemas before version `49` were not committed by the legacy project.

Future database changes must add migration tests that start from the previous committed schema and validate the new version.

## Import Transaction Status

Legacy import now parses the backup before replacing current rows and performs the Room table replacement inside `RoomDatabase.runInTransaction`.

This improves the previous behavior because invalid JSON no longer reaches the destructive replacement path, and goal/item/historic replacement is atomic at the Room level.

Remaining limitation:

- settings are still written after the Room transaction, so backup import is not yet fully atomic across persistence layers.
- legacy import validation is still tied to the old payload shape instead of a versioned backup contract.

Full import atomicity should be revisited when settings move to DataStore and the backup model becomes explicitly versioned.

## Legacy Import Validation Status

Legacy import still accepts the already validated flat JSON shape and rejects malformed payloads before the Room replacement path runs.

This is still a compatibility bridge, not the final backup contract. Validation now protects the current app state from half-defined legacy payloads, but the decoder remains a bridge from the old shape into the newer backup model.

Versioned backups now also fail cleanly when the `schemaVersion` is unknown or malformed, so the import path does not reach the destructive replacement logic for unsupported future payloads.

## Versioned Backup Envelope Status

Export now writes an explicit schema-versioned envelope:

```json
{
  "schemaVersion": 1,
  "database": {}
}
```

Schema version `1` now wraps the backup payload that separates settings from persisted Room data. Import accepts both this versioned backup payload and validated legacy unversioned backups. Unknown schema versions fail before any replacement logic starts.

Remaining limitation:

- the schema-versioned envelope still sits on top of the backup payload, so the next session can focus on compatibility plumbing around the v2 model rather than the outer transport.
