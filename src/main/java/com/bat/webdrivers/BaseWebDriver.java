package com.bat.webdrivers;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.util.StringUtils;

public class BaseWebDriver {
    public BaseWebDriver() {}

    protected static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    public ChromeDriver getChromeDriver() throws Exception {
        throw new Exception("No Chrome driver invoked");
    }

    public FirefoxDriver getFirefoxDriver() throws Exception {
        throw new Exception("No Firefox driver invoked");
    }

    public EdgeDriver getEdgeDriver() throws Exception {
        throw new Exception("No Edge driver invoked");
    }
}
