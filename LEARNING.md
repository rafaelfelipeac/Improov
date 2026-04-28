# LEARNING — Revival Notes & Decisions

This document captures the reasoning, trade-offs, and lessons learned while reviving Improov.
It should grow incrementally as the v2 work progresses.

The goal is not to restate code line by line. The goal is to preserve context: why decisions were made, what risks were uncovered, and what the next engineer should understand before changing the project again.

The work should be split into small sessions with a clear stop point. When a change reaches a coherent boundary, commit it and start a fresh session for the next boundary instead of carrying a large refactor forward in one run.

---

## Revival Intent

Improov v2 is a legacy revival, not a quick cleanup.

The planned direction is:

- Recover the build first.
- Modernize the Android foundation.
- Replace the XML UI with Compose.
- Preserve important product behavior.
- Protect local user data and backup compatibility.
- Use the project as a deliberate refactoring and decision-making exercise.

The work can ship as a single v2.0.0 pull request, but it should be developed as a sequence of reviewable phases and smaller sessions.

## Reference Project

Hermes is the reference project for the desired quality bar.

The relevant ideas to carry into Improov are:

- Compose + Material 3 as the UI foundation.
- Hilt for dependency injection unless a future decision explicitly chooses otherwise.
- Room as the source of truth for durable app data.
- DataStore Preferences for settings.
- StateFlow-based screen state.
- Clear UI -> ViewModel -> Repository boundaries.
- Schema-driven backup compatibility.
- Strong localization discipline.
- Project-level AI guidance and living documentation.
- Tests that prefer fakes over mocks where practical.

Hermes is a reference for engineering maturity and workflow, not a product template to copy feature-for-feature.

## First Decisions

- Use `revival/v2` as the long-lived branch for the v2 work.
- Keep documentation in English.
- Treat `docs/spec.md` as the current behavior source of truth.
- Treat `docs/legacy-revival-audit.md` as the legacy-state snapshot.
- Treat `docs/revival-v2-plan.md` as the modernization plan.
- Update this file whenever a meaningful technical decision or lesson emerges.

## Session Discipline

- Prefer one focused goal per session.
- Stop when the diff is coherent, even if the broader refactor is not finished.
- Commit at the end of the session when it makes sense.
- Start a new session for the next boundary instead of widening the current one.

## Immediate Technical Lesson

The first blocker is not UI or architecture; it is executability.

The legacy Gradle wrapper (`6.1.1`) fails on the current JDK 17 environment before the project can compile. That means the first implementation phase must recover a reliable toolchain before any XML removal, Compose migration, or deeper refactor.

This is a useful constraint: a large refactor needs feedback loops first.

## Toolchain Revival Step 1

The first build change is intentionally smaller than the final Hermes-like target.

Instead of jumping directly to Compose, Gradle 9, AGP 9, and Kotlin 2, the first step moves the legacy project to a JDK 17-compatible build baseline while keeping the old XML/Fragment app structure intact. This separates "can the project build?" from "what should the v2 architecture become?"

Changes in this step:

- Gradle wrapper moved from `6.1.1` to `8.9`.
- Android Gradle Plugin moved from `4.0.1` to `8.7.3`.
- Kotlin Gradle plugin moved from `1.3.72` to `1.9.25`.
- Navigation Safe Args plugin moved from the old `android.arch` coordinate to AndroidX.
- `kotlin-android-extensions` was removed because the app already uses ViewBinding and the plugin is obsolete.
- `namespace` was added for modern AGP.
- `jcenter()` was removed from project repositories.
- The old support Preference dependency was replaced by AndroidX Preference.
- The legacy ktlint/detekt `preBuild` hook was removed so static analysis modernization does not block the first compile feedback loop.

This is a bridge, not the final v2 stack.

## Room and Apple Silicon

After the first toolchain update, the build reached kapt and failed inside Room annotation processing.

The failure was caused by Room `2.2.5` trying to load an old SQLite JDBC native library that does not provide a macOS arm64 binary:

`No native library is found for os.name=Mac and os.arch=aarch64`

Updating Room to `2.6.1` is part of the build revival, not a behavior refactor. It lets Room annotation processing run on the current development machine while keeping the existing Room APIs and DAOs intact for now.

Modern kapt/Room also exposed a DAO annotation issue: static-importing `OnConflictStrategy.REPLACE` produced Java stubs with `onConflict = null`. Referencing `OnConflictStrategy.REPLACE` explicitly in each DAO keeps the annotation stable across the newer toolchain.

## Dagger Metadata Compatibility

After Room was updated, kapt reached Dagger and failed because Dagger `2.27` could not read metadata produced by the newer Kotlin toolchain.

The project is still on Dagger 2 for this bridge step, but the dependency was updated to `2.51.1` so annotation processing can run with Kotlin `1.9.25`.

This is not the final DI decision. The v2 plan still treats Hilt as the likely target because it matches Hermes, but switching DI frameworks should happen after the build baseline is stable.

Newer Dagger also rejects `@Binds` extension functions. `Application.context(): Context` was changed to a regular `bindContext(application: Application): Context` binding. The dependency graph intent is unchanged; only the declaration style was modernized.

## Kotlin and AndroidX Strictness

Once annotation processing passed, the Kotlin compiler surfaced several legacy API assumptions:

- `AnimatorListenerAdapter` callbacks now resolve with non-null `Animator` parameters.
- AGP 8 requires explicit `buildConfig = true` when app code references `BuildConfig`.
- The manifest package attribute is ignored when `namespace` is configured, so it was removed.
- `Snackbar` internals should be referenced through Material's resource ids, not the app `R`.
- Some AndroidX/AppCompat overrides now use non-null parameters/return types.
- Nullable `when` subjects must handle `null` explicitly.

These are compatibility fixes only. They should disappear naturally when the XML/Fragment UI is replaced by Compose.

## Modern Lint Baseline

After build and unit tests passed, `lintDebug` exposed three blocking issues:

- `targetSdkVersion 29` is no longer acceptable for Play-oriented lint checks.
- The launcher activity needs an explicit `android:exported` value.
- The launcher activity incorrectly declared `ACTION_VIEW`, which made external Play Store intents look like they could resolve to the app's own non-exported activity.

The bridge baseline now targets SDK 35, marks the launcher activity as exported, and removes the unnecessary `ACTION_VIEW` action from the launcher intent filter.

This target SDK bump makes the existing legacy storage backup flow even more obviously temporary. Backup/export/import must move to Storage Access Framework during the v2 persistence phase.

## Test Runtime Revival

After `assembleDebug` passed, `testDebugUnitTest` compiled and started running but most tests failed during Mockito initialization.

The failure came from the old `mockito-inline:3.2.4` stack trying to instrument newer bytecode/JDK classes. Updating Mockito inline to `5.2.0` keeps the current tests intact for the bridge step.

Long term, Improov should move toward the Hermes preference of fake-based tests instead of broad mocking. For Phase 0, the goal is to recover the existing test signal before rewriting test style.

## Build Hygiene Step 1

After the project compiled again, the next move was to reduce build-script entropy before adding any new architecture.

This step introduced `gradle/libs.versions.toml` and moved plugin/dependency versions there. The goal is not just aesthetic: a version catalog turns future upgrades into explicit, centralized decisions instead of scattered edits across Gradle files.

The migration intentionally kept most runtime library versions unchanged from the bridge baseline. Centralizing versions and upgrading versions are separate refactor steps. Keeping them separate makes regressions easier to attribute.

Legacy `ktlint.gradle` and `detekt.gradle` JavaExec scripts were removed in favor of real Gradle plugins. This aligns Improov with the Hermes approach and gives future CI a stable vocabulary:

- `ktlintCheck`
- `detekt`
- `lintDebug`
- `testDebugUnitTest`
- `assembleDebug`

This is also a refactoring lesson: before changing app architecture, make the build system express the project clearly.

## Static Analysis Baselines

Re-enabling ktlint and detekt exposed a large amount of legacy style debt. The important decision here is to baseline the existing debt instead of mass-formatting or rewriting every Kotlin file immediately.

Why this matters:

- A baseline keeps CI useful for new changes without pretending the legacy code is already clean.
- It avoids noisy commits that would make future architecture changes harder to review.
- It lets the project pay down issues by feature area as XML/Fragment screens are replaced.
- It creates an explicit inventory of debt in `app/config/ktlint/baseline.xml` and `app/detekt-baseline.xml`.

This is a practical large-refactor strategy: freeze the legacy surface, protect new work, then shrink the baseline as each migrated area becomes owned by v2.

Detekt also reported deprecated keys in `default-detekt-config.yml`. That is build tooling debt, not product behavior. It should be cleaned in a focused step so config migration is not mixed with UI or architecture migration.

## CI Modernization Step 1

The GitHub Actions workflows were still using JDK 8, old action versions, and an NDK install that is no longer needed for the revived build.

The CI workflow now uses:

- `actions/checkout@v4`
- `actions/setup-java@v4` with Temurin 17
- `gradle/actions/setup-gradle@v4`
- `assembleDebug`
- `testDebugUnitTest`
- `lintDebug`
- `ktlintCheck`
- `detekt`

This mirrors the local feedback loop. For a revival branch, that alignment matters because every future refactor should prove it did not break the current executable baseline.

## Detekt Configuration Cleanup

After the baseline step, detekt passed but still printed many warnings about deprecated configuration keys.

This is a different category of debt from source-code findings. The source-code baseline says "these legacy issues exist." Deprecated tool configuration says "the build definition itself is aging." For a long-running revival, the second problem should be fixed early because stale tooling noise hides useful feedback.

The detekt config was updated to the current key names while preserving the previous rule intent where possible:

- `ComplexMethod` became `CyclomaticComplexMethod`.
- comma-separated scalar values became YAML arrays.
- deprecated `excludeAnnotated*` keys became `ignoreAnnotated`.
- deprecated rules already handled by the compiler or migrated out of the active ruleset were removed.

The lesson: clean the warning channel before starting high-risk code movement. A quiet build is not just aesthetics; it is a refactoring instrument.

## Compose Foundation Step 1

The first Compose step intentionally does not replace a screen yet.

Instead, the project now has a small Compose foundation:

- Compose enabled in the Android module.
- Compose dependencies centralized in the version catalog.
- A Material 3 theme under `core/ui/theme`.
- A minimal `ImproovAppShell` composable that wraps future screens in the v2 theme.

This is a refactoring strategy decision. Adding the new UI stack before migrating screens gives the branch a safe integration point: the compiler, dependencies, theme, and static analysis can fail early while the legacy app behavior is still unchanged.

The legacy XML/Fragment UI is still the running app. That is temporary, but useful. It lets the project carry both worlds for a short period:

- legacy UI remains the behavior reference;
- Compose becomes the target surface;
- future commits can migrate one screen or flow at a time.

This keeps the v2 rewrite from becoming a blind rewrite. The new architecture is introduced as executable infrastructure before it owns product behavior.

The static-analysis configuration also needed a Compose-aware adjustment. Compose uses PascalCase function names for composables that represent UI nodes, and theme color files naturally contain color literals. The rules now allow `@Composable` PascalCase names and exclude the theme color-token file from `MagicNumber`.

That is not weakening quality. It is aligning the quality gate with the framework's idioms so violations remain meaningful.

## Compose Navigation Shell

The next v2 foundation step added a Compose Navigation shell without replacing the legacy launcher.

The project now has:

- `V2MainActivity`, a non-exported activity that can host the Compose app root.
- `ImproovAppRoot`, the first Compose root entry point.
- `ImproovNavHost`, a Navigation Compose graph.
- `ImproovRoutes`, route constants that mirror the existing legacy navigation graph.

The legacy `MainActivity` remains the launcher. This is deliberate. It gives the project an executable v2 surface for experiments while the production behavior remains anchored in the known XML/Fragment flow.

This is the strangler-fig pattern applied to UI migration: create the new host beside the old one, route future migrated screens through the new host, then remove the old host when it no longer owns behavior.

The route names are intentionally product-oriented (`goals`, `profile`, `backup`) rather than Fragment-oriented. That keeps the v2 navigation language independent from the legacy implementation classes.

## Persistence Safety Step 1

After the Compose shell, the next priority moved back to architecture and data safety.

The important finding was that the legacy production database builder used both `allowMainThreadQueries()` and `fallbackToDestructiveMigration()`, while the Room database had `exportSchema = false`.

That combination is risky for a revival:

- main-thread queries hide persistence work inside UI flows;
- destructive migration can erase user data when a migration path is missing;
- missing schema export prevents proper migration tests.

The first persistence step enabled Room schema export, generated the current version `49` schema, and removed the production destructive-migration and main-thread-query shortcuts.

This is intentionally earlier than rich screen migration. A modern Compose UI over unsafe persistence would only modernize the surface. v2 needs the data contract to become explicit before the app behavior is rewritten.

## Migration Test Infrastructure

Room migration tests depend on committed historical schemas.

Improov did not have schema export enabled before the v2 revival, so there is no trustworthy `48.json` artifact in the repository. That means we should not pretend to fully validate the legacy `48 -> 49` migration from source alone.

The first migration-test step therefore does something narrower and honest:

- adds `room-testing`;
- exposes `app/schemas` to Android instrumentation tests;
- verifies that the current `49` schema can be created from the exported schema.

The lesson is practical: migration testing has to start before you need it. From this point forward, every Room schema change can be tested against a committed previous schema.

Enabling the first instrumentation test also exposed two build-health issues:

- the old AndroidX Test stack was incompatible with the current target SDK because its generated test activities did not declare `android:exported`;
- variant `resValue` entries that referenced other string resources broke test APK resource linking, so they were changed to literal values matching the existing strings.

Both are examples of why architecture work often starts by improving testability. The moment a new safety test is added, old assumptions in the build become visible.

## Backup Import Transaction

The legacy backup import flow parsed JSON and then deleted existing rows before inserting replacement data. That meant a malformed or partially incompatible import path could put current data at risk.

The first hardening step changed the order and boundary:

- parse the backup before entering the replacement path;
- replace `goal`, `item`, and `historic` tables inside `RoomDatabase.runInTransaction`;
- use DAO-level `DELETE FROM` operations instead of loading every row only to delete it;
- keep invalid JSON away from the transaction entirely.

This is still not the final v2 backup architecture. The backup format remains unversioned and settings are still written through SharedPreferences outside the Room transaction. But the highest-risk part, replacing relational app data, is now atomic.

The Dagger wiring also moved `DatabaseDataSource` construction into `BackupModule`. That avoided adding style suppressions for a new `@Inject constructor` pattern while keeping dependency creation explicit.

## Legacy Backup Validation

The next problem after transaction safety was input shape. The old import path accepted whatever Gson could deserialize into the legacy model, even if required sections were missing.

The small fix was to decode through a nullable legacy payload and only map into the app's backup model when every required field is present. That keeps malformed or partial legacy files out of the replacement path without forcing the full v2 backup format decision yet.

This is a useful refactor lesson: when a large redesign is still ahead, a narrow validation boundary can remove real risk now and still preserve freedom for the larger architecture step later.

## Versioned Backup Envelope

The first explicit backup contract is intentionally small: export now writes a `schemaVersion` envelope around the existing backup payload. Schema version `1` still carries the legacy `Database` shape inside `database`, so this is not the final v2 backup format yet.

Import can read both the versioned envelope and validated legacy unversioned files. Unsupported schema versions fail before the replace path starts, which creates the boundary needed for a future format split without breaking legacy restores.

## Domain Rule Extraction

Phase 3 started pulling the most important goal behavior out of the fragments and into testable rule helpers:

- goal percentage and completion checks now live in `GoalRules`;
- goal and item reorder logic now uses shared swap helpers;
- item completion toggles and historic creation now come from small domain helpers;
- goal form validation now delegates the divide-and-conquer and counter value checks to the domain layer;
- the shared `Goal` model keeps a safe local percentage calculation for compatibility, but the feature-level rules own the new decision points.

The focused rule tests passed with an isolated Gradle cache under `/tmp/improov-gradle`. The broader legacy `testDebugUnitTest` run still needs attention separately because the suite is large and the default wrapper cache was locked in the sandboxed run.

Next anchor: `phase-4-compose-screens`.
