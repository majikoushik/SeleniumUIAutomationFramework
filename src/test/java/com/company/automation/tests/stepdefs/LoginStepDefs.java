package com.company.automation.tests.stepdefs;

import com.company.automation.framework.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

/**
 * Step definitions for Swag Labs login feature.
 */
public class LoginStepDefs {

    private final LoginPage loginPage = new LoginPage();

    @Given("I am on the Swag Labs login page")
    public void iAmOnTheSwagLabsLoginPage() {
        loginPage.open();
    }

    @When("I login as {string} with password {string}")
    public void iLoginAsWithPassword(String username, String password) {
        loginPage.loginAs(username, password);
    }

    @Then("I should see the products page")
    public void iShouldSeeTheProductsPage() {
        boolean onProducts = loginPage.isProductsPageDisplayed();
        Assertions.assertTrue(
                onProducts,
                "Expected to be on Swag Labs products page after login, but products title was not visible."
        );
    }
}
