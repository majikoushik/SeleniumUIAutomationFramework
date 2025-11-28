package com.company.automation.framework.driver;

import com.company.automation.framework.config.ConfigurationManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates WebDriver instances based on browser and run.mode (local/remote).
 * For now, we only implement local mode; remote (Selenium Grid) can be added later.
 */
public final class DriverFactory {

    private DriverFactory() {
        // utility class
    }

    public static WebDriver createInstance(BrowserType browserType) {
        String runMode = ConfigurationManager.get("run.mode");

        // For now we only support local. Later we can add remote/grid here.
        if (!"local".equalsIgnoreCase(runMode)) {
            throw new UnsupportedOperationException("Only local run.mode is supported at this point");
        }

        WebDriver driver;
        switch (browserType) {
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;

            case EDGE:
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;

            case CHROME:
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();

                // Disable Chrome password manager UI
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                options.setExperimentalOption("prefs", prefs);
                options.addArguments("--incognito");

                // Disable password leak detection (“Change your password” dialog)
                options.addArguments("--disable-features=PasswordLeakDetection");

                String browserProp = System.getProperty("browser", "chrome");
                if ("chrome-headless".equalsIgnoreCase(browserProp)) {
                    System.out.println("Headless chrome running");
                    options.addArguments("--headless=new");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--window-size=1920,1080");
                }

                driver = new ChromeDriver(options);
        }

        // Timeouts from config
        int implicitWait = ConfigurationManager.getInt("implicit.wait");
        int pageLoadTimeout = ConfigurationManager.getInt("page.load.timeout");

        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.manage().window().maximize();

        return driver;
    }
}
