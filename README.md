# sdd_selenium
GitHub Spec Kit and Selenium

## sdd-selenium-framework (Generated Structure)

Example structure of a Selenium framework (Java + Maven + TestNG).

### Created Structure:

```
sdd-selenium-framework/
├── .github/workflows/         # CI/CD Pipelines (GitHub Actions)
├── config/                    # Configuration files
├── src/
│   ├── main/                  # Support: drivers, pages, utils
│   └── test/                  # Tests: tests, data, listeners
├── pom.xml                    # Dependency Manager (Maven)
├── testng.xml                 # TestNG Configuration
└── README.md                  # Instructions
```

### How to Tag Tests:

```java
@Test(groups = {"smoke"})
public void smokeTest() {
    // Test implementation
}

@Test(groups = {"regression"})
public void regressionTest() {
    // Test implementation
}
```

```java
import com.project.annotations.TestCategory;

@TestCategory({"smoke", "critical"})
@Test
public void miTest() {
    // tu código
}
```

### How to Run (Local):

1) Ensure you have Java 17 and Maven installed.
2) From the project root, run:

```bash
mvn test
```
```bash
mvn test -DtestCategory=smoke
```
```bash
mvn test -DtestCategory=regression
```

### Notes:

- The project uses WebDriverManager to automatically download drivers.
- Update `config/env.qa.properties` or `config/env.staging.properties` according to your environment.
- `testng.xml` is configured for test suite execution with parameters (`baseUrl`, `browser`).
- For detailed AI agent guidance, see `AGENTS.md` (English version).
- Current test base URL: `https://www.uci.cu/index.php/directorio/personas`
