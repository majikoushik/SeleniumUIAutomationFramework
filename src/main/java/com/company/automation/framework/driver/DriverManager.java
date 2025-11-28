package com.company.automation.framework.driver;

import org.openqa.selenium.WebDriver;

/**
 * Thread-safe holder of WebDriver instances.
 * Each test thread gets its own driver via ThreadLocal.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
        // utility class
    }

    public static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not set for this thread. " +
                    "Call DriverManager.setDriver() before using it.");
        }
        return driver;
    }

    public static void quit() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
