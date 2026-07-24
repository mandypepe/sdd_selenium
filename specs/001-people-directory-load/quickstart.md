# Quickstart Guide: People Directory Load Verification

## Prerequisites
- Java 17+ installed
- Maven 3.8+ installed
- Google Chrome browser installed (for local runs)

## Setup
Ensure that all dependencies are compiled:
```bash
mvn clean compile
```

## Running the Directory Tests
To execute only the newly created directory verification tests:
```bash
mvn -Dtest=com.project.tests.DirectoryTests test
```

To run the suite in parallel (if configured in `testng.xml`):
```bash
mvn test
```

## Viewing Reports
Allure reports will be generated in `allure-results/`. To view the interactive report locally, run:
```bash
allure serve allure-results
```
