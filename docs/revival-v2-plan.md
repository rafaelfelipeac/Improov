# Improov v2 Revival Plan

Plan date: 2026-04-24

## Intent

Improov v2 is planned as a full legacy revival: bring the app back to life, modernize the Android stack, replace the XML-based UI with a modern Compose implementation, preserve the important product behavior, and create a foundation that can receive new features again.

The work can ship as one v2.0.0 pull request, but it should not be executed as one unstructured change. The branch should be developed in clear phases with commits that leave a reviewable trail.
Those phases should be broken into smaller sessions so each step stays focused, easy to validate, and easy to stop at a clean boundary.

## Reference Project

Hermes is the reference project for the desired final direction.

Reference path inspected:

- `/Users/rafaelcordeiro/AndroidStudioProjects/hermes`

Hermes should inform Improov v2 in these areas:

- Modern Android stack.
- Compose + Material 3 UI.
- Room as source of truth for app entities.
- DataStore Preferences for settings.
- Hilt for dependency injection.
- Coroutines, Flow, and `StateFlow` for reactive state.
- Clear UI -> ViewModel -> Repository boundaries.
- Schema-driven backup compatibility.
- Local-first product decisions.
- Calm, practical UX.
- Project-level instructions for AI agents.
- Living learning documentation.
- Small project-specific review/check skills.
- Strong localization discipline.
- Tests with fakes where possible.
- CI that keeps build, lint, and release surfaces honest.

This does not mean Improov should copy Hermes feature-for-feature. Hermes is the quality bar and implementation style reference, not the product model.

## Product Direction

Improov should remain a local-first goals app.

The v2 goal is not to turn it into Hermes. The v2 goal is to bring Improov to the same level of technical maturity and product clarity:

- Offline-first.
- No account required.
- No server dependency.
- Clear goal tracking.
- Calm and useful progress feedback.
- Modern Android UI.
- Safe persistence.
- Backup that users can trust.
- Documentation that explains decisions, not only APIs.

## PR Strategy

The v2.0.0 release can be delivered as one pull request, but the implementation should be split into phases and commits:

1. Toolchain revival.
2. Modern project baseline.
3. Persistence and backup foundation.
4. Domain model/rules extraction.
5. Compose application shell.
6. Compose feature screens.
7. Data migration and compatibility tests.
8. Cleanup of legacy XML/Fragments/adapters.
9. Release prep.

## Session Anchors

Use these exact anchor ids when starting the next session. The anchor name should be the first thing mentioned in the handoff prompt.

- `phase-0-recover-build`
- `phase-1-establish-modern-baseline`
- `phase-2-backup-envelope-v1`
- `phase-2-backup-v2-model`
- `phase-2-import-compatibility`
- `phase-3-domain-rules`
- `phase-4-compose-screens`
- `phase-5-cleanup-hardening`

For the backup work specifically, the current small-step sequence is:

- `backup-envelope-v1`
- `backup-v2-inner-model`
- `backup-import-compatibility`
- `backup-settings-datastore`

When a session ends, note the next anchor explicitly in the summary so the next prompt can be as short as: `continue from backup-v2-inner-model`.

Each phase should have:

- A stated goal.
- A short completion note.
- Tests or a documented reason when tests cannot run yet.
- No unrelated cleanup.
- A small enough scope to finish in one focused session when possible.
- A commit at the end of the session when the diff is coherent.

## Proposed v2 Stack

Target direction, inspired by Hermes:

- Kotlin modern version.
- Android Gradle Plugin modern version.
- Gradle version compatible with the selected AGP/JDK.
- Version catalog via `gradle/libs.versions.toml`.
- Compose + Material 3.
- Navigation Compose.
- Room + KTX.
- DataStore Preferences.
- Hilt.
- Kotlin serialization for backup JSON if practical.
- Coroutines + Flow + StateFlow.
- Detekt + ktlint.
- GitHub Actions for build/lint/tests.

The exact versions should be decided during phase 0/1 based on what compiles reliably and what is appropriate for the current Android ecosystem.

## Architectural Target

Improov v2 should follow the same broad boundaries as Hermes:

- UI only renders state and sends user intents.
- ViewModels orchestrate use cases/repositories and expose `StateFlow`.
- Repositories own persistence operations and transactions.
- Room is the source of truth for durable app data.
- DataStore stores settings only.
- Domain rules should be testable without Android UI.
- Backup compatibility should be schema-driven.

Avoid:

- UI directly accessing Room/DataStore.
- Business rules inside composables.
- Nullable `StateFlow` as one-shot events.
- Large base UI classes that own unrelated behavior.
- Hardcoded UI strings.
- Hardcoded dimensions/tokens scattered through composables.

## UI Target

All XML UI should be removed for v2.

Target:

- Compose-only screens.
- Material 3.
- Theme tokens and dimensions centralized.
- Light and dark theme support.
- Screen state modeled explicitly: loading, empty, content, error.
- Reusable components only when they remove real duplication.
- Accessibility and content descriptions treated as part of the implementation.
- Localized strings for all user-facing text.

Legacy UI to remove:

- Fragments as UI surface.
- XML layouts.
- RecyclerView adapters.
- `BaseFragment`.
- ViewBinding.
- Custom toolbar/bottom navigation wiring tied to `MainActivity`.
- Deprecated Activity/permission APIs.

## Persistence and Backup Target

Improov v2 should treat data preservation as a first-class constraint.

Required direction:

- Room schema export enabled.
- Migration tests.
- No `fallbackToDestructiveMigration()`.
- No `allowMainThreadQueries()`.
- Foreign keys/indices where appropriate.
- Data operations that replace/import/clear should be transactional.
- Backup JSON should include explicit `schemaVersion`.
- Import compatibility should be decided by `schemaVersion`, not app version.
- Old backup support should be either implemented or explicitly declared unsupported with a friendly failure path.

Hermes reference:

- `docs/backup-compatibility-policy.md`
- Versioned backup decoders.
- Unknown future schemas fail fast with a friendly error.
- Replace-mode import remains transactional in the repository layer.

Improov should get its own backup compatibility policy before backup v2 is implemented.

## Documentation Target

Improov v2 should adopt documentation practices inspired by Hermes:

- `docs/spec.md` remains the behavior source of truth.
- `docs/legacy-revival-audit.md` remains the legacy-state snapshot.
- `docs/revival-v2-plan.md` tracks the modernization plan.
- Add `LEARNING.md` as a living record of decisions and lessons.
- Add project-level AI instructions once conventions stabilize.
- Add ADRs for large decisions where helpful.

Candidate ADRs:

- Compose-only v2.
- Hilt vs manual DI.
- DataStore migration.
- Room schema and backup compatibility.
- Backup v1/v2 import policy.
- Navigation Compose structure.
- Minimum supported Android version.

## AI-Assisted Development Target

Hermes uses project instructions and repo-local skills to keep AI-assisted changes aligned with project conventions. Improov should eventually do the same.

Target practices:

- Project-level agent guidance for Kotlin/Compose style.
- Guardrails for localization.
- Guardrails for persistence and backup compatibility.
- Guardrails for tests and fake-based testing.
- Living `LEARNING.md` updates when meaningful decisions are made.

Improov should not blindly copy Hermes skills. It should start with a smaller set based on its own needs:

- Compose/UI guardrails.
- Localization consistency check.
- Backup compatibility check.
- Test gap check.
- Broad PR review checklist.

## Phase Plan

### Phase 0: Recover Build

Goal:

- Make the legacy project configure, build, and run tests.

Work:

- Choose temporary or target JDK.
- Update Gradle/AGP/Kotlin enough to configure.
- Remove or replace repositories that block dependency resolution.
- Run unit tests and assemble debug.

Done when:

- `assembleDebug` works.
- Unit tests run or failures are documented.
- The branch has a known baseline.

### Phase 1: Establish Modern Baseline

Goal:

- Create the v2 project foundation.

Work:

- Move build scripts toward Kotlin DSL if chosen.
- Add version catalog.
- Add Compose/Material 3.
- Add Hilt.
- Add DataStore.
- Update CI.
- Keep the app compiling.

Done when:

- A minimal Compose app shell can run.
- Build/lint/test commands are defined.
- Legacy screens may still exist temporarily, but the v2 foundation is in place.

### Phase 2: Data and Compatibility

Goal:

- Make persistence safe before UI replacement accelerates.

Work:

- Update Room.
- Export schema.
- Define migrations.
- Add migration tests.
- Define backup compatibility policy.
- Decide legacy backup import policy.
- Move settings toward DataStore.

Done when:

- Data preservation rules are explicit.
- Import/export compatibility has tests or a documented staged plan.

Current policy document:

- `docs/persistence-and-backup-policy.md`

### Phase 3: Domain Rules

Goal:

- Extract important goal behavior from UI.

Work:

- Goal validation.
- Percentage calculation.
- Goal completion.
- List item completion.
- Counter/final-value history creation.
- Order/reorder behavior.

Done when:

- Rules have unit tests.
- ViewModels can orchestrate behavior without UI logic.

### Phase 4: Compose Screens

Goal:

- Replace XML/Fragment UI with Compose.

Suggested order:

1. App shell/navigation.
2. Goal list.
3. Goal form.
4. Goal detail.
5. Profile.
6. Settings/language.
7. Backup.
8. Welcome/splash.

Done when:

- User flows from the spec exist in Compose.
- Old XML layouts and adapters are no longer needed.
- UI state is explicit.

### Phase 5: Cleanup and Hardening

Goal:

- Remove legacy code and prepare v2.0.0.

Work:

- Delete XML layouts, Fragments, adapters, old base classes, and obsolete extensions.
- Remove deprecated dependencies.
- Run tests/lint/detekt/ktlint.
- Update README and screenshots if applicable.
- Update changelog/version to `2.0.0`.

Done when:

- The app builds as v2.
- Legacy UI stack is gone.
- Documentation and release notes match the shipped behavior.

## Decision Principles

- Prefer preserving user data over simplifying implementation.
- Prefer testable domain rules over UI-driven behavior.
- Prefer a clear migration path over a hidden rewrite.
- Prefer documenting trade-offs over silently choosing.
- Prefer incremental commits inside the long branch.
- Prefer small, bounded sessions over long context-heavy runs.
- Prefer Hermes conventions when they fit Improov, but do not copy them when the product needs differ.

## Open Decisions

- Exact target AGP/Kotlin/Gradle/JDK versions.
- Minimum SDK for v2.
- Hilt vs manual DI.
- Kotlin serialization vs Gson for backup v2.
- DataStore migration strategy.
- Whether old JSON backups must remain importable.
- Whether existing Room data must migrate in place or can go through backup/import.
- Whether `archived` becomes a real product feature in v2.0.0.
- Whether v2.0.0 should include new product features or only revival/modernization.

## Working Rule

The final PR can be one v2.0.0 PR, but the work should behave like a sequence of small projects and smaller sessions. Every major step should teach something, leave documentation behind, and keep the next step easier than the last. When a session is complete, commit it and start a new session for the next boundary.
