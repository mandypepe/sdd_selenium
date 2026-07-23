package com.project.utils;

import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class ReportLogger {

    public static void log(String message) {
        // Log simple text to Allure (if configured)
        Allure.addAttachment("log", new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8)));
        System.out.println(message);
    }
}

