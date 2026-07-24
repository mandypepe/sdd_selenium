# Tasks: People Directory Initial Load Verification

**Input**: Design documents from `/specs/001-people-directory-load/`

**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

## Phase 1: Setup (Shared Infrastructure)

- [x] T001 Define package components and files mapping in POM/project view
- [x] T002 Configure TestNG suite in testng.xml to include the new test class `com.project.tests.DirectoryTests`

---

## Phase 2: Foundational (Blocking Prerequisites)

**⚠️ CRITICAL**: Foundational elements must be implemented first to enable the test class and page objects to build.

- [x] T003 Create `BasePage.java` abstract class at `src/main/java/com/project/pages/BasePage.java` encapsulating driver reference, clicks, typing, and fluent wait synchronization
- [x] T004 Verify `BasePage` integrates correctly with ThreadLocal-managed driver via `DriverManager.getDriver()`

**Checkpoint**: Base structures resolved.

---

## Phase 3: User Story 1 - Initial Page Load and Directory Display (Priority: P1) 🎯 MVP

**Goal**: Verify the directory page loads successfully, showing the title, section header, and populated personnel list.

**Independent Test**: Execute `mvn "-Dtest=com.project.tests.DirectoryTests" test` to verify all directory test methods pass.

### Tests for User Story 1

- [x] T005 [P] [US1] Create test suite stub containing test methods `test_navigateToDirectoryWithoutErrors`, `test_mainTitleAndHeaderVisible`, `test_initialPersonListPopulated`, `test_specialCharactersEncodedCorrectly`, `test_navigationAndLayoutNotBroken` at `src/test/java/com/project/tests/DirectoryTests.java`
- [x] T006 [P] [US1] Implement `DirectoryTestData` class at `src/test/java/com/project/data/DirectoryTestData.java` defining accented names and target URLs

### Implementation for User Story 1

- [x] T007 [US1] Implement `DirectoryPage` class at `src/main/java/com/project/pages/DirectoryPage.java` inheriting from `BasePage` and defining private title and section locator selectors
- [x] T008 [US1] Implement `PersonList` component class at `src/main/java/com/project/pages/components/PersonList.java` containing selectors for views-row, name, and role elements
- [x] T009 [US1] Integrate `PersonList` as a member of `DirectoryPage` and expose retrieval methods for person names and count
- [x] T010 [US1] Code assertions inside `src/test/java/com/project/tests/DirectoryTests.java` using the `DirectoryPage` API to assert title, section, record counts, and accents
- [x] T011 [US1] Add `ReportLogger` logs for each operation in `DirectoryTests.java`

**Checkpoint**: Tests compile, execute, and verify all US-1 criteria successfully.

---

## Phase 4: Polish & Cross-Cutting Concerns

- [x] T012 Implement screenshot capture on failure in TestNG listener at `src/test/java/com/project/listeners/TestListener.java` and attach to Allure reports
- [x] T013 Verify that maven build succeeds and tests are executable via terminal command `mvn "-Dtest=com.project.tests.DirectoryTests" test`
- [x] T014 Run quickstart.md validation to ensure documentation matches code behavior

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup (Phase 1)**: Can start immediately.
- **Foundational (Phase 2)**: Depends on Setup completion; blocks User Story 1.
- **User Story 1 (Phase 3)**: Depends on Phase 2; must write tests first (T005, T006) and make sure they fail/stub before executing implementation (T007-T011).
- **Polish (Phase 4)**: Depends on all implementation tasks.
