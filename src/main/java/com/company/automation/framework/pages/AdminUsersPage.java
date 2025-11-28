package com.company.automation.framework.pages;

import com.company.automation.framework.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for Admin -> User Management -> System Users screen in OrangeHRM.
 *
 * Assumptions:
 * - User is already logged in.
 * - We navigate via the left menu "Admin" item.
 */
public class AdminUsersPage extends BasePage {

    // Left menu "Admin"
    private final By adminMenuItem = By.xpath("//span[text()='Admin']/ancestor::a");

    // Page heading: "System Users"
    private final By pageHeading = By.xpath("//h6[text()='System Users']");

    // Filter: Username input
    private final By usernameFilterInput =
            By.xpath("//label[text()='Username']/parent::div/following-sibling::div//input");

    // Filter: Search button
    private final By searchButton = By.xpath("//button[@type='submit']");

    // Table body rows
    private final By tableBodyRows = By.cssSelector("div.oxd-table-body div.oxd-table-card");

    /**
     * Navigate to the System Users page via the Admin menu.
     */
    public void navigateTo() {
        click(adminMenuItem);
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading));
        wait.until(ExpectedConditions.visibilityOfElementLocated(tableBodyRows));
    }

    /**
     * Search for a system user by username using the filter.
     *
     * @param username the username to search for (e.g., "Admin")
     */
    public void searchByUsername(String username) {
        type(usernameFilterInput, username);
        click(searchButton);

        // Wait for results to refresh (simple strategy: table body visible)
        wait.until(ExpectedConditions.visibilityOfElementLocated(tableBodyRows));
    }

    /**
     * Check if the user with given username appears in the results table.
     *
     * The username is typically in the second column of each row.
     *
     * @param username target username
     * @return true if found, false otherwise
     */
    public boolean isUserPresent(String username) {
        // XPath: find a row in the table body where the "Username" cell text equals the given username.
        By usernameCell = By.xpath(
                "//div[@class='oxd-table-body']//div[@role='row']//div[@role='cell'][2]//p[text()='" + username + "']"
        );

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameCell));
            return true;
        } catch (TimeoutException ex) {
            return false;
        }
    }
}
