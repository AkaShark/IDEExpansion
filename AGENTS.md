# Repository Guidelines

## Project Structure & Module Organization
- `src/main/kotlin` holds plugin sources; actions live under `com/sharker/ideexpansion` and shared helpers under `utils/`.
- `src/main/resources/META-INF/plugin.xml` registers actions, vendor info, and compatibility; update icons in `src/main/resources/icons/`.
- Build config lives in `build.gradle.kts` (Kotlin 2.2.x, Java 21, IntelliJ Platform plugin 2.10.x); Gradle wrapper and settings sit in the root.

## Build, Test, and Development Commands
- `./gradlew build` compiles Kotlin, packages the plugin, and checks for errors.
- `./gradlew runIde` launches the plugin in a sandboxed IDE using the configured IntelliJ build (2025.1.4.1). Use this for manual validation of actions.
- `./gradlew test` runs any added automated tests. Add tests before relying on it; none exist yet.
- `./gradlew clean` clears build outputs when Gradle cache causes odd behavior.

## Coding Style & Naming Conventions
- Kotlin: 4-space indents, favor expression bodies, and prefer null-safety and immutability. Keep packages lowercase (`com.sharker.ideexpansion`) and classes in PascalCase.
- Action IDs remain stable and namespaced (e.g., `com.sharker.ideexpansion.actions.copyFileRelativePath`); action text in Title Case for UI.
- Keep utility helpers small and reusable; place user-facing strings in resources when they are shared.

## Testing Guidelines
- Put new tests in `src/test/kotlin`; mirror package paths. Use IntelliJ Platform test harnesses where plugin behavior depends on IDE APIs.
- Name tests after the behavior under check (e.g., `CopyFilePathActionTest`), and prefer focused cases over broad integration when possible.
- For manual checks, verify the action appears in Main Toolbar, NavBar, and Tools menu, and that copied paths are project-relative.

## Commit & Pull Request Guidelines
- Use short, imperative commit messages (e.g., `Add relative path copy action`). There is no established history, so stay consistent across new commits.
- PRs should describe the change, note affected menus/actions, include before/after screenshots for UI-visible tweaks, and list manual/automated test steps with command outputs.

## Compatibility & Release Notes
- The plugin targets IntelliJ since build `251` (2025.1). Update `intellijPlatform { create(...) }` and `plugin.xml` together when raising compatibility.
- Keep `changeNotes` concise and incremental; summarize user-visible changes and mention any new shortcuts or toolbar placements.

## 沟通与修改原则
- 所有后续沟通请使用中文回复。
- 不直接修改代码，需要代码改动的位置，详细的告诉我改动的位置，怎么改动，具体的代码是什么，以及改动的原因，除非我明确说明你来改动其他的都由我来改动

