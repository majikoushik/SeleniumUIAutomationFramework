package com.company.automation.framework.core;

import com.company.automation.framework.config.ConfigurationManager;
import com.company.automation.framework.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Common WebDriver + WebDriverWait functionality for all page objects.
 * This is your abstraction layer:
     * Holds driver
     * Holds WebDriverWait
     * Provides reusable actions (click, type, waitVisible)
 * Defines open(relativeUrl) so pages don’t hardcode full URLs
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        int timeout = ConfigurationManager.getInt("explicit.wait");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        waitVisible(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = waitVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitVisible(locator).getText();
    }

    public void open(String relativePath) {
        String baseUrl = ConfigurationManager.get("base.url");
        driver.get(baseUrl + relativePath);
    }
}
