# UI Automation Framework
Selenium + Cucumber BDD + Page Object Model (POM) + Allure Reports + Maven + JUnit 5 + Parallel Execution + CI/CD

This repository contains an enterprise-grade UI automation framework built with:
- Java 17+ (runs fine on JDK 25)
- Selenium 4
- Cucumber 7 (BDD)
- JUnit 5 Platform
- Page Object Model (POM)
- Allure Reporting
- Maven build system
- Parallel execution
- Multiple environments
- GitHub Actions + Jenkins CI support

The framework uses the OrangeHRM demo application as a sample AUT:
https://opensource-demo.orangehrmlive.com/

It is designed to be easily extendable by any team member.

## Project Structure
```
ui-automation-bdd/
  |- src
  |  |- main
  |  |  |- java/com/company/automation/framework
  |  |  |  |- config/            -> Environment config loader
  |  |  |  |- driver/            -> DriverFactory, DriverManager
  |  |  |  |- core/              -> BasePage + common UI actions
  |  |  |  |- pages/             -> Page Objects (LoginPage, AdminUsersPage, ...)
  |  |  |  |- utils/             -> Helper utilities
  |  |- test
  |     |- java/com/company/automation/tests
  |     |  |- stepdefs/          -> Cucumber Step Definitions + Hooks
  |     |  |- runners/           -> RunCucumberTest JUnit Runner
  |     |- resources
  |        |- features/          -> Cucumber feature files (.feature)
  |        |- config/            -> config-qa.properties, config-dev.properties, ...
  |- pom.xml
  |- README.md
```

## Core Architecture
1. **Page Object Model (POM)**
   - Every UI page is implemented as a Page Object under `src/main/java/.../pages`.
   - Example:

```java
public class LoginPage extends BasePage {
    private final By username = By.name("username");
    private final By password = By.name("password");

    public void loginAs(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginButton);
    }
}
```

   - Step Definitions never interact with Selenium directly; they always use Page Objects, ensuring maintainability.

2. **BasePage**
   - Provides WebDriver access via DriverManager and explicit waits via WebDriverWait.
   - Generic helper methods: `waitVisible(locator)`, `click(locator)`, `type(locator, text)`, `open(relativeUrl)`.

3. **Driver Layer (Thread-Safe)**
   - DriverFactory creates drivers based on browser name (Chrome/Firefox/Edge) with headless support for CI.
   - DriverManager uses ThreadLocal to enable safe parallel execution.

4. **Configuration Management**
   - Environment-specific properties files under `src/test/resources/config/` (e.g., `config-qa.properties`, `config-dev.properties`).
   - Config values include `base.url`, `login.path`, `explicit.wait`, `run.mode`.
   - `ConfigurationManager` loads the correct file via `-Denv=qa` or `-Denv=dev`; Maven profiles set this automatically.

5. **Cucumber BDD Layer**
   - Feature files stored under `src/test/resources/features/`.
   - Example scenarios:
     - Login smoke test (`login.feature`):

```
@login @smoke @orangehrm
Scenario: Valid admin user can log in successfully
  Given I am on the OrangeHRM login page
  When I login as "Admin" with password "admin123"
  Then I should see the OrangeHRM dashboard
```

     - Admin user search regression (`admin_user_search.feature`):

```
@orangehrm @admin @regression
Scenario: Admin can search for an existing system user
  Given I am logged in to OrangeHRM as an admin user
  When I navigate to the System Users page
  And I search for system user "Admin"
  Then I should see system user "Admin" in the results
```

6. **Hooks**
   - Manages driver start/stop per scenario, takes screenshots on failure, and quits driver after each scenario.

7. **JUnit 5 Cucumber Runner**
   - `RunCucumberTest` loads features, stepdefs, integrates Allure, and sets default tag filters (e.g., `@smoke`).

## Running the Tests
1. Smoke suite (default):
```
mvn clean test -Pqa-chrome
```
2. Full regression:
```
mvn clean test -Pqa-chrome-headless -Dcucumber.filter.tags="@regression"
```
3. Only admin tests:
```
mvn clean test -Pqa-chrome -Dcucumber.filter.tags="@orangehrm and @admin"
```
4. Against DEV env:
```
mvn clean test -Pdev-chrome -Dcucumber.filter.tags="@smoke"
```

## Allure Reports
- Live report: `mvn allure:serve`
- Static HTML report: `mvn allure:report` (output: `target/site/allure-maven-plugin/index.html`)

## Parallel Execution
- JUnit 5 parallel mode configured via `src/test/resources/junit-platform.properties`:
```
junit.jupiter.execution.parallel.enabled=true
junit.jupiter.execution.parallel.mode.default=concurrent
junit.jupiter.execution.parallel.config.fixed.parallelism=3
```
- Each scenario runs in its own browser instance because WebDriver is managed via ThreadLocal.

## CI/CD Integration
- GitHub Actions workflow: `.github/workflows/ui-tests.yml` runs `mvn clean test -Pqa-chrome-headless -Dcucumber.filter.tags="@smoke"` and uploads `allure-results`.
- Jenkinsfile stages include running the suite and publishing Allure results, with optional tag/profile parameters and notifications.

## How to Add a New Test
1. Create a new Page Object under `src/main/java/.../pages`.
2. Add a new feature file under `src/test/resources/features` with relevant tags.
3. Add Step Definitions under `src/test/java/.../stepdefs` calling Page Object methods.
4. Run the suite with the desired profile and tags, e.g.:
```
mvn clean test -Pqa-chrome -Dcucumber.filter.tags="@module"
```
