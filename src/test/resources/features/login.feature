@login @swaglabs @regression @smoke
Feature: Swag Labs login

  Scenario: Valid standard user can log in successfully
    Given I am on the Swag Labs login page
    When I login as "standard_user" with password "secret_sauce"
    Then I should see the products page
