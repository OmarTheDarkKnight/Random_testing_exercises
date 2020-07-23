package exercises;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class MagicBrickClickDropdown extends BaseTest {
    @Test
    public void magicBrickDemoTest(String[] args) {
        launchBrowser("Chrome");
        driver.get("https://www.magicbricks.com");
        String magicBrickClick = "18K+ Owner Properties";

        JavascriptExecutor js = (JavascriptExecutor)driver;
        WebElement element = driver.findElement(By.xpath("//li[@id='rentDrop']/div"));
        js.executeScript("arguments[0].classList.add('active')",element);
        js.executeScript("arguments[0].classList.remove('animated')",element);

        driver.findElement(By.xpath("//div[@id='staticSwiperSliderRent']/descendant::span[contains(., '"+ magicBrickClick +"')]")).click();
        driver.findElement(By.xpath("//div[@id='exitIntent']/descendant::div[@class='m-contact__close']")).click();
    }
}
