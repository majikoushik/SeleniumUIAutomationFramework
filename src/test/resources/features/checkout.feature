@swaglabs @checkout @regression @smoke
Feature: Swag Labs checkout flow

  Scenario: Standard user can complete checkout and item total is correct
    Given I am logged in to Swag Labs as a standard user
    And I am on the products page
    And I sort products by "Price (low to high)"
    And I add the following products to the cart:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    When I proceed to checkout with first name "John", last name "Doe", postal code "12345"
    Then the item total on the checkout page should equal the sum of selected products
    And I should see a successful order completion message
