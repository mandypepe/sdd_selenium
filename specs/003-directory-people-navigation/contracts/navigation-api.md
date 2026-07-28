# Contract: Navigation & Personnel API

**Feature**: TC-003 Directory People Navigation Verification  
**Created**: 2026-07-25  
**Type**: Backend/API Contract (Optional; depends on SPA architecture)

---

## Overview

This document defines the contract for the personnel list API endpoint (if applicable). If the application uses server-side rendering (SSR) without a JavaScript API layer, this contract describes the expected HTML structure and data attributes that Page Objects will query.

---

## Personnel List API Endpoint (if SPA/AJAX)

### Endpoint Details

```
GET /api/directory/personnel
```

### Request Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `page` | Integer | No | 1 | Page number (1-indexed) |
| `pageSize` | Integer | No | 20 | Number of records per page |
| `section` | String | No | "people" | Directory section (e.g., "people", "institutions") |

### Response Format

**HTTP 200 OK**

```json
{
  "success": true,
  "data": {
    "records": [
      {
        "id": "001",
        "fullName": "Dr. María García López",
        "roleTitle": "Director of Research",
        "department": "Institute of Advanced Studies",
        "email": "maria.garcia@institution.edu",
        "officeLocation": "Building A, Room 301"
      },
      ...
    ],
    "pagination": {
      "currentPage": 1,
      "pageSize": 20,
      "totalRecords": 65,
      "totalPages": 4,
      "hasNextPage": true,
      "hasPreviousPage": false
    }
  }
}
```

### Response Codes

| Code | Meaning | Body |
|------|---------|------|
| **200** | Success | Personnel records + pagination metadata |
| **400** | Bad request (invalid page, pageSize) | `{ "error": "Invalid page parameter" }` |
| **404** | Section not found | `{ "error": "Section 'people' not found" }` |
| **500** | Server error | `{ "error": "Internal server error" }` |

### Rate Limiting

- No rate limiting specified; assume standard per-IP rate limits apply.

### Caching

- Response may be cached by CDN/browser; assume cache-control headers are set appropriately by backend.

### Error Handling

- If `page` exceeds `totalPages`, return HTTP 400 with error message, or silently return last valid page (implementation dependent; Page Objects must handle gracefully).
- If `pageSize` is invalid (≤ 0 or > 1000), return HTTP 400.

---

## Server-Side Rendered (SSR) HTML Contract

If the application uses traditional SSR, Page Objects will query the DOM using these data attributes:

### HTML Structure Expectations

#### Directory Navigation

```html
<nav id="directory-navigation" class="nav-bar" role="navigation">
  <ul class="nav-list">
    <li>
      <a href="/directory" data-testid="nav-people" class="nav-link">
        People
      </a>
    </li>
    <li>
      <a href="/directory/institutions" data-testid="nav-institutions" class="nav-link">
        Institutions
      </a>
    </li>
    <li>
      <a href="/directory/departments" data-testid="nav-departments" class="nav-link">
        Departments
      </a>
    </li>
  </ul>
</nav>
```

#### Personnel List Container

```html
<section id="people-section" class="directory-section" data-section="people">
  <!-- Loading Indicator (optional; may appear during data fetch) -->
  <div id="loading-indicator" class="spinner" aria-label="Loading personnel records" style="display: none;">
    <span class="spinner-icon"></span>
  </div>

  <!-- Personnel Records -->
  <div class="personnel-list" id="personnel-records" data-testid="personnel-list">
    <div class="personnel-record" data-testid="personnel-record" data-id="001">
      <h3 class="personnel-name" data-testid="personnel-name">Dr. María García López</h3>
      <p class="personnel-role" data-testid="personnel-role">Director of Research</p>
      <p class="personnel-department" data-testid="personnel-department">Institute of Advanced Studies</p>
    </div>

    <div class="personnel-record" data-testid="personnel-record" data-id="002">
      <h3 class="personnel-name">José Martínez</h3>
      <p class="personnel-role">Senior Software Engineer</p>
      <p class="personnel-department">Technology Department</p>
    </div>
    ...
  </div>

  <!-- Pagination Controls -->
  <nav class="pagination" id="pagination" data-testid="pagination-controls" aria-label="Personnel list pagination">
    <button class="pagination-prev" id="prev-page" data-testid="pagination-prev" disabled>
      Previous
    </button>
    
    <div class="pagination-pages">
      <button class="pagination-page active" data-page="1" data-testid="pagination-page-1">1</button>
      <button class="pagination-page" data-page="2" data-testid="pagination-page-2">2</button>
      <button class="pagination-page" data-page="3" data-testid="pagination-page-3">3</button>
      <button class="pagination-page" data-page="4" data-testid="pagination-page-4">4</button>
    </div>
    
    <button class="pagination-next" id="next-page" data-testid="pagination-next">
      Next
    </button>

    <span class="pagination-info" data-testid="pagination-info">
      Page <span id="current-page">1</span> of <span id="total-pages">4</span>
    </span>
  </nav>
</section>
```

### Data Attributes Expected by Page Objects

| Element | Attribute | Expected Value | Purpose |
|---------|-----------|-----------------|---------|
| Navigation link | `data-testid` | `"nav-people"` | Locating "People" navigation option |
| Personnel list container | `data-testid` | `"personnel-list"` | Locating list for data validation |
| Personnel record | `data-testid` | `"personnel-record"` | Identifying individual records |
| Personnel record | `data-id` | Record ID (e.g., "001") | Distinguishing records; used in data validation |
| Personnel name | `data-testid` | `"personnel-name"` | Extracting name for assertion |
| Personnel role | `data-testid` | `"personnel-role"` | Extracting role/title for assertion |
| Pagination prev button | `data-testid` | `"pagination-prev"` | Clicking previous page |
| Pagination next button | `data-testid` | `"pagination-next"` | Clicking next page |
| Pagination page button | `data-testid` | `"pagination-page-N"` | Clicking specific page |
| Pagination info | `data-testid` | `"pagination-info"` | Validating current page number |
| Loading indicator | `aria-label` | `"Loading personnel records"` | Waiting for data to load |

### CSS Classes (Reference)

- `.directory-section`: Container for section content.
- `.personnel-list`: Container for personnel records.
- `.personnel-record`: Individual personnel entry.
- `.personnel-name`, `.personnel-role`, `.personnel-department`: Record fields.
- `.pagination`: Pagination controls container.
- `.pagination-prev`, `.pagination-next`, `.pagination-page`: Button elements.
- `.active`: Applied to the current page button.
- `.spinner`: Loading indicator (typically a rotating icon).

### State-Based Selectors

| State | Expected Behavior | DOM Indicator |
|-------|-------------------|--------------|
| **First page** | Previous button disabled | `<button ... disabled>Previous</button>` |
| **Last page** | Next button disabled | `<button ... disabled>Next</button>` |
| **Loading** | Spinner visible | `<div class="spinner" style="display: block;">` |
| **Loaded** | Spinner hidden | `<div class="spinner" style="display: none;">` |
| **Current page** | Page button has `.active` class | `<button class="pagination-page active" data-page="1">` |

---

## Contract Validation Tests

### Test Cases for API Contract Compliance (if SPA)

1. **Valid Page Request**: GET `/api/directory/personnel?page=1&pageSize=20` returns 200 with expected JSON structure.
2. **Invalid Page Request**: GET `/api/directory/personnel?page=0` returns 400 with error message.
3. **Pagination Metadata**: Response includes `pagination` object with `currentPage`, `pageSize`, `totalRecords`, `totalPages`, `hasNextPage`, `hasPreviousPage`.
4. **Record Structure**: Each record in `records` array includes `id`, `fullName`, `roleTitle`.

### Test Cases for HTML Contract Compliance (if SSR)

1. **Navigation Link Presence**: Element with `data-testid="nav-people"` exists and is clickable.
2. **Personnel List Container**: Element with `data-testid="personnel-list"` exists and contains personnel records.
3. **Personnel Records**: Each `.personnel-record` has `data-testid` and `data-id` attributes.
4. **Pagination Controls**: Elements with `data-testid` for prev, next, and page buttons exist.
5. **Pagination State**: First page has Previous button disabled; last page has Next button disabled.
6. **Loading Indicator**: Loading spinner with `aria-label="Loading personnel records"` appears during data fetch and disappears when complete.

---

## Page Object Contract (Implementation Guidance)

### DirectoryNavigationPage API

```java
public class DirectoryNavigationPage {
    public void openDirectory();                    // Navigate to directory homepage
    public PeopleSectionPage selectPeopleSection(); // Click "People" and return PeopleSectionPage
    public boolean isPeopleOptionVisible();         // Verify "People" option is visible
}
```

### PeopleSectionPage API

```java
public class PeopleSectionPage {
    public void waitForRecordsToLoad();                            // Wait for personnel records to appear
    public List<PersonnelRecord> getVisibleRecords();              // Fetch all visible records on current page
    public int getRecordCount();                                   // Count visible records
    public PersonnelRecord getRecordByName(String name);           // Find record by name
    public int getCurrentPageNumber();                             // Get current page number
    public int getTotalPages();                                    // Get total pages
    public void navigateToPage(int pageNumber);                    // Navigate to specific page
    public void goToNextPage();                                    // Click next page
    public void goToPreviousPage();                                // Click previous page
    public boolean isNextPageButtonEnabled();                      // Check if Next button is enabled
    public boolean isPreviousPageButtonEnabled();                  // Check if Previous button is enabled
}
```

### PaginationComponent API

```java
public class PaginationComponent {
    public int getCurrentPageNumber();              // Get current page
    public int getTotalPages();                     // Get total pages
    public void goToNextPage();                     // Click Next
    public void goToPreviousPage();                 // Click Previous
    public void goToPage(int pageNumber);           // Click specific page
    public boolean isNextPageEnabled();             // Check Next button state
    public boolean isPreviousPageEnabled();         // Check Previous button state
}
```

---

**Status**: API/HTML Contract Defined ✅  
**Notes**: 
- If application uses SSR, focus on HTML contract validation.
- If application uses SPA/AJAX, focus on API contract validation.
- Page Objects abstract away contract details; tests use business methods only.
