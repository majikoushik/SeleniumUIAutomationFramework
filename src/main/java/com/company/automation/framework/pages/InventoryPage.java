package com.company.automation.framework.pages;

import com.company.automation.framework.core.BasePage;
import com.company.automation.framework.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Page Object for Swag Labs inventory (products) page.
 */
public class InventoryPage extends BasePage {

    // "Products" title at top-left
    private final By productsTitle = By.cssSelector("span.title");

    // Sort dropdown
    // <select class="product_sort_container" ...>
    private final By sortDropdown = By.cssSelector("select.product_sort_container");

    // Product name elements
    private final By productNames = By.cssSelector(".inventory_item_name");

    // Cart icon in header
    private final By cartIcon = By.cssSelector("a.shopping_cart_link");

    /**
     * Ensure we are on the Products page.
     */
    public void ensureOnProductsPage() {
        WebDriver driver = DriverManager.getDriver();
        try {
            WebElement titleElement = waitVisible(productsTitle);
            String titleText = titleElement.getText();
            System.out.println("[InventoryPage] Products page title: " + titleText);
        } catch (TimeoutException e) {
            System.out.println("[InventoryPage] Timeout waiting for products title.");
            System.out.println("[InventoryPage] URL: " + driver.getCurrentUrl());
            System.out.println("[InventoryPage] Title: " + driver.getTitle());
            throw e;
        }
    }

    /**
     * Sort products using the visible text in the sort dropdown.
     */
    public void sortByVisibleText(String visibleText) {
        System.out.println("[InventoryPage] Sorting by: " + visibleText);
        WebElement dropdownElement = waitVisible(sortDropdown);
        Select select = new Select(dropdownElement);
        select.selectByVisibleText(visibleText);
        wait.until(ExpectedConditions.visibilityOfElementLocated(productNames));
    }

    /**
     * Add a product to the cart by its visible name using Swag Labs data-test convention.
     *
     * For example:
     *  "Sauce Labs Backpack"   -> data-test="add-to-cart-sauce-labs-backpack"
     *  "Sauce Labs Bike Light" -> data-test="add-to-cart-sauce-labs-bike-light"
     */
    public void addProductToCart(String productName) {
        String normalized = productName.trim();

        // Build the slug part used by Swag Labs in data-test/id.
        // This is enough for all core products we're using:
        // - lower-case
        // - spaces -> hyphens
        String slug = normalized.toLowerCase().replace(" ", "-");

        String dataTest = "add-to-cart-" + slug;

        By addButton = By.cssSelector("button[data-test='" + dataTest + "']");

        System.out.println("[InventoryPage] Adding product to cart: '" + normalized + "'");
        System.out.println("[InventoryPage] Using data-test: " + dataTest);

        click(addButton);
    }



    /**
     * Get the price of a product on the inventory page by its name.
     *
     * Example price text: "$29.99"
     */
    public double getProductPrice(String productName) {
        String normalized = productName.trim();

        // Find the inventory_item card that has a child with class containing 'inventory_item_name'
        // and matching normalized text, then locate its price element.
        String xpath =
                "//*[contains(@class,'inventory_item_name') and normalize-space()='" + normalized + "']" +
                        "/ancestor::div[contains(@class,'inventory_item')]" +
                        "//div[contains(@class,'inventory_item_price')]";

        By priceLocator = By.xpath(xpath);
        String priceText = getText(priceLocator).trim();   // e.g. "$29.99"

        System.out.println("[InventoryPage] Price text for '" + normalized + "': " + priceText);

        return parsePrice(priceText);
    }



    /**
     * Click on the cart icon to go to the cart page.
     */
    public void goToCart() {
        System.out.println("[InventoryPage] Going to cart page.");

        By titleLocator = By.cssSelector("span.title");

        for (int attempt = 1; attempt <= 2; attempt++) {
            click(cartIcon);  // uses elementToBeClickable now
            try {
                wait.until(ExpectedConditions.textToBe(titleLocator, "Your Cart"));
                System.out.println("[InventoryPage] Cart page loaded (attempt " + attempt + ").");
                return;
            } catch (TimeoutException e) {
                System.out.println("[InventoryPage] Cart navigation not complete on attempt "
                        + attempt + " (title not 'Your Cart').");
            }
        }

        throw new RuntimeException("Failed to navigate to Cart page after retries.");
    }

    /**
     * Check if a product with the given name is visible in the inventory list.
     */
    public boolean isProductVisible(String productName) {
        List<WebElement> products = driver.findElements(productNames);
        System.out.println("[InventoryPage] Number of products found: " + products.size());
        for (WebElement p : products) {
            String name = p.getText().trim();
            System.out.println("[InventoryPage] Product name: " + name);
            if (name.equalsIgnoreCase(productName.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Utility to parse a price like "$29.99" into 29.99 (double).
     */
    private double parsePrice(String priceText) {
        if (priceText.startsWith("$")) {
            priceText = priceText.substring(1);
        }
        return Double.parseDouble(priceText);
    }
}
