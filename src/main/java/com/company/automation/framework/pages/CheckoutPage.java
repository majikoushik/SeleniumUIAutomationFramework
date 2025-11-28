package com.company.automation.framework.pages;

import com.company.automation.framework.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

/**
 * Page Object for Swag Labs checkout steps (Step One, Step Two, and Complete).
 */
public class CheckoutPage extends BasePage {

    // Step One: Your Information
    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");

    // Step Two: Overview
    private final By itemTotalLabel = By.cssSelector(".summary_subtotal_label"); // e.g. "Item total: $39.98"
    private final By finishButton = By.id("finish");

    // Complete
    private final By successHeader = By.cssSelector("h2.complete-header"); // "Thank you for your order!"

    /**
     * Fill checkout information (Step One) and continue to overview.
     */
    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(postalCodeInput, postalCode);
        click(continueButton);
    }

    /**
     * Get the item total from the summary on Step Two.
     *
     * E.g. label text: "Item total: $39.98"
     */
    public double getItemTotal() {
        String label = getText(itemTotalLabel).trim();
        System.out.println("[CheckoutPage] Item total label: " + label);

        // Extract the number after the colon
        String[] parts = label.split(":");
        if (parts.length < 2) {
            throw new IllegalStateException("Unexpected item total label format: " + label);
        }
        String priceText = parts[1].trim(); // "$39.98"
        if (priceText.startsWith("$")) {
            priceText = priceText.substring(1);
        }
        return Double.parseDouble(priceText);
    }

    /**
     * Click Finish to complete the order.
     */
    public void clickFinish() {
        click(finishButton);
    }

    /**
     * Check if the order success message is displayed.
     */
    public boolean isOrderSuccessMessageDisplayed() {
        try {
            String text = getText(successHeader).trim();
            System.out.println("[CheckoutPage] Success header: " + text);
            return text.equalsIgnoreCase("Thank you for your order!");
        } catch (TimeoutException e) {
            return false;
        }
    }
}
