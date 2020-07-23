package com.bat.webdrivers;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chromium.ChromiumOptions;

import java.util.ArrayList;
import java.util.List;

public abstract class ChromiumWebDriver extends BaseWebDriver {
    private List<String> excludeSwitches = new ArrayList<String>();

    public abstract String getBinaryPath();
    public abstract boolean isEnableNotifications();
    public abstract boolean isEnableInfoBar();
    public abstract boolean isStartMaximized();
    public abstract boolean isAcceptBadSSL();
    public abstract boolean isEnableExtension();
    public abstract boolean isUseProxy();
    public abstract String getProxyAddress();
    public abstract String getProfilePath();
    public abstract boolean isOpenPrivateMode();
    public abstract String getPrivateModeString();
    public abstract String getPageLoadStrategy();

    protected void setChromiumBrowserPreferences(ChromiumOptions<?> options) {
        if(!isEmpty(getBinaryPath())) {
            options.setBinary(getBinaryPath());
        }

        if(!isEnableNotifications()) {
            options.addArguments("--disable-notifications");
        }

        if(!isEnableInfoBar()) {
            excludeSwitches.add("enable-automation");
        }

        if(isStartMaximized()) {
            options.addArguments("--start-maximized");
        }

        if(isAcceptBadSSL()) {
            options.addArguments("ignore-certificate-errors");
        }

        if(!isEnableExtension()) {
            excludeSwitches.add("load-extension");
        }

        if(isUseProxy() && !isEmpty(getProxyAddress())) {
            options.addArguments("--proxy-server=" + getProxyAddress());
        }

        if(isOpenPrivateMode()) {
            options.addArguments(getPrivateModeString());
        } else {
            if(!isEmpty(getProfilePath())) {
                options.addArguments("user-data-dir=" + getProfilePath());// Don't give default folder
            }
        }

        String pageLoadStrategy = getPageLoadStrategy();
        if(
                pageLoadStrategy.equalsIgnoreCase("normal")
                || pageLoadStrategy.equalsIgnoreCase("eager")
                || pageLoadStrategy.equalsIgnoreCase("none")
        ) {
            options.setPageLoadStrategy(PageLoadStrategy.fromString(pageLoadStrategy));
        }

        if(!excludeSwitches.isEmpty()) {
            options.setExperimentalOption("excludeSwitches", excludeSwitches.toArray());
        }
    }
}
