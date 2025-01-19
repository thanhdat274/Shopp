package com.jvb.demo.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumConfig {
    protected static WebDriver driver;
    protected static final String webDriverPath = "C:\\Users\\ABC\\Downloads\\chromedriver.exe";
    protected static ChromeOptions options;

    @BeforeAll
    public static void initDriver() {
        options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        System.setProperty("webdriver.chrome.driver", webDriverPath);
        driver = new ChromeDriver(options);
    }

    @AfterAll
    public static void close() {
        driver.close();
    }
}
