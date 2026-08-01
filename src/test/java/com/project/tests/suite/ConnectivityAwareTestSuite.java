package com.project.tests.suite;

import com.project.utils.TestConfiguration;
import com.project.utils.ReportLogger;
import com.project.utils.ConnectivityChecker;
import com.project.drivers.DriverManager;
import com.project.drivers.DriverFactory;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Test suite controller that manages connectivity awareness across all tests.
 * This class runs before all tests to determine if the target website is available
 * and configures the test execution mode accordingly.
 */
public class ConnectivityAwareTestSuite {
    
    private static final String DEFAULT_BASE_URL = "https://www.uci.cu/index.php/directorio/personas";
    
    @BeforeSuite(alwaysRun = true)
    @Parameters({"baseUrl", "offlineMode"})
    public void suiteSetUp(@Optional(DEFAULT_BASE_URL) String baseUrl, 
                          @Optional("auto") String offlineMode) {
        
        ReportLogger.log("=== CONNECTIVITY AWARE TEST SUITE INITIALIZATION ===");
        ReportLogger.log("Target URL: " + baseUrl);
        ReportLogger.log("Offline Mode Parameter: " + offlineMode);
        
        // Handle explicit offline mode setting
        if ("true".equalsIgnoreCase(offlineMode)) {
            TestConfiguration.enableOfflineMode();
            ReportLogger.log("OFFLINE MODE EXPLICITLY ENABLED - All tests will skip external dependencies");
            return;
        } else if ("false".equalsIgnoreCase(offlineMode)) {
            TestConfiguration.disableOfflineMode();
            ReportLogger.log("ONLINE MODE EXPLICITLY ENABLED - Tests will require external dependencies");
        }
        
        // Auto-detect connectivity if in auto mode
        if ("auto".equalsIgnoreCase(offlineMode)) {
            ReportLogger.log("AUTO-DETECT MODE: Checking website availability...");
            
            try {
                // Initialize a temporary driver for connectivity check
                DriverManager.setDriver(DriverFactory.createDriver("chrome"));
                
                ConnectivityChecker checker = new ConnectivityChecker();
                boolean isAvailable = checker.isWebsiteAccessible(DriverManager.getDriver(), baseUrl);
                
                if (isAvailable) {
                    ReportLogger.log("✅ Website is available - tests will proceed normally");
                    TestConfiguration.disableOfflineMode();
                } else {
                    ReportLogger.log("❌ Website is not available - enabling offline mode");
                    TestConfiguration.enableOfflineMode();
                }
                
            } catch (Exception e) {
                ReportLogger.log("⚠️ Error checking connectivity: " + e.getMessage());
                ReportLogger.log("🔄 Defaulting to offline mode for safety");
                TestConfiguration.enableOfflineMode();
            } finally {
                // Clean up the temporary driver
                DriverManager.quitDriver();
            }
        }
        
        // Log final configuration
        ReportLogger.log("=== FINAL TEST CONFIGURATION ===");
        ReportLogger.log(TestConfiguration.getConfigurationSummary());
        
        if (TestConfiguration.isOfflineMode()) {
            ReportLogger.log("🔧 OFFLINE MODE ACTIVE:");
            ReportLogger.log("   - Tests requiring external website will be SKIPPED");
            ReportLogger.log("   - Framework and utility tests will RUN");
            ReportLogger.log("   - No network dependencies will be attempted");
        } else {
            ReportLogger.log("🌐 ONLINE MODE ACTIVE:");
            ReportLogger.log("   - All tests will run normally");
            ReportLogger.log("   - External website dependencies are REQUIRED");
            ReportLogger.log("   - Network connectivity will be validated");
        }
        
        ReportLogger.log("=== SUITE INITIALIZATION COMPLETE ===");
    }
}