# Data Model: People Directory Initial Load

This document defines the page representations and models representing UI records.

## 1. DirectoryPage Representation
Encapsulates components and actions for `https://www.uci.cu/index.php/directorio/personas`.

### Exposed Model
```java
public class DirectoryPage extends BasePage {
    private final PersonList personList;
    
    // Locators
    private final By pageTitle = By.cssSelector(".titulo-page h2");
    private final By activeSection = By.cssSelector(".menu-directorio .menu-item.menu-item--active-trail a");
}
```

## 2. PersonList Component Representation
Encapsulates list rendering and children.

```java
public class PersonList {
    private final WebDriver driver;
    private final By listContainer = By.cssSelector(".view-content");
    private final By rowRecords = By.cssSelector(".view-content .views-row");
    private final By personName = By.cssSelector(".profesor .nombre a");
}
```

## 3. PersonRecord Entity
Data holder representing an individual record retrieved from the DOM.

```java
public class PersonRecord {
    private final String name;
    private final String role;
    private final String degree;

    public PersonRecord(String name, String role, String degree) {
        this.name = name;
        this.role = role;
        this.degree = degree;
    }

    public String getName() { return name; }
    public String getRole() { return role; }
    public String getDegree() { return degree; }
}
```
