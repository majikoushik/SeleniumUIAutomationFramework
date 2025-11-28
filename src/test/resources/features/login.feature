@login @smoke @orangehrm
Feature: OrangeHRM login

  Scenario: Valid admin user can log in successfully
    Given I am on the OrangeHRM login page
    When I login as "Admin" with password "admin123"
    Then I should see the OrangeHRM dashboard
