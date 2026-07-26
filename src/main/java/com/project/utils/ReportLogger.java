package com.project.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class ReportLogger {

    public static void log(String message) {
        // Log simple text to Allure (if configured)
        Allure.addAttachment("log", new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8)));
        System.out.println(message);
    }

    /**
     * Captures a PNG screenshot from the current browser state and attaches
     * it to the Allure report.
     *
     * @param driver the WebDriver instance to capture from
     * @param name   the attachment name displayed in the report
     */
    public static void attachScreenshot(WebDriver driver, String name) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), "png");
            System.out.println("Screenshot attached: " + name);
        } catch (Exception e) {
            System.out.println("Failed to attach screenshot: " + e.getMessage());
        }
    }
}

