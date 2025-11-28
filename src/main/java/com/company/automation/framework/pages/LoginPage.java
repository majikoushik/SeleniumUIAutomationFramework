package com.company.automation.framework.pages;

import com.company.automation.framework.config.ConfigurationManager;
import com.company.automation.framework.core.BasePage;
import com.company.automation.framework.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for Swag Labs (https://www.saucedemo.com/) login page.
 */
public class LoginPage extends BasePage {

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
        System.out.println("[LoginPage] Navigated to: " + driver.getCurrentUrl());
        System.out.println("[LoginPage] Title: " + driver.getTitle());

        try {
            waitVisible(usernameInput);
        } catch (TimeoutException e) {
            System.out.println("[LoginPage] Timeout waiting for username input.");
            System.out.println("[LoginPage] Final URL: " + driver.getCurrentUrl());
            System.out.println("[LoginPage] Final title: " + driver.getTitle());
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
            System.out.println("[LoginPage] Timeout in loginAs(). URL: " + driver.getCurrentUrl());
            System.out.println("[LoginPage] Title: " + driver.getTitle());
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
