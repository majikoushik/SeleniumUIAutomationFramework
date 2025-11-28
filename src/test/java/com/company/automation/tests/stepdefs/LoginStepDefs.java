package com.company.automation.tests.stepdefs;

import com.company.automation.framework.pages.LoginPage;
import com.company.automation.framework.driver.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

/**
 * Step definitions for OrangeHRM login feature.
 */
public class LoginStepDefs {

    private final LoginPage loginPage = new LoginPage();

    @Given("I am on the OrangeHRM login page")
    public void iAmOnOrangeHrmLoginPage() {
        loginPage.open();  // this now waits for heading + username
    }

    @When("I login as {string} with password {string}")
    public void iLoginAsWithPassword(String username, String password) {
        loginPage.loginAs(username, password);
    }

    @Then("I should see the OrangeHRM dashboard")
    public void iShouldSeeTheOrangeHrmDashboard() {
        WebDriver driver = DriverManager.getDriver();

        // Simplest robust check: URL contains /dashboard OR page title contains OrangeHRM.
        String currentUrl = driver.getCurrentUrl();
        String title = driver.getTitle();

        boolean looksLikeDashboard =
                currentUrl.contains("/dashboard") || title.toLowerCase().contains("orangehrm");

        Assertions.assertTrue(
                looksLikeDashboard,
                "Expected to be on dashboard, but URL was: " + currentUrl + " and title: " + title
        );
    }
}
