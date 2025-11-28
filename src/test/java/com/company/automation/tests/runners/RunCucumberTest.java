package com.company.automation.tests.runners;

import io.cucumber.junit.platform.engine.Cucumber;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;

/**
 * JUnit Platform suite that runs Cucumber features from src/test/resources/features.
 *
 * Maven Surefire is configured to pick up this class and run it as the test entry point.
 */
@Suite
@Cucumber
@SelectClasspathResource("features")  // looks in src/test/resources/features
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "com.company.automation.tests.stepdefs"
)
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty, summary, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)
@ConfigurationParameter(
        key = FILTER_TAGS_PROPERTY_NAME,
        value = "@smoke"   // default filter; can be overridden from Maven
)
public class RunCucumberTest {
}
