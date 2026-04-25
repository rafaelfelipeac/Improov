# Improov Specification

Spec date: 2026-04-24

This specification documents the important behavior, data, and constraints of Improov as it exists today. It should serve as the source of truth for reviving the project, refactoring safely, and deciding what to preserve, fix, or redesign.

## Product Goal

Improov is a local-first Android app that helps users create goals, break targets into steps, and track progress until completion.

The app should allow users to:

- Create measurable goals.
- Update progress through a list, counter, or final value.
- Visualize accumulated progress.
- Record a history of value changes.
- Mark goals and items as completed/uncompleted.
- Keep data locally on the device.
- Export/import a manual backup.
- Use the app in English or Brazilian Portuguese.

## Platforms and Current Scope

- Current platform: native Android.
- Current UI: XML Views + ViewBinding.
- Current persistence: Room + SharedPreferences.
- Operating mode: offline/local-first.
- Current languages: `en` and `pt-BR`.
- Screen orientation: portrait.
- Release app id: `com.rafaelfelipeac.improov`.
- Debug app id: `com.rafaelfelipeac.improov.debug`.

Out of current scope:

- Remote user account.
- Cloud sync.
- Multi-device support.
- Web or iOS.
- Notifications/reminders.

## Mandatory Technical State for Revival

The app is not currently buildable in the current environment with JDK 17 because of the old wrapper/toolchain.

Registered current toolchain:

- Gradle wrapper: `6.1.1`.
- Android Gradle Plugin: `4.0.1`.
- Kotlin Gradle plugin: `1.3.72`.
- Kotlin stdlib: `1.4.10`.
- compileSdk/targetSdk: `29`.
- minSdk: `21`.
- Room: `2.2.5`.
- Navigation: `2.3.0`.
- Dagger: `2.27`.
- Material: `1.3.0-alpha03`.

Revival requirement:

- Before any behavioral refactor, the project must compile and run tests with a declared toolchain.
- Modernization changes should be small and verifiable.
- The first technical goal is a reliable `assembleDebug` and `testDebugUnitTest`.
- Refactor work should be split into small, reviewable sessions with a clear stop point.
- When a session reaches a coherent boundary, it should end in a commit before the next session starts.

## Personas and Use Cases

### Primary User

A person who wants to record personal goals, track progress, and avoid leaving goals only in memory.

### Main Use Cases

- See onboarding on first launch.
- Go directly to the list after onboarding.
- Create a goal.
- Edit a goal.
- View the goal list.
- Reorder goals.
- Open a goal.
- Update progress.
- Create, edit, reorder, and complete list-goal items.
- View progress history for numeric goals.
- Configure language.
- Edit name.
- Export backup.
- Import backup.
- Generate and clear sample data in debug.

## Navigation

Start destination:

- `navigationSplash`.

Main flows:

- Splash -> Welcome when `welcome == false`.
- Splash -> List when `welcome == true`.
- Welcome -> List after completing onboarding.
- List -> Goal Detail when tapping a goal.
- List -> Goal Form when tapping the FAB.
- Goal Detail -> Goal Form when editing.
- Goal Form -> Goal Detail after saving.
- Profile -> Goal Form when tapping the FAB.
- Profile -> Profile Edit.
- Profile -> Settings.
- Profile -> Backup.
- Profile -> Welcome when reopening initial messages.
- Settings -> Settings Language.

Bottom navigation:

- Goals/List.
- Profile.

Current back behavior:

- On list or welcome, back exits the app.
- On goal detail, back navigates to list.
- If the tips bottom sheet is open, back closes the tip.
- On other screens, default NavController/Activity behavior is used.

## Onboarding and First Run

Preference:

- `KEY_WELCOME`, default `false`.

Rules:

- If `welcome == false`, the user should see onboarding.
- After completing onboarding, save `welcome = true`.
- From profile, the user can reopen onboarding; this saves:
  - `welcome = false`
  - `firstTimeAdd = true`
  - `firstTimeList = false`

First-time tips:

- `firstTimeAdd`, default `true`, controls the tip after creating the first goal.
- `firstTimeList`, default `false`, controls the swipe/list tip after the first creation.

## Domain Model

### Goal

Represents a goal.

Fields:

- `goalId: Long`
- `name: String`
- `value: Float`
- `type: GoalType`
- `done: Boolean`
- `divideAndConquer: Boolean`
- `singleValue: Float`
- `bronzeValue: Float`
- `silverValue: Float`
- `goldValue: Float`
- `order: Int`
- `archived: Boolean`
- `incrementValue: Float`
- `decrementValue: Float`
- `createdDate: Date?`
- `updatedDate: Date?`
- `doneDate: Date?`
- `undoneDate: Date?`
- `archiveDate: Date?`
- `date: Date?`

Types:

- `GOAL_LIST`: progress through completed items.
- `GOAL_COUNTER`: progress through fixed increment/decrement values.
- `GOAL_FINAL`: progress through manually entered variable values.
- `GOAL_NONE`: initial state with no selected type.

Rules:

- `name` is required.
- `type` is required before saving.
- If `divideAndConquer == false`, `singleValue` is required and must be greater than zero.
- If `divideAndConquer == true`, `bronzeValue`, `silverValue`, and `goldValue` are required and must satisfy `gold > silver > bronze`.
- If `type == GOAL_COUNTER`, `incrementValue` and `decrementValue` are required and must be greater than zero.
- On create, `createdDate` receives the current date, `value = 0`, `done = false`, and `order` is calculated from the number of loaded goals.
- On edit, `updatedDate` receives the current date.
- After creation, the type should not be changed by the current UI.
- A goal is complete when `value >= singleValue`, or when `divideAndConquer == true` and `value >= goldValue`.

Current behavior to preserve until an explicit decision:

- The list does not filter `archived`.
- `archiveGoal` exists, but is not functional.
- `doneDate` exists in the model, but list completion currently writes `undoneDate` in some paths as well.

### Item

Represents an item inside a `GOAL_LIST` goal.

Fields:

- `itemId: Long`
- `goalId: Long`
- `name: String`
- `order: Int`
- `done: Boolean`
- `createdDate: Date?`
- `updatedDate: Date?`
- `doneDate: Date?`
- `undoneDate: Date?`
- `deleteDate: Date?`
- `date: Date?`

Rules:

- `goalId` should point to the owning goal.
- `name` is required.
- On create, `done = false`, `createdDate = now`, `order = itemsSize + 1`.
- On edit, `updatedDate = now`.
- On completion, `done = true`, `doneDate = now`.
- On uncompletion, `done = false`, `undoneDate = now`.
- Drag/drop swaps `order` between two items and persists both.

Current behavior:

- There is no clearly exposed real delete flow.
- `deleteDate` exists in the model, but there is no consistent soft delete.

### Historic

Represents a numeric progress entry.

Fields:

- `historicId: Long`
- `goalId: Long`
- `value: Float`
- `date: Date?`

Rules:

- It should be created for changes in `GOAL_COUNTER` and `GOAL_FINAL` goals.
- Increment creates positive history.
- Decrement creates negative history.
- Manual value creates history with the typed value.
- History is displayed in reverse order from the loaded list.

## Persistence

### Room Database

Name:

- `improov-db`.

Current version:

- `49`.

Tables:

- `goal`
- `item`
- `historic`

Entities:

- `GoalDataModel`.
- `ItemDataModel`.
- `HistoricDataModel`.

Converters:

- `Date` through `Converters`.

Current configuration:

- `allowMainThreadQueries()`.
- `fallbackToDestructiveMigration()`.
- `MIGRATION_48_49`, without SQL commands.
- `exportSchema = false`.

Desired future spec:

- Do not allow main-thread queries.
- Do not use destructive migration.
- Export schema.
- Add indices by `goalId`.
- Add foreign keys from `item` and `historic` to `goal`.
- Add use-case-specific queries.
- Make import/clear operations transactional.

### Current DAOs

Each DAO has:

- `getAll()`.
- `get(id)`.
- `save(entity)`.
- `delete(entity)`.

Limitation:

- Filtering by goal, sorting, and history ordering are done in memory in the domain layer.

### SharedPreferences

File:

- `com.rafaelfelipeac.improov.preferences`.

Keys:

- `KEY_WELCOME: Boolean`, default `false`.
- `KEY_NAME: String`, default `""`.
- `KEY_LANGUAGE: String`, default `"en"`.
- `KEY_FIRST_TIME_ADD: Boolean`, default `true`.
- `KEY_FIRST_TIME_LIST: Boolean`, default `false`.
- `KEY_EXPORT_DATE: Long`, default `0L`.
- `KEY_IMPORT_DATE: Long`, default `0L`.

## Backup

Current format:

```json
{
  "language": "en",
  "welcome": true,
  "name": "User",
  "firstTimeList": false,
  "firstTimeAdd": true,
  "goals": [],
  "items": [],
  "historics": []
}
```

Current export rules:

- Read preferences.
- Read all Room tables.
- Serialize with Gson.
- Save `exportDate = now`.
- Return JSON as a string.

Current import rules:

- Deserialize JSON with Gson.
- Delete all goals, items, and historics.
- Insert goals, items, and historics from the backup if present.
- Restore preferences.
- Save `importDate = now`.
- Return `true` on success and `false` on parse error.

Current file:

- Path: `/Improov/Backup/Backup.txt` in external storage.
- MIME type: `text/plain`.

Desired future spec:

- Use Storage Access Framework.
- Use Activity Result API.
- Version the payload.
- Validate backup before deleting data.
- Import transactionally.
- Preserve existing data if import fails.
- Handle errors with specific messages.

## UI and Screens

### Splash

Responsibility:

- Decide the initial route.

Expected state:

- No user interaction.

### Welcome

Responsibility:

- Show initial messages.
- Let the user start using the app.

Content:

- Three main messages.
- Start button.

On completion:

- Save `welcome = true`.
- Navigate to list.

### Goal List

Responsibility:

- Show goals.
- Show empty state when there are no goals.
- Navigate to detail.
- Create a goal through the FAB.
- Reorder through drag/drop.
- Complete/uncomplete through swipe.

States:

- Loading.
- Empty.
- Content.

Rules:

- Sort goals by `order`.
- On drag, swap `order` between source and destination.
- On swipe right:
  - If the goal is already complete or percentage >= 100, toggle done immediately.
  - Otherwise, ask for confirmation through a bottom sheet.
- On swipe left:
  - Reload item; it does not currently archive.

### Goal Form

Responsibility:

- Create or edit a goal.

Fields:

- Name.
- Type: List, Counter, Final value.
- Divide and Conquer toggle.
- Single value when not using divide-and-conquer.
- Bronze, silver, and gold when using divide-and-conquer.
- Increment/decrement when type is Counter.

Validations:

- Name is required.
- Type is required.
- Single value is required when not using divide-and-conquer.
- Bronze/silver/gold are required and ordered when using divide-and-conquer.
- Increment/decrement are required for Counter.
- Type cannot be changed after saving.

When saving a new goal:

- Create `Goal`.
- Persist it.
- If `firstTimeAdd == true`, save `firstTimeAdd = false` and `firstTimeList = true`.
- Navigate to the saved goal detail.

When editing:

- Update editable fields.
- Persist.
- Navigate to detail.

### Goal Detail

Responsibility:

- Show the selected goal.
- Show progress.
- Update progress.
- Show and manage items for list goals.
- Show history for numeric goals.

For `GOAL_LIST`:

- Show item list.
- Allow item creation through the menu.
- Allow item editing through a bottom sheet.
- Allow completion/uncompletion through swipe.
- Allow reorder through drag/drop.
- Update `goal.value` with the count of completed items.

For `GOAL_COUNTER`:

- Show current total.
- Show increment and decrement buttons.
- Each click changes `goal.value`.
- Each click creates `Historic`.
- Show history.

For `GOAL_FINAL`:

- Show value input.
- Save typed value as an increment in the total.
- Create `Historic`.
- Show history.

Visual progress:

- Without divide-and-conquer: one arc with `singleValue`.
- With divide-and-conquer: bronze, silver, and gold arcs.
- When each milestone is reached, swap the dark icon for a colored icon.

### Profile

Responsibility:

- Show/edit name.
- Open backup.
- Open settings.
- Reopen onboarding.
- In debug, generate/clear data.

Rules:

- If name is empty, show the register-name action.
- If name is filled, show the name and edit action.
- `Generate data` and `Clear data` appear only in `BuildConfig.DEBUG`.

### Profile Edit

Responsibility:

- Edit the user name.

Rules:

- Name cannot be empty.
- On save, persist it in SharedPreferences.

### Settings

Responsibility:

- Open language configuration.
- Open Play Store for rating.
- Open email for feedback.
- Show version.

### Settings Language

Responsibility:

- Select language.

Languages:

- English: `en`.
- Portuguese: `pt_br`, applied as locale `pt-BR`.

### Backup

Responsibility:

- Show backup description.
- Show last export/import date.
- Export data.
- Import data.
- Share exported file.

Current permissions:

- `READ_EXTERNAL_STORAGE`.
- `WRITE_EXTERNAL_STORAGE`.

Current behavior:

- If permission is missing, request permission.
- If permission is denied, suggest opening app settings.
- Export saves a local file and offers an action to share/save.
- Import uses a file picker with `ACTION_GET_CONTENT`.

## Current Architecture

Layers:

- Presentation: Fragments, Adapters, ViewModels.
- Domain: models, repository interfaces, use cases.
- Data: data sources, DAOs, mappers, preferences.
- Core: DI, base classes, extensions, persistence, locale.

DI:

- Dagger 2.
- Singleton `AppComponent`.
- Feature modules.
- ViewModels exposed through a custom `ViewModelProvider`.

Current ViewModel pattern:

- Injects use cases.
- Exposes `Flow<T>` based on `MutableStateFlow<T?>`.
- Uses `filterNotNull()` to hide the initial state.
- Uses `viewModelScope.launch` for suspend calls.

Refactor constraint:

- Do not change product behavior together with architecture migration unless a test covers the rule.
- Move rules into pure classes before changing UI.

## Quality, Tests, and CI

Existing tests:

- Unit tests for use cases.
- Unit tests for data sources.
- Unit tests for ViewModels.
- Unit tests for mappers/models.
- Instrumented DAO tests.
- Instrumented Preferences tests.

Desired commands once the toolchain is recovered:

```bash
./gradlew testDebugUnitTest
./gradlew lint
./gradlew ktlint
./gradlew detekt
./gradlew assembleDebug
```

Definition of done for revival phase 0:

- Local build compiles.
- Unit tests run.
- Updated CI runs at least unit tests and debug assemble.
- Remaining failures are documented.

Definition of done for business-rule refactor:

- Extracted rules have unit tests.
- ViewModels expose predictable state.
- One-shot events are not incorrectly re-emitted after screen recreation.

## Known Risks That Should Become Backlog

- Legacy build incompatible with JDK 17.
- Obsolete `jcenter()`.
- Obsolete `kotlin-android-extensions`.
- Old AGP/Kotlin/Gradle.
- Legacy storage and broad permissions.
- Old `startActivityForResult` and permission APIs.
- Room with `allowMainThreadQueries()`.
- Room with `fallbackToDestructiveMigration()`.
- No exported Room schema.
- No foreign keys/indices.
- Non-transactional backup import.
- Domain rules in Fragments.
- Nullable `StateFlow` used as events.
- Flow collection without `repeatOnLifecycle`.
- RecyclerView without DiffUtil and with disabled recycling.
- `BaseFragment` has too many responsibilities.
- Errors represented as booleans/empty strings/`printStackTrace`.
- Dates spread as `java.util.Date`/`Calendar`.
- Incomplete archiving.
- Possible division by zero in percentage calculation.
- Inconsistent/incomplete `doneDate`, `undoneDate`, `archiveDate`, and `deleteDate` fields.

## Modernization Backlog by Priority

### P0: Recover Executability

- Define target JDK/Gradle/AGP/Kotlin.
- Make Gradle configure.
- Remove dependency on JDK 8 in CI or document it temporarily.
- Run tests and assemble.

### P1: Reduce Platform Risk

- Update Actions.
- Remove `jcenter()`.
- Migrate AndroidX Preference.
- Remove `kotlin-android-extensions`.
- Fix Safe Args/plugin.
- Update target SDK in small steps.

### P2: Protect Data

- Enable Room schema export.
- Add migration tests.
- Remove destructive migration.
- Remove main-thread queries.
- Create specific queries.
- Version backup.
- Import backup transactionally.

### P3: Make Rules Testable

- Extract progress calculation.
- Extract goal validation.
- Extract done/undone mutation.
- Extract historic creation.
- Create `UiState` per screen.
- Separate one-shot events from state.

### P4: Modernize UI

- Use ListAdapter/DiffUtil.
- Review lifecycle collection.
- Reduce BaseFragment.
- Improve accessibility.
- Review strings.
- Evaluate Compose after behavior is protected.

## Pending Product Decisions

- Should archiving exist? If yes, should swipe left archive?
- Should completed goals remain editable?
- Should history also record item completion?
- Should backup replace everything or offer merge?
- Should debug data remain in the app or move to fixtures/tests?
- Should DecoView be kept or replaced?
- Should Dagger 2 remain, or should the app migrate to Hilt/Koin/manual DI?
- Should XML remain, or should the app migrate gradually to Compose?

## Golden Rule for Revival

Infrastructure changes can come first, but behavior changes must be intentional, tested, and documented. The initial goal is not to reimagine Improov; it is to recover a reliable base so it can evolve.
