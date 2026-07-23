package com.project.tests.base;

import com.project.drivers.DriverFactory;
import com.project.drivers.DriverManager;
import com.project.utils.ReportLogger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"baseUrl", "browser"})
    public void setUp(@Optional("https://www.uci.cu/index.php/directorio/personas") String baseUrl,
                      @Optional("chrome") String browser) {
        driver = DriverFactory.createDriver(browser);
        DriverManager.setDriver(driver);
        ReportLogger.log("Browser started: " + browser);
        driver.manage().window().maximize();
        driver.get(baseUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        ReportLogger.log("Closing browser");
        DriverManager.quitDriver();
    }
}


