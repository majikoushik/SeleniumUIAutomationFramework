@swaglabs @inventory @regression @smoke
Feature: Swag Labs inventory filtering

  Scenario: Standard user can see a specific product in the inventory
    Given I am logged in to Swag Labs as a standard user
    When I am on the products page
    And I sort products by "Price (low to high)"
    Then I should see product "Sauce Labs Backpack" in the inventory
