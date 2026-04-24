# LEARNING — Revival Notes & Decisions

This document captures the reasoning, trade-offs, and lessons learned while reviving Improov.
It should grow incrementally as the v2 work progresses.

The goal is not to restate code line by line. The goal is to preserve context: why decisions were made, what risks were uncovered, and what the next engineer should understand before changing the project again.

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

The work can ship as a single v2.0.0 pull request, but it should be developed as a sequence of reviewable phases.

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
