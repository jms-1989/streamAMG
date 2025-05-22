package streaming.tests;

import streaming.framework.config.ConfigManager;
import streaming.framework.driver.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class BaseTest {

    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(String browser) {
        DriverManager.setDriver(browser);
        // Start at Vidyard base URL
        DriverManager.getDriver().get(ConfigManager.getBaseUrl());
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
}