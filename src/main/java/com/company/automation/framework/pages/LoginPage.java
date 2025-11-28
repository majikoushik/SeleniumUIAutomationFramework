package com.company.automation.framework.pages;

import com.company.automation.framework.config.ConfigurationManager;
import com.company.automation.framework.core.BasePage;
import com.company.automation.framework.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for Swag Labs (https://www.saucedemo.com/) login page.
 */
public class LoginPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);
    // Swag Labs login locators
    private final By usernameInput = By.id("user-name");          // or By.cssSelector("[data-test='username']")
    private final By passwordInput = By.id("password");           // or By.cssSelector("[data-test='password']")
    private final By loginButton   = By.id("login-button");       // or By.cssSelector("[data-test='login-button']")
    private final By errorMessage  = By.cssSelector("h3[data-test='error']");

    // Products page title element (visible after successful login)
    private final By productsTitle = By.cssSelector("span.title");

    /**
     * Open Swag Labs login page and wait until username is visible.
     */
    public void open() {
        String loginPath = ConfigurationManager.get("login.path");
        super.open(loginPath);

        WebDriver driver = DriverManager.getDriver();
        log.info("[LoginPage] Navigated to: " + driver.getCurrentUrl());
        log.info("[LoginPage] Title: " + driver.getTitle());

        try {
            waitVisible(usernameInput);
        } catch (TimeoutException e) {
            log.info("[LoginPage] Timeout waiting for username input.");
            log.info("[LoginPage] Final URL: " + driver.getCurrentUrl());
            log.info("[LoginPage] Final title: " + driver.getTitle());
            throw e;
        }
    }

    /**
     * Perform login with given credentials.
     */
    public void loginAs(String username, String password) {
        try {
            type(usernameInput, username);
            type(passwordInput, password);
            click(loginButton);
        } catch (TimeoutException e) {
            WebDriver driver = DriverManager.getDriver();
            log.info("[LoginPage] Timeout in loginAs(). URL: " + driver.getCurrentUrl());
            log.info("[LoginPage] Title: " + driver.getTitle());
            throw e;
        }
    }

    /**
     * Check if Products page is displayed after login.
     */
    public boolean isProductsPageDisplayed() {
        try {
            return waitVisible(productsTitle).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Get login error message (for negative scenarios).
     */
    public String getErrorMessage() {
        try {
            return getText(errorMessage);
        } catch (TimeoutException e) {
            return "";
        }
    }
}
