package com.company.automation.framework.pages;

import com.company.automation.framework.config.ConfigurationManager;
import com.company.automation.framework.core.BasePage;
import org.openqa.selenium.By;

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

    public void open() {
        String loginPath = ConfigurationManager.get("login.path");
        super.open(loginPath);
    }

    public void loginAs(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
    }

    public String getErrorMessage() {
        return getText(errorToast);
    }
}
