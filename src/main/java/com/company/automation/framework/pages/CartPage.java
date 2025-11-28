package com.company.automation.framework.pages;

import com.company.automation.framework.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page Object for the Swag Labs cart page.
 */
public class CartPage extends BasePage {

    // Cart title ("Your Cart")
    private final By cartTitle = By.cssSelector("span.title");

    // Cart items
    private final By cartItem = By.cssSelector(".cart_item");
    private final By itemName = By.cssSelector(".inventory_item_name");
    private final By itemPrice = By.cssSelector(".inventory_item_price");

    // Checkout button
    private final By checkoutButton = By.id("checkout");

    /**
     * Ensure we are on the cart page by waiting until the title text is "Your Cart".
     */
    public void ensureOnCartPage() {
        // Wait until the span.title text changes to "Your Cart"
        wait.until(ExpectedConditions.textToBe(cartTitle, "Your Cart"));

        WebElement titleElement = driver.findElement(cartTitle);
        System.out.println("[CartPage] Title: " + titleElement.getText());
    }

    /**
     * Verify that a specific product is present in the cart.
     */
    public boolean isProductInCart(String productName) {
        String normalized = productName.trim();

        ensureOnCartPage();

        List<WebElement> items = driver.findElements(cartItem);
        System.out.println("[CartPage] Number of cart items: " + items.size());

        for (WebElement item : items) {
            String name = item.findElement(itemName).getText().trim();
            System.out.println("[CartPage] Cart item name: '" + name + "'");
            if (name.equalsIgnoreCase(normalized)) {
                System.out.println("[CartPage] Matched cart item: '" + normalized + "'");
                return true;
            }
        }
        System.out.println("[CartPage] Product NOT found in cart: '" + normalized + "'");
        return false;
    }

    /**
     * Get the price of a specific product from the cart.
     */
    public double getProductPriceInCart(String productName) {
        String normalized = productName.trim();

        ensureOnCartPage();

        List<WebElement> items = driver.findElements(cartItem);
        for (WebElement item : items) {
            String name = item.findElement(itemName).getText().trim();
            if (name.equalsIgnoreCase(normalized)) {
                String priceText = item.findElement(itemPrice).getText().trim(); // e.g. "$29.99"
                if (priceText.startsWith("$")) {
                    priceText = priceText.substring(1);
                }
                double price = Double.parseDouble(priceText);
                System.out.println("[CartPage] Price for '" + normalized + "' in cart: " + price);
                return price;
            }
        }
        throw new IllegalArgumentException("Product '" + normalized + "' not found in cart.");
    }

    /**
     * Click the Checkout button.
     */
    public void clickCheckout() {
        ensureOnCartPage();  // makes sure we are really on Your Cart

        click(checkoutButton);  // now uses elementToBeClickable

        // The same span.title element is used on the checkout page.
        // Its text becomes "Checkout: Your Information".
        wait.until(ExpectedConditions.textToBe(cartTitle, "Checkout: Your Information"));
        System.out.println("[CartPage] Navigated to Checkout: Your Information.");
    }
}
