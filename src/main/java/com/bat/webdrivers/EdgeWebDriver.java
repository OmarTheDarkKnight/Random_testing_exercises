package com.bat.webdrivers;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EdgeWebDriver extends ChromiumWebDriver {
    @Value("${edge.driver-path}")
    private String driverPath;

    @Value("${edge.binary-path}")
    private String binaryPath;

    @Value("${edge.enable-notifications}")
    private boolean enableNotifications;
    @Value("${edge.enable-info-bar}")
    private boolean enableInfoBar;
    @Value("${edge.start-maximized}")
    private boolean startMaximized;
    @Value("${edge.accept-bad-ssl}")
    private boolean acceptBadSSL;
    @Value("${edge.enable-extensions}")
    private boolean enableExtension;

    @Value("${edge.use-proxy}")
    private boolean useProxy;
    @Value("${edge.proxy-server}")
    private String proxyAddress;

    @Value("${edge.profile}")
    private String profilePath;
    @Value("${edge.open-in-private-mode}")
    private boolean openPrivateMode;

    @Value("${edge.log}")
    private String logPath;
    @Value("${edge.silent-log}")
    private boolean silentLog;

    @Value("${edge.page-load-strategy}")
    private String pageLoadStrategy;

    public EdgeWebDriver() {}

    @Override
    public EdgeDriver getEdgeDriver() throws Exception {
        EdgeOptions options = new EdgeOptions();

        try{
            setChromiumBrowserPreferences(options);
            // options.setPageLoadStrategy(PageLoadStrategy.EAGER);

            if(!isEmpty(logPath)) {
                System.setProperty(EdgeDriverService.EDGE_DRIVER_LOG_PROPERTY, logPath);
            }

            if(silentLog) {
                System.setProperty(EdgeDriverService.EDGE_DRIVER_SILENT_OUTPUT_PROPERTY, Boolean.toString(silentLog));
            }

        } catch (Exception exception) {
            System.out.println("FAILED TO INITIATE CHROME DRIVER");
            throw new Exception("CHROME DRIVER EXCEPTION : " + exception.getMessage());
        }

        System.setProperty("webdriver.chrome.driver", driverPath);
        System.setProperty(EdgeDriverService.EDGE_DRIVER_EXE_PROPERTY, driverPath);
        return new EdgeDriver(options);
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
    public String toString() {
        return "EdgeWebDriver";
    }

    @Override
    public boolean isOpenPrivateMode() {
        return openPrivateMode;
    }

    @Override
    public String getPrivateModeString() {
        return "--inPrivate";
    }

    @Override
    public String getPageLoadStrategy() {
        return pageLoadStrategy;
    }
}
