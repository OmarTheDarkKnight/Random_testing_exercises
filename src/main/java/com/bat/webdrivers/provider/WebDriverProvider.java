package com.bat.webdrivers.provider;

import com.bat.configurations.SpringConfig;
import com.bat.webdrivers.ChromeWebDriver;
import com.bat.webdrivers.EdgeWebDriver;
import com.bat.webdrivers.FirefoxWebDriver;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class WebDriverProvider {
    @Value("${selenium.implicit_wait_time}")
    private int implicitWaitTime;

    @Autowired
    private ChromeWebDriver chromeWebDriver;

    @Autowired
    private FirefoxWebDriver firefoxWebDriver;

    @Autowired
    private EdgeWebDriver edgeWebDriver;

    private ChromeDriver getChromeWebDriver() throws Exception {
        return chromeWebDriver.getChromeDriver();
    }

    private FirefoxDriver getFirefoxWebDriver() throws Exception {
        return firefoxWebDriver.getFirefoxDriver();
    }

    private EdgeDriver getEdgeWebDriver() throws Exception {
        return edgeWebDriver.getEdgeDriver();
    }

    public WebDriver getWebDriver(@NotNull String browserName) throws Exception {
        WebDriver driver = null;
        if(browserName.toLowerCase().equals("chrome")) driver = getChromeWebDriver();
        else if(browserName.toLowerCase().equals("firefox")) driver = getFirefoxWebDriver();
        else if(browserName.toLowerCase().equals("edge")) driver = getEdgeWebDriver();
        else throw new Exception("Invalid browser name provided");

        // add implicit wait time before returning
        driver.manage().timeouts().implicitlyWait(implicitWaitTime, TimeUnit.SECONDS);
        return driver;
    }
}
