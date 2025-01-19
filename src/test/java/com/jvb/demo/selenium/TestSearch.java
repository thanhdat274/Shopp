package com.jvb.demo.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestSearch extends SeleniumConfig{
    @Test
    public void test() throws Exception {
        driver.get("https://www.youtube.com/");
        WebElement txtSearch = driver.findElement(By.name("search_query"));
        txtSearch.click();
        txtSearch.sendKeys("black pink");
        txtSearch.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        List<WebElement> listEle = driver.findElements(By.tagName("ytd-video-renderer"));

        Actions actions = new Actions(driver);
        actions.click(listEle.get(0)).perform();
        Thread.sleep(3000);
    }

    @Test
    public void test2() throws Exception{
        driver.get("https://learn.liferay.com/w/reference/liferay-university");
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL).sendKeys("A").sendKeys("C").keyUp(Keys.CONTROL).build().perform();
        
    }
}
