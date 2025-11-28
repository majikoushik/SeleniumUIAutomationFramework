#@regression → this will not run with your default @smoke filter.
#@orangehrm @admin are just module tags for later slicing (@orangehrm and @admin).
@orangehrm @admin @regression
Feature: Admin - System Users search

  Scenario: Admin can search for an existing system user by username
    Given I am logged in to OrangeHRM as an admin user
    When I navigate to the System Users page
    And I search for system user "Admin"
    Then I should see system user "Admin" in the results
