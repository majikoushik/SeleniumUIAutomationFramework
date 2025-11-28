package com.company.automation.tests.stepdefs;

import com.company.automation.framework.driver.DriverManager;
import com.company.automation.framework.pages.AdminUsersPage;
import com.company.automation.framework.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

/**
 * Step definitions for Admin -> System Users search scenarios.
 */
public class AdminUserManagementStepDefs {

    private final LoginPage loginPage = new LoginPage();
    private final AdminUsersPage adminUsersPage = new AdminUsersPage();

    @Given("I am logged in to OrangeHRM as an admin user")
    public void i_am_logged_in_as_admin_user() {
        // Use the existing login page object
        loginPage.open();
        loginPage.loginAs("Admin", "admin123");

        // A simple sanity check: we should be on dashboard after login
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        Assertions.assertTrue(
                currentUrl.contains("/dashboard"),
                "Expected to be on dashboard after login, but URL was: " + currentUrl
        );
    }

    @When("I navigate to the System Users page")
    public void i_navigate_to_the_system_users_page() {
        adminUsersPage.navigateTo();
    }

    @When("I search for system user {string}")
    public void i_search_for_system_user(String username) {
        adminUsersPage.searchByUsername(username);
    }

    @Then("I should see system user {string} in the results")
    public void i_should_see_system_user_in_the_results(String username) {
        boolean present = adminUsersPage.isUserPresent(username);
        Assertions.assertTrue(
                present,
                "Expected to find user '" + username + "' in System Users table, but it was not present."
        );
    }
}
