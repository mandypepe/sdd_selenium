package com.project.tests;

import org.testng.annotations.Test;
import com.project.utils.ReportLogger;
import com.project.tests.base.BaseTest;

/**
 * @deprecated Superseded by {@link HealthCheckTests} which implements
 *             AC-1.1 (no blank/server error), AC-1.2 (directory content),
 *             AC-1.3 (unresponsive server), and AC-2.1 (screenshot on failure).
 *             This class is retained for backward compatibility but all tests
 *             are disabled.
 */
@Deprecated
public class ApplicationNoBlankOrServerErrorTest extends BaseTest {

    @Test(enabled = false, description = "Superseded by HealthCheckTests.verifyNoBlankScreenOrServerError")
    public void placeholder_noBlankOrServerError() {
        ReportLogger.log("DEPRECATED: Use HealthCheckTests instead — see specs/002-web-availability");
    }
}




