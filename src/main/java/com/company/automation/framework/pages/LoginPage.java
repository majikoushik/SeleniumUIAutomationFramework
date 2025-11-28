package com.company.automation.framework.pages;

import com.company.automation.framework.config.ConfigurationManager;
import com.company.automation.framework.core.BasePage;
import com.company.automation.framework.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for OrangeHRM login page.
 * URL pattern: base.url + login.path (configured in properties).
 */
public class LoginPage extends BasePage {

    // Locators from OrangeHRM demo login page
    private final By usernameInput = By.name("username");
    private final By passwordInput = By.name("password");
    private final By loginButton   = By.cssSelector("button[type='submit']");
    private final By errorToast    = By.cssSelector("p.oxd-text.oxd-text--p.oxd-alert-content-text");
    private final By loginHeading  = By.xpath("//h5[text()='Login']");

    /**
     * Open the login page and wait until it's clearly loaded.
     */
    public void open() {
        String loginPath = ConfigurationManager.get("login.path");
        super.open(loginPath);

        WebDriver driver = DriverManager.getDriver();
        System.out.println("[LoginPage] Navigated to: " + driver.getCurrentUrl());
        System.out.println("[LoginPage] Page title: " + driver.getTitle());

        try {
            // Wait for the "Login" heading first (very specific to OrangeHRM login page)
            waitVisible(loginHeading);
            // Then wait for username input to be visible
            waitVisible(usernameInput);
        } catch (TimeoutException e) {
            System.out.println("[LoginPage] Timeout waiting for login page elements.");
            System.out.println("[LoginPage] Final URL: " + driver.getCurrentUrl());
            System.out.println("[LoginPage] Final title: " + driver.getTitle());
            throw e; // rethrow so test still fails, but now with extra debug info in logs
        }
    }

    public void loginAs(String username, String password) {
        // At this point, open() should have ensured the field exists, but we keep waits for safety.
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

    public String getErrorMessage() {
        return getText(errorToast);
    }
}
