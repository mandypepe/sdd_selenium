# Data Model: Directory People Navigation

**Feature**: TC-003 Directory People Navigation Verification  
**Created**: 2026-07-25  
**Specification**: specs/003-directory-people-navigation/spec.md

---

## Entity Definitions

### PersonnelRecord

Represents a single personnel entry displayed in the Directory People section.

#### Core Attributes

| Attribute | Type | Constraints | Description |
|-----------|------|-------------|-------------|
| **id** | String | Unique, non-null | Unique identifier (UUID or numeric ID from backend) |
| **fullName** | String | Non-null, max 256 chars | Full name of personnel; must not be truncated in UI |
| **roleTitle** | String | Non-null, max 256 chars | Organizational role or academic title (e.g., "Director", "Associate Professor") |
| **email** | String | Optional, email format | Email address (if displayed; may vary by institution) |
| **department** | String | Optional, max 256 chars | Department or organizational unit (if displayed) |
| **officeLocation** | String | Optional, max 256 chars | Physical office location (if displayed) |

#### Validation Rules

1. **Name Validation**: `fullName` must contain at least 2 characters; no truncation in UI display.
2. **Role Validation**: `roleTitle` must be non-empty and properly formatted (no truncation, appropriate length).
3. **Data Completeness**: At minimum, `id`, `fullName`, and `roleTitle` must be present; other fields optional.
4. **Special Characters**: Names and roles may contain accented characters, punctuation, or institution-specific symbols (e.g., "José García", "Dr. Smith, Ph.D."); UI must render without distortion.

#### Edge Cases

- **Long Names**: Names > 50 characters must wrap or ellipsis without breaking layout.
- **Long Titles**: Role titles > 50 characters must wrap cleanly.
- **Special Characters**: Accented characters (Spanish: ñ, á, é, í, ó, ú), punctuation, and symbols render correctly.
- **Missing Optional Fields**: Records missing email/department/office must still display correctly.
- **Empty Results**: If personnel data is empty, UI displays appropriate "No records" message (not a hard error).

---

### PaginationMetadata

Metadata about the paginated personnel list.

| Attribute | Type | Constraints | Description |
|-----------|------|-------------|-------------|
| **currentPage** | Integer | ≥ 1 | Current active page number (1-indexed) |
| **pageSize** | Integer | > 0, typically 10–50 | Number of records per page (default: 20) |
| **totalRecords** | Integer | ≥ 0 | Total number of records in the complete list |
| **totalPages** | Integer | ≥ 1 | Total number of pages (computed: ceil(totalRecords / pageSize)) |
| **hasNextPage** | Boolean | N/A | True if currentPage < totalPages |
| **hasPreviousPage** | Boolean | N/A | True if currentPage > 1 |

#### Validation Rules

1. **Page Navigation**: `currentPage` must be between 1 and `totalPages` (inclusive).
2. **Page Size Consistency**: All pages (except possibly the last) must contain exactly `pageSize` records.
3. **Last Page**: May contain fewer records (< pageSize).
4. **Navigation State**:
   - Previous button/link disabled when `hasPreviousPage` = false.
   - Next button/link disabled when `hasNextPage` = false.

---

### DirectoryNavigation

Represents the main directory navigation structure.

| Attribute | Type | Constraints | Description |
|-----------|------|-------------|-------------|
| **navigationOptions** | List<String> | Non-empty | Available navigation categories (e.g., ["People", "Institutions", "Departments"]) |
| **activeSection** | String | One of navigationOptions | Currently selected section (e.g., "People") |
| **sectionContent** | Object | Type varies | Content specific to active section (e.g., PersonnelRecord list + PaginationMetadata for "People") |

#### Validation Rules

1. **Navigation Visibility**: All available options must be visible and accessible from the directory homepage.
2. **Option Selection**: Clicking an option must route to the corresponding section without errors.
3. **State Consistency**: `activeSection` accurately reflects the currently displayed section.

---

## Test Data Sets

### Dataset 1: Single Page (< 20 records)

**Scenario**: Personnel directory with fewer records than one page size.  
**Records**: 8 personnel records  
**Expected Behavior**:
- Page 1 displays all 8 records.
- Previous button is disabled.
- Next button is disabled.
- No pagination navigation required.

**Sample Data**:
```json
{
  "records": [
    {
      "id": "001",
      "fullName": "Dr. María García López",
      "roleTitle": "Director of Research",
      "department": "Institute of Advanced Studies"
    },
    {
      "id": "002",
      "fullName": "José Martínez",
      "roleTitle": "Senior Software Engineer",
      "department": "Technology Department"
    },
    {
      "id": "003",
      "fullName": "Dr. Elena Rodríguez Pérez",
      "roleTitle": "Associate Professor",
      "department": "Faculty of Engineering"
    },
    {
      "id": "004",
      "fullName": "Antonio Fernández",
      "roleTitle": "Systems Administrator",
      "department": "IT Operations"
    },
    {
      "id": "005",
      "fullName": "Dr. Carmen Díaz Santos",
      "roleTitle": "Dean of Academic Affairs",
      "department": "Academic Leadership"
    },
    {
      "id": "006",
      "fullName": "Miguel Gonzalez",
      "roleTitle": "Junior Developer",
      "department": "Technology Department"
    },
    {
      "id": "007",
      "fullName": "Dr. Lucia Morales",
      "roleTitle": "Professor of Mathematics",
      "department": "Faculty of Sciences"
    },
    {
      "id": "008",
      "fullName": "Rafael Nuñez",
      "roleTitle": "Administrative Coordinator",
      "department": "Administration"
    }
  ],
  "pagination": {
    "currentPage": 1,
    "pageSize": 20,
    "totalRecords": 8,
    "totalPages": 1
  }
}
```

---

### Dataset 2: Multiple Pages (60+ records)

**Scenario**: Personnel directory spanning multiple pages.  
**Records**: 65 personnel records (3+ pages at 20 records/page)  
**Expected Behavior**:
- Page 1: displays records 1–20; Next enabled, Previous disabled.
- Page 2: displays records 21–40; Next enabled, Previous enabled.
- Page 3: displays records 41–60; Next enabled, Previous enabled.
- Page 4 (partial): displays records 61–65 (5 records); Next disabled, Previous enabled.

**Sample Data Structure** (representative; full dataset omitted for brevity):
```json
{
  "records": [
    { "id": "001", "fullName": "Dr. María García López", "roleTitle": "Director of Research" },
    { "id": "002", "fullName": "José Martínez", "roleTitle": "Senior Software Engineer" },
    ...
    { "id": "060", "fullName": "Dr. Ultimo Director", "roleTitle": "Department Chair" },
    { "id": "061", "fullName": "Dr. Roberto Vargas", "roleTitle": "Researcher" },
    { "id": "062", "fullName": "Laura Sánchez", "roleTitle": "Administrative Assistant" },
    { "id": "063", "fullName": "Dr. Felipe Torres", "roleTitle": "Emeritus Professor" },
    { "id": "064", "fullName": "Sofía Mendoza", "roleTitle": "Finance Manager" },
    { "id": "065", "fullName": "Dr. Antonio Ramos", "roleTitle": "Advisor" }
  ],
  "pagination": {
    "currentPage": 1,
    "pageSize": 20,
    "totalRecords": 65,
    "totalPages": 4
  }
}
```

---

### Dataset 3: Exact Page Boundary (exactly 40 records)

**Scenario**: Personnel directory with exactly 2 pages (boundary condition).  
**Records**: 40 personnel records (exactly 2 × 20)  
**Expected Behavior**:
- Page 1: displays records 1–20; Next enabled, Previous disabled.
- Page 2: displays records 21–40 (exactly 20 records); Next disabled, Previous enabled.
- No partial pages; pagination navigation is clean.

**Sample Data Structure**:
```json
{
  "records": [
    { "id": "001", "fullName": "Dr. María García López", "roleTitle": "Director of Research" },
    ...
    { "id": "040", "fullName": "Dr. Last Personnel", "roleTitle": "Final Title" }
  ],
  "pagination": {
    "currentPage": 1,
    "pageSize": 20,
    "totalRecords": 40,
    "totalPages": 2
  }
}
```

---

### Dataset 4: Special Characters & Long Names

**Scenario**: Personnel records with special characters, long names, and title variations.  
**Records**: 10 personnel with diverse naming and title patterns  
**Expected Behavior**:
- All names render without truncation or distortion.
- Special characters (ñ, á, é, etc.) display correctly.
- Long titles wrap cleanly without breaking layout.
- UI remains stable and readable.

**Sample Data**:
```json
{
  "records": [
    {
      "id": "s01",
      "fullName": "Dr. José María García López de la Vega",
      "roleTitle": "Director General de Investigación y Desarrollo Tecnológico"
    },
    {
      "id": "s02",
      "fullName": "M.Sc. Alejandra Rodríguez García",
      "roleTitle": "Jefa de Departamento de Ingeniería de Software"
    },
    {
      "id": "s03",
      "fullName": "Dr. Juan José Pérez Díaz, Ph.D.",
      "roleTitle": "Profesor Titular de Cátedra"
    },
    {
      "id": "s04",
      "fullName": "Lic. María del Carmen Fernández Sánchez",
      "roleTitle": "Coordinadora de Proyectos Especiales"
    },
    {
      "id": "s05",
      "fullName": "Dr. Ángel Manuel González Vélez",
      "roleTitle": "Vicedecano de Asuntos Académicos"
    },
    {
      "id": "s06",
      "fullName": "Ing. Raúl Enrique Martínez Núñez",
      "roleTitle": "Especialista en Infraestructura y Seguridad Informática"
    },
    {
      "id": "s07",
      "fullName": "Dr. Javier Ignacio López García-Rossell",
      "roleTitle": "Presidente del Comité de Ética en Investigación"
    },
    {
      "id": "s08",
      "fullName": "Lic. Sofía María Delgado Ortiz",
      "roleTitle": "Secretaria de Recursos Humanos"
    },
    {
      "id": "s09",
      "fullName": "Dr. Carlos Humberto Ayala Martínez",
      "roleTitle": "Catedrático en Sistemas Computacionales"
    },
    {
      "id": "s10",
      "fullName": "Ing. Patricia Verónica García del Castillo",
      "roleTitle": "Directora de Operaciones Tecnológicas"
    }
  ],
  "pagination": {
    "currentPage": 1,
    "pageSize": 20,
    "totalRecords": 10,
    "totalPages": 1
  }
}
```

---

### Dataset 5: Edge Case – Empty Results

**Scenario**: Directory configured but currently contains no personnel records.  
**Records**: 0  
**Expected Behavior**:
- UI displays "No personnel records available" or similar message.
- Pagination controls are hidden or disabled.
- No errors in browser console.

**Sample Data Structure**:
```json
{
  "records": [],
  "pagination": {
    "currentPage": 0,
    "pageSize": 20,
    "totalRecords": 0,
    "totalPages": 0
  }
}
```

---

## Test Data Usage

### Data Provider Mapping

Each test dataset maps to a test scenario:

| Test Scenario | Dataset | Key Validation |
|---------------|---------|-----------------|
| `test_singlePageDisplay` | Dataset 1 | All records fit on page 1; no pagination needed |
| `test_multiPageNavigation` | Dataset 2 | Next/Previous buttons work correctly across pages |
| `test_pageNavi_gationBoundary` | Dataset 3 | Exactly 2 pages; clean boundary handling |
| `test_specialCharactersRendering` | Dataset 4 | Accented characters, long names render correctly |
| `test_firstPageDisplay` | Dataset 2, Page 1 | Validate first page loads with correct records |
| `test_middlePageDisplay` | Dataset 2, Page 2 | Validate middle page navigation and content |
| `test_lastPageDisplay` | Dataset 2, Page 4 | Validate last page (partial records) loads correctly |

---

## Validation Rules for Test Execution

### Personnel Record Validation

1. **Name Presence**: Every displayed record must have a non-empty `fullName`.
2. **Role Presence**: Every displayed record must have a non-empty `roleTitle`.
3. **No Truncation**: Name and role text must be fully visible (within reasonable wrap limits).
4. **Text Formatting**: No overlapping, distorted, or illegible text.

### Pagination Validation

1. **Page Count Accuracy**: UI page count matches `totalPages` from backend.
2. **Navigation Button State**:
   - Previous button disabled on page 1.
   - Next button disabled on last page.
3. **Record Count per Page**: Each page (except last) contains exactly `pageSize` records.
4. **Last Page Handling**: Last page may contain fewer records; validate count is `totalRecords % pageSize` or `pageSize` if divisible.

### Navigation Validation

1. **"People" Option Visibility**: "People" option is visible in directory navigation.
2. **Section Routing**: Clicking "People" routes to personnel list without errors.
3. **Data Load Time**: Personnel list renders within 3 seconds (SC-002).

---

## State Transitions (if applicable)

The Directory People section does not have complex state transitions; however, pagination follows this flow:

```
[Idle] → [Click "People" Navigation] → [Loading Data] → [Page 1 Rendered]
  ↓                                                           ↓
[Click Previous] ← [Disabled] (on page 1)          [Click Next] → [Page N Rendered]
```

---

## Assumptions

- Backend is stable and provides consistent test data across runs.
- Page size is configurable but defaults to 20 records/page.
- Pagination controls are standard HTML elements (buttons, links, or styled divs with aria-roles).
- Personnel data is pre-populated; no UI-based data creation is needed for tests.

---

**Status**: Data Model Complete ✅  
**Next Phase**: Create contracts/ directory and quickstart.md
