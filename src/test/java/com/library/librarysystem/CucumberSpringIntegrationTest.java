package com.library.librarysystem;

// MAKE SURE THESE THREE LINES ARE AT THE TOP OF YOUR FILE
import io.cucumber.junit.platform.engine.Cucumber;
import org.junit.platform.suite.api.ConfigurationParameter;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Cucumber
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.library.librarysystem")
public class CucumberSpringIntegrationTest {
}