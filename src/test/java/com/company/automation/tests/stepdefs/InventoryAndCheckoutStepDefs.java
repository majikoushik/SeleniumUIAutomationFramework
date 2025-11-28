package com.company.automation.tests.stepdefs;

import com.company.automation.framework.driver.DriverFactory;
import com.company.automation.framework.driver.DriverManager;
import com.company.automation.framework.pages.CartPage;
import com.company.automation.framework.pages.CheckoutPage;
import com.company.automation.framework.pages.InventoryPage;
import com.company.automation.framework.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Step definitions for Swag Labs inventory and checkout regression scenarios.
 */
public class InventoryAndCheckoutStepDefs {
    private static final Logger log = LoggerFactory.getLogger(InventoryAndCheckoutStepDefs.class);
    private final LoginPage loginPage = new LoginPage();
    private final InventoryPage inventoryPage = new InventoryPage();
    private final CartPage cartPage = new CartPage();
    private final CheckoutPage checkoutPage = new CheckoutPage();

    // To store selected products and expected total
    private final List<String> selectedProducts = new ArrayList<>();
    private double expectedItemTotal = 0.0;

    @Given("I am logged in to Swag Labs as a standard user")
    public void i_am_logged_in_to_swag_labs_as_standard_user() {
        loginPage.open();
        loginPage.loginAs("standard_user", "secret_sauce");

        boolean onProductsPage = loginPage.isProductsPageDisplayed();
        String currentUrl = DriverManager.getDriver().getCurrentUrl();

        Assertions.assertTrue(
                onProductsPage,
                "Expected to be on products page after login, but URL was: " + currentUrl
        );
    }

    @When("I am on the products page")
    public void i_am_on_the_products_page() {
        inventoryPage.ensureOnProductsPage();
    }

    @When("I sort products by {string}")
    public void i_sort_products_by(String sortText) {
        inventoryPage.sortByVisibleText(sortText);
    }

    @When("I add the following products to the cart:")
    public void i_add_the_following_products_to_the_cart(DataTable dataTable) {
        selectedProducts.clear();
        expectedItemTotal = 0.0;

        List<String> products = dataTable.asList(String.class);
        for (String rawName : products) {
            String productName = rawName.trim();
            selectedProducts.add(productName);

            inventoryPage.addProductToCart(productName);
            double price = inventoryPage.getProductPrice(productName);
            expectedItemTotal += price;

            log.info("[StepDefs] Added '" + productName + "' with price " + price);
        }

        log.info("[StepDefs] Expected item total from selected products: " + expectedItemTotal);

        // Go to cart
        inventoryPage.goToCart();

        // Optional: verify products in cart
        for (String product : selectedProducts) {
            Assertions.assertTrue(
                    cartPage.isProductInCart(product),
                    "Expected product '" + product + "' to be present in cart."
            );
        }
    }


    @When("I proceed to checkout with first name {string}, last name {string}, postal code {string}")
    public void i_proceed_to_checkout_with_first_name_last_name_postal_code(String firstName, String lastName, String postalCode) {
        cartPage.clickCheckout();
        checkoutPage.fillCheckoutInformation(firstName, lastName, postalCode);
    }

    @Then("the item total on the checkout page should equal the sum of selected products")
    public void the_item_total_on_the_checkout_page_should_equal_the_sum_of_selected_products() {
        double actualItemTotal = checkoutPage.getItemTotal();
        log.info("[StepDefs] Actual item total from checkout summary: " + actualItemTotal);

        // We can allow a very small delta for floating point comparisons
        Assertions.assertEquals(expectedItemTotal, actualItemTotal, 0.01,
                "Item total on checkout page does not match sum of selected products.");
    }

    @Then("I should see a successful order completion message")
    public void i_should_see_a_successful_order_completion_message() {
        checkoutPage.clickFinish();
        boolean success = checkoutPage.isOrderSuccessMessageDisplayed();
        Assertions.assertTrue(
                success,
                "Expected to see order completion success message, but it was not displayed."
        );
    }

    @Then("I should see product {string} in the inventory")
    public void i_should_see_product_in_the_inventory(String productName) {
        boolean visible = inventoryPage.isProductVisible(productName);
        org.junit.jupiter.api.Assertions.assertTrue(
                visible,
                "Expected product '" + productName + "' to be visible in inventory, but it was not found."
        );
    }
}
