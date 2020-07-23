package com.bat.webdrivers;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FirefoxWebDriver extends BaseWebDriver {
    @Value("${firefox.driver-path}")
    private String driverPath;

    @Value("${firefox.binary-path}")
    private String binaryPath;

    @Value("${firefox.enable-notifications}")
    private boolean enableNotification;
    @Value("${firefox.accept-bad-ssl}")
    private boolean acceptBadSSL;

    @Value("${firefox.use-proxy}")
    private boolean useProxy;
    @Value("${firefox.proxy-host}")
    private String proxyHost;
    @Value("${firefox.proxy-port}")
    private String proxyPort;

    @Value("${firefox.profile}")
    private String firefoxProfile;
    @Value("${firefox.open-in-private-mode}")
    private boolean openPrivateMode;

    @Value("${firefox.log}")
    private String logPath;

    @Value("${firefox.page-load-strategy}")
    private String pageLoadStrategy;

    public FirefoxWebDriver() {}

    @Override
    public FirefoxDriver getFirefoxDriver() throws Exception {
        FirefoxOptions options = new FirefoxOptions();

        try{
            if(!isEmpty(binaryPath)) {
                options.setBinary(binaryPath);
            }

            FirefoxProfile prof = null;
            if(!isEmpty(firefoxProfile)) {
                ProfilesIni allProf = new ProfilesIni();// all profiles on pc
                prof = allProf.getProfile(firefoxProfile);
            } else {
                prof = new FirefoxProfile();
                if(openPrivateMode) {
                    options.addArguments("-private");
                }
            }

            if(!enableNotification) {
                prof.setPreference("dom.webnotifications.enabled", false);
            }

            if(acceptBadSSL) {
                prof.setAcceptUntrustedCertificates(true);
                prof.setAssumeUntrustedCertificateIssuer(false);
            }

            if(
                    pageLoadStrategy.equalsIgnoreCase("normal")
                    || pageLoadStrategy.equalsIgnoreCase("eager")
                    || pageLoadStrategy.equalsIgnoreCase("none")
            ) {
                 options.setPageLoadStrategy(PageLoadStrategy.fromString(pageLoadStrategy));
            }

            if(useProxy && !isEmpty(proxyHost) && !isEmpty(proxyPort)) {
                prof.setPreference("network.proxy.type", 1);
                prof.setPreference("network.proxy.socks", proxyHost);
                prof.setPreference("network.proxy.socks_port", proxyPort);
            }

            if(!isEmpty(logPath)) {
                System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, logPath);
            }
            options.setProfile(prof);
        } catch (Exception exception) {
            System.out.println("FAILED TO INITIATE FIREFOX DRIVER");
            throw new Exception("FIREFOX DRIVER EXCEPTION : " + exception.getMessage());
        }

        System.setProperty("webdriver.gecko.driver", driverPath);
        return new FirefoxDriver(options);
    }
}
