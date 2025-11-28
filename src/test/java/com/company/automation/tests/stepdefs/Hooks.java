package com.company.automation.tests.stepdefs;

import com.company.automation.framework.driver.BrowserType;
import com.company.automation.framework.driver.DriverFactory;
import com.company.automation.framework.driver.DriverManager;
import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.Locale;

/**
 * Global Cucumber hooks for WebDriver lifecycle and failure screenshots.
 */
public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) {
        // Determine browser from system property: -Dbrowser=chrome / chrome-headless / firefox / edge
        String browserProp = System.getProperty("browser", "chrome").toLowerCase(Locale.ROOT);
        BrowserType browserType;

        if (browserProp.contains("firefox")) {
            browserType = BrowserType.FIREFOX;
        } else if (browserProp.contains("edge")) {
            browserType = BrowserType.EDGE;
        } else {
            browserType = BrowserType.CHROME;
        }

        WebDriver driver = DriverFactory.createInstance(browserType);
        DriverManager.setDriver(driver);

        System.out.println("=== Starting scenario: " + scenario.getName() + " on browser: " + browserType + " ===");
    }

    @After
    public void afterScenario(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                try {
                    WebDriver driver = DriverManager.getDriver();
                    if (driver instanceof TakesScreenshot) {
                        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                        scenario.attach(screenshot, "image/png", "Failure screenshot");
                    }
                } catch (IllegalStateException e) {
                    // Driver was never created (e.g., session failure in @Before) – just skip screenshot.
                    System.out.println("No WebDriver available for screenshot: " + e.getMessage());
                }
            }
        } finally {
            // Quit if present; DriverManager.quit() already handles null safely.
            DriverManager.quit();
        }
    }

}
