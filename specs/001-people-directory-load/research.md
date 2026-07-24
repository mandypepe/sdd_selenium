# Technical Research: People Directory Initial Load Verification

**Feature**: People Directory Initial Load Verification  
**Feature ID**: 001  
**Date**: 2026-07-23  

---

## 1. Live DOM Analysis (`https://www.uci.cu/index.php/directorio/personas`)

A live page source check reveals the following critical HTML structure and CSS selectors:

### Page Context & Title
- **Page container**: `.view-directorio`
- **Main page title**: `.titulo-page h2` (Contains the text `"Directorio"`)
- **Active category link (Section Header)**: `.menu-directorio .menu-item.menu-item--active-trail a` or `.menu-directorio a[href*="personas"]` (Contains the text `"Personas"`)

### Personnel List Elements
- **Record Card/Row Container**: `.view-content .views-row`
- **Inner record container**: `.profesor.row` (Every person item is wrapped in this class)
- **Accented Name**: `.profesor .nombre a` (e.g. contains `"Abel Velázquez Pratts"` or `"Ada Isabel Llaneras Pulido"`)
- **Cargo / Job Role**: `.profesor .cargo` (Contains `"Profesor"`, `"Estudiante"`, etc.)
- **Grado Científico / Academic Degree**: `.profesor .grado-cientifico` (Contains `"Máster en Ciencias"`, etc.)

---

## 2. Accented & Accented Character Encoding Check

Accented characters such as `á`, `é`, `í`, `ó`, `ú`, `ñ` are present in names like `"Abel Velázquez Pratts"` and `"Ada Isabel Llaneras Pulido"`.
- **Mitigation**:
  1. WebDriver will fetch page HTML with UTF-8 character encoding automatically.
  2. Maven compiler plugin and files should be configured with `<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>` (already configured in `pom.xml`).
  3. Ensure Java assertions compare using standard UTF-8 strings.

---

## 3. Element Synchronization Strategy

To avoid fragile tests or hard delays:
1. Wait for page container `.view-directorio` to be visible.
2. Wait for at least one `.profesor` container to be visible inside `.view-content`.
3. Provide default timeout of 10s via `WaitUtils` wrapper inside page object business actions.
