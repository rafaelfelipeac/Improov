# Improov Legacy Revival Audit

Audit date: 2026-04-24

## Executive Summary

Improov is a native Android goals/habits app created in 2020. The product surface is well bounded: onboarding, goal list, goal detail/progress, goal form, profile, language settings, backup/import, and sample data. The codebase uses Kotlin, XML Views, Navigation Component, Room, SharedPreferences, Dagger 2, Coroutines/Flow, ViewBinding, ktlint, detekt, and unit/instrumented tests.

The current state is a platform-blocked legacy app. In the current machine, the project does not reach Gradle configuration because the wrapper uses Gradle 6.1.1/old Groovy and the only installed JDK is JDK 17. The observed error was `Could not initialize class org.codehaus.groovy.reflection.ReflectionCache`. The original CI used JDK 1.8, which confirms that the expected toolchain is old.

Before any large product refactor, the priority is to recover a reproducible build. Without that, every change is guesswork.

## Validated Current State

### Repository

- Local branch: `master`, tracking `origin/master`.
- The worktree was clean at the start of the audit.
- License: Apache 2.0.
- The README already warns that the project served its learning purpose and contains choices that would be done differently today.
- `projectFilesBackup/.idea/workspace.xml` exists and is likely an old IDE artifact that can be removed later.

### Declared Stack

- Android Gradle Plugin: `4.0.1`.
- Gradle wrapper: `6.1.1`.
- Kotlin Gradle plugin: `1.3.72`.
- Kotlin stdlib: `1.4.10`.
- compileSdk/targetSdk: `29`.
- minSdk: `21`.
- App version: `1.8.0`, versionCode `23`.
- Old Navigation Safe Args plugin: `android.arch.navigation:navigation-safe-args-gradle-plugin:1.0.0`.
- Room: `2.2.5`.
- Lifecycle: `2.2.0`.
- Navigation runtime: `2.3.0`.
- Dagger: `2.27`.
- Material: `1.3.0-alpha03`.
- Coroutines: `1.4.0`.
- Gson: `2.8.6`.
- Repositories: `google()`, `jcenter()`, `mavenCentral()`, JitPack.

### Build Attempt Result

Command executed:

```bash
GRADLE_USER_HOME=/tmp/improov-gradle ./gradlew testDebugUnitTest --no-daemon --console=plain
```

Result:

- The wrapper downloaded Gradle 6.1.1.
- Configuration failed before compilation.
- Likely cause: old Gradle/Groovy running on JDK 17.
- Locally available JDK: Zulu 17.0.14.
- Old CI configured `actions/setup-java@v1` with `java-version: 1.8`.

Conclusion: today the project is not buildable in this environment without installing JDK 8/11 or updating the Gradle/AGP/Kotlin toolchain.

## Technical Inventory

### Approximate Size

- Production Kotlin files: 140.
- Kotlin test files: 71.
- XML layouts: 25.
- Kotlin lines across `main`, `test`, and `androidTest`: approximately 11,139.
- XML lines under `res`: approximately 4,588.

### Main Structure

- `app/src/main/java/com/rafaelfelipeac/improov/core`
  - DI, persistence, extensions, base classes, locale helper, and configuration.
- `features/main`
  - `MainActivity`, toolbar, bottom navigation, FAB, and back handling.
- `features/splash`
  - Decides whether to open onboarding or the list based on preferences.
- `features/welcome`
  - Onboarding with ViewPager/adapter and persisted flag.
- `features/goal`
  - Goal list, form, detail, items, history, drag/drop, and swipe.
- `features/profile`
  - User name, edit flow, generate sample data, clear data, reopen welcome, backup, settings.
- `features/settings`
  - Preferences and language selection.
- `features/backup`
  - Exports/imports JSON containing preferences and Room tables.
- `features/dialog`
  - Simple custom dialogs.
- `features/commons`
  - Domain models, Room entities, DAOs, and goal type enum.

## Existing Functionality

### Onboarding and Splash

- Splash reads the `welcome` preference.
- If `welcome == false`, it opens onboarding.
- Onboarding saves the flag that unlocks normal app usage.
- Profile can reopen the initial messages.

### Goals

The app supports three goal types:

- `GOAL_LIST`: progress measured by completed items.
- `GOAL_COUNTER`: progress with fixed increment/decrement values.
- `GOAL_FINAL`: progress with manually entered variable values.
- `GOAL_NONE`: initial/no-selection state.

Each goal can be:

- Simple, with `singleValue`.
- "Divide and conquer", with `bronzeValue`, `silverValue`, and `goldValue` milestones.

Important `Goal` fields:

- Identity and display: `goalId`, `name`, `order`.
- Progress: `value`, `type`, `done`, `divideAndConquer`, `singleValue`, `bronzeValue`, `silverValue`, `goldValue`.
- Counter: `incrementValue`, `decrementValue`.
- Lifecycle state: `archived`.
- Dates: `createdDate`, `updatedDate`, `doneDate`, `undoneDate`, `archiveDate`, `date`.

Observation: archive fields and dates exist, but the list screen does not filter archived goals and the `archiveGoal` action in the fragment is incorrect/unused, setting `archived = false`.

### Goal List

- Uses a custom RecyclerView adapter through `BaseAdapter`.
- Sorting is done in memory by `order`.
- Drag and drop swaps the order of two goals and persists both.
- Swipe right completes/uncompletes a goal.
- If the goal has not reached 100%, a bottom sheet asks for confirmation before completing.
- Swipe left only reloads the item; it does not appear to archive/delete.

Risks:

- `Goal.getPercentage()` can divide by zero if the goal is inconsistent.
- `doneOrUndoneGoal` always writes `undoneDate`, even when completing.
- `GoalListAdapter` calls `holder.setIsRecyclable(false)`, which avoids recycling bugs but hurts performance and masks UI state issues.

### Goal Detail

- Displays progress with DecoView.
- Shows an item list for `GOAL_LIST`.
- Shows history for `GOAL_COUNTER` and `GOAL_FINAL`.
- Increment/decrement creates `Historic`.
- Manual final value creates `Historic`.
- Items can be created/edited via bottom sheet, ordered via drag/drop, and completed/uncompleted via swipe.

Risks:

- Too much domain logic lives in the Fragment, especially progress, completion, dates, history, and validations.
- The screen mixes rendering, mutable state, and persistence.
- The ViewModel mostly forwards calls to use cases and exposes simple events.
- Flows are collected with `lifecycleScope.launch { flow.collect { ... } }`, without `repeatOnLifecycle`, so collectors may stay active outside STARTED and update destroyed views depending on lifecycle timing.
- `MutableStateFlow(null).filterNotNull()` is used as an event mechanism. This can re-emit old events to new collectors and does not model screen state.

### Goal Form

- Creates/edits goals.
- Validates type, name, and values.
- Does not allow changing the type of an existing goal to avoid losing history.
- Controls first-time tips.

Risks:

- Validation and object composition rules are concentrated in the UI.
- `order` is calculated from the list loaded in memory.
- Date fields may become inconsistent.

### Profile

- Edits the name in SharedPreferences.
- Generates three sample goals: Counter, List, and Final.
- Clears goals, items, and historics.
- Opens backup, settings, and welcome.

Risks:

- Generate/clear data are destructive commands without a transactional layer.
- Clear data deletes rows by iterating item by item.

### Settings and Language

- Uses `PreferenceFragmentCompat`.
- Language is saved in `Preferences.language`, default `en`.
- Strings exist in `values` and `values-pt-rBR`.

Risks:

- `com.android.support:preference-v14:29.0.0` appears alongside AndroidX/Jetifier. It should be removed during modernization.
- Settings keys are strings in resources.
- Locale changes likely depend on a custom legacy helper.

### Backup/Import

- Exports JSON with:
  - `language`
  - `welcome`
  - `name`
  - `firstTimeList`
  - `firstTimeAdd`
  - goals
  - items
  - historics
- Import deletes all data and recreates tables/preferences.
- Uses `Gson`.
- Saves the file to `Environment.getExternalStorageDirectory()/Improov/Backup/Backup.txt`.
- Uses `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE`, and `requestLegacyExternalStorage`.

Risks:

- Legacy storage does not survive well on modern Android.
- `startActivityForResult` and permission APIs are old.
- Import is not transactional; a partial failure can leave data deleted.
- Backup has no schema version, signature, checksum, or structural validation.
- Failures use `printStackTrace` and return an empty string/false.
- `JsonParseException` does not cover all I/O or unexpected-structure errors.

## Persistence

### Room

Database:

- Name: `improov-db`.
- Current version: `49`.
- Entities:
  - `GoalDataModel` -> `goal` table.
  - `ItemDataModel` -> `item` table.
  - `HistoricDataModel` -> `historic` table.
- `exportSchema = false`.
- `Date` converter.

Current configuration:

- `allowMainThreadQueries()`.
- `fallbackToDestructiveMigration()`.
- `addMigrations(MIGRATION_48_49)`.

Important risks:

- `fallbackToDestructiveMigration()` can delete user data when a migration is missing.
- `allowMainThreadQueries()` allows performance regressions and masks architectural issues.
- DAOs only provide `getAll`, `get(id)`, `save`, and `delete`; filters and sorting are done in memory.
- There are no foreign keys between goal/item/historic.
- There are no indices for `goalId` in items/history.
- `get(id)` returns a non-null entity; if it does not exist, Room can fail at runtime.

### SharedPreferences

File:

- `com.rafaelfelipeac.improov.preferences`.

Keys:

- `KEY_WELCOME`
- `KEY_NAME`
- `KEY_LANGUAGE`
- `KEY_FIRST_TIME_ADD`
- `KEY_FIRST_TIME_LIST`
- `KEY_EXPORT_DATE`
- `KEY_IMPORT_DATE`

Risk:

- This works, but for modernization it is worth migrating to DataStore or keeping Preferences encapsulated until the rest stabilizes.

## Current Architecture

### Strengths

- Basic feature/data/domain/presentation separation.
- Repository interfaces in the domain layer.
- Small, testable use cases.
- ViewModels with coroutines.
- Room entities separated from domain models.
- Explicit mappers.
- Tests cover many use cases, data sources, and ViewModels.
- Dagger centralizes dependency creation.
- XML and ViewBinding already reduce synthetic-view risk.

### Structural Problems

- Partial Clean Architecture: important rules live in Fragments.
- Use cases are mostly pass-through, while rules stay in the UI.
- DAOs are too generic; use-case-specific queries happen in memory.
- Screen state is not modeled as `UiState`.
- Nullable `StateFlow` is used as events, mixing state and event semantics.
- `BaseFragment` has too many responsibilities: toolbar, nav, bottom sheets, snackbar, keyboard, date, dialog, FAB, and global access to MainActivity.
- MainActivity and BaseFragment know too much about each other.
- Adapters use `notifyDataSetChanged` and disable recycling.
- Dates use `java.util.Date`/`Calendar` directly.
- Errors are not modeled; the UI receives generic success/failure.

## Tests

There is a good foundation for the age of the project:

- Unit tests for use cases.
- Data source tests.
- ViewModel tests.
- Mapper/model tests.
- Instrumented DAO and Preferences tests.

Gaps:

- The suite could not be run because of the JDK/Gradle incompatibility.
- There are no Room migration tests.
- There is no end-to-end test for transactional backup/import.
- The most important goal-detail rules live in a Fragment and are hard to test without UI.
- There are no screenshot/UI tests for visual regressions.
- There are no tests specific to modern Android behavior: storage, permissions, process restore, lifecycle.

## CI/CD

Existing workflows:

- `.github/workflows/android_ci.yml`
  - PR only.
  - Tests, lint, ktlint, detekt, assembleDebug.
  - Uses `actions/checkout@v2`, `actions/setup-java@v1`, JDK 1.8.
  - Installs an old NDK through `${ANDROID_HOME}/tools/bin/sdkmanager`.
- `.github/workflows/deploy.yml`
  - Release build, signing, artifact upload, Google Play alpha upload, Telegram notification.

Risks:

- Old Actions.
- Old JDK 8 setup.
- `jcenter()` and old dependencies can break resolution.
- NDK is probably unnecessary or obsolete here.
- Deploying from PRs to `master` is risky for a project being revived.

## Recommended Refactor

### Phase 0: Freeze Behavior and Recover Build

Goal: compile/test before changing architecture.

1. Choose a toolchain strategy:
   - Short term: install JDK 8 or 11 to run the project as-is.
   - Better path: update Gradle/AGP/Kotlin in small steps until reaching a JDK 17-compatible combination.
2. Remove `jcenter()` where possible.
3. Update GitHub Actions.
4. Run `testDebugUnitTest`, `lint`, `ktlint`, `detekt`, and `assembleDebug`.
5. Record real failures after the build configures.

### Phase 1: Minimal Modernization Without Product Redesign

Goal: reduce platform risk.

1. Remove `kotlin-android-extensions`.
2. Fix the old Safe Args/plugin setup.
3. Migrate `com.android.support:preference-v14` to AndroidX Preference.
4. Update target/compile SDK gradually.
5. Replace deprecated APIs:
   - `startActivityForResult` -> Activity Result API.
   - Broad storage permissions -> Storage Access Framework.
   - `Environment.getExternalStorageDirectory()` -> URI/document provider.
6. Remove `allowMainThreadQueries()`.
7. Remove `fallbackToDestructiveMigration()` and maintain migrations.

### Phase 2: Protect Data

Goal: avoid losing existing user data.

1. Enable `exportSchema = true`.
2. Add Room migration tests.
3. Add indices and foreign keys:
   - `item.goalId -> goal.goalId`.
   - `historic.goalId -> goal.goalId`.
4. Replace `getAll().filter` with specific queries:
   - ordered goals.
   - items by goal.
   - historics by goal.
5. Make import transactional.
6. Version backup JSON.
7. Validate backup before deleting existing data.

### Phase 3: Move UI Rules to Domain/ViewModel

Goal: make evolution testable.

1. Extract progress rules into pure components:
   - percentage calculation.
   - goal completion.
   - single/divide-and-conquer progress.
   - item done/undone mutation.
   - historic creation.
2. Transform ViewModels to expose `StateFlow<UiState>`.
3. Use `SharedFlow` or channels for snackbar/navigation/one-shot events.
4. Use `repeatOnLifecycle` in Fragments.
5. Reduce `BaseFragment`; move bottom sheets to local screen components.

### Phase 4: UI and Experience

Goal: keep the product familiar, but current.

Two possible routes:

- Conservative route: XML + ViewBinding + updated Material Components.
- Modern route: gradual Compose migration by screen.

Pragmatic recommendation: modernize XML and behavior first; migrate to Compose only after build, data, and tests are reliable.

Improvements:

- DiffUtil/ListAdapter in RecyclerViews.
- Recyclable ViewHolders.
- Explicit states: loading, empty, content, error.
- Accessibility for buttons, labels, content descriptions, and touch sizes.
- PT/EN copy review.
- Remove generic dialogs/bottom sheets from `BaseFragment`.

### Phase 5: Product

Ideas that fit the existing base:

- Real goal archiving and an archived goals screen.
- Modern backup to a user-selected file.
- Versioned JSON export/import.
- Weekly/monthly progress dashboard.
- Reminders/notifications.
- Categories/tags.
- Better per-goal history.
- Undo for swipes.
- Optional future sync, only after local persistence is solid.

## Recommended Work Order

1. Create a revival branch.
2. Make the build run locally and in CI.
3. Update dependencies in small steps, validating each step.
4. Fix storage/backup before raising target SDK too far.
5. Add migration and backup tests.
6. Extract rules from `GoalDetailFragment`.
7. Introduce `UiState`/events in ViewModels.
8. Improve RecyclerViews/adapters.
9. Review UX and only then consider Compose.

## Suggested Technical Decision

I do not recommend a full rewrite now. The base has value: clear product model, many tests, small features, and simple local persistence. The best path is an incremental revival:

- First make it buildable.
- Then protect data.
- Then move rules into testable layers.
- Finally modernize UI.

A rewrite would only make sense if the goal is to turn Improov into a different product, with no commitment to existing data and freedom to discard almost all current behavior.

