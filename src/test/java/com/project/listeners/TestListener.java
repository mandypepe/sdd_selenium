package com.project.listeners;

import com.project.drivers.DriverManager;
import com.project.utils.ReportLogger;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ReportLogger.log("START TEST: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ReportLogger.log("PASS TEST: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ReportLogger.log("FAIL TEST: " + result.getName() + " - Reason: " + result.getThrowable());
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Failure Screenshot - " + result.getName(), "image/png", new ByteArrayInputStream(screenshot), "png");
                ReportLogger.log("Attached failure screenshot to report for test: " + result.getName());
            } catch (Exception e) {
                ReportLogger.log("Failed to capture screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ReportLogger.log("SKIP TEST: " + result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    @Override
    public void onStart(ITestContext context) {
        ReportLogger.log("SUITE START: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ReportLogger.log("SUITE FINISH: " + context.getName());
    }
}
