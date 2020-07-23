package com.bat.webdrivers;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChromeWebDriver extends ChromiumWebDriver {
    @Value("${chrome.driver-path}")
    private String driverPath;

    @Value("${chrome.binary-path}")
    private String binaryPath;

    @Value("${chrome.enable-notifications}")
    private boolean enableNotifications;
    @Value("${chrome.enable-info-bar}")
    private boolean enableInfoBar;
    @Value("${chrome.start-maximized}")
    private boolean startMaximized;
    @Value("${chrome.accept-bad-ssl}")
    private boolean acceptBadSSL;
    @Value("${chrome.enable-extensions}")
    private boolean enableExtension;

    @Value("${chrome.use-proxy}")
    private boolean useProxy;
    @Value("${chrome.proxy-server}")
    private String proxyAddress;

    @Value("${chrome.profile}")
    private String profilePath;
    @Value("${chrome.open-in-private-mode}")
    private boolean openPrivateMode;

    @Value("${chrome.log}")
    private String logPath;
    @Value("${chrome.silent-log}")
    private boolean silentLog;

    @Value("${chrome.page-load-strategy}")
    private String pageLoadStrategy;

    public ChromeWebDriver() {}

    @Override
    public ChromeDriver getChromeDriver() throws Exception {
        ChromeOptions options = new ChromeOptions();

        try{
            setChromiumBrowserPreferences(options);
            // options.setPageLoadStrategy(PageLoadStrategy.EAGER);

            if(!isEmpty(logPath)) {
                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, logPath);
            }

            if(silentLog) {
                System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, Boolean.toString(silentLog));
            }

        } catch (Exception exception) {
            System.out.println("FAILED TO INITIATE CHROME DRIVER");
            throw new Exception("CHROME DRIVER EXCEPTION : " + exception.getMessage());
        }

        System.setProperty("webdriver.chrome.driver", driverPath);
        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, driverPath);
        return new ChromeDriver(options);
    }

    @Override
    public String getBinaryPath() {
        return binaryPath;
    }

    @Override
    public boolean isEnableNotifications() {
        return enableNotifications;
    }

    @Override
    public boolean isEnableInfoBar() {
        return enableInfoBar;
    }

    @Override
    public boolean isStartMaximized() {
        return startMaximized;
    }

    @Override
    public boolean isAcceptBadSSL() {
        return acceptBadSSL;
    }

    @Override
    public boolean isEnableExtension() {
        return enableExtension;
    }

    @Override
    public boolean isUseProxy() {
        return useProxy;
    }

    @Override
    public String getProxyAddress() {
        return proxyAddress;
    }

    @Override
    public String getProfilePath() {
        return profilePath;
    }

    @Override
    public boolean isOpenPrivateMode() {
        return openPrivateMode;
    }

    @Override
    public String getPrivateModeString() {
        return "--incognito";
    }

    @Override
    public String getPageLoadStrategy() {
        return pageLoadStrategy;
    }
}
