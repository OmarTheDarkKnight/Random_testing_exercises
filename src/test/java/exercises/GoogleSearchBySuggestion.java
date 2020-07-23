package exercises;

import base.BaseTest;
import org.openqa.selenium.*;
import org.testng.annotations.Test;

public class GoogleSearchBySuggestion extends BaseTest {

    @Test
    public void googleSearchTest() {
        String searchString = "hello google";
        launchBrowser("Chrome");
        driver.get("http://google.com");
        WebElement element = driver.findElement(By.xpath("//input[@name='q']"));
        element.sendKeys("Hello");
        element.sendKeys(" ");
        String xpath = "//input[@name='q']/ancestor::div[@class='RNNXgb']/following-sibling::div/descendant::ul/descendant::div[contains(., '" + searchString.toLowerCase() + "')]";
        element = driver.findElement(By.xpath(xpath));
        element.click();
        System.out.println(xpath);
    }
}
