package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.bat.configurations.SpringConfig;
import com.bat.webdrivers.provider.WebDriverProvider;
import manager.ExtentManager;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class BaseTest {
    public ExtentReports report = null;
    public ExtentTest test = null;
    public SoftAssert softAssert = null;

    public String browser = null;
    public String testerRole = null;

    public WebDriver driver = null;
    public WebDriverWait wait = null;
    public Properties prop = null;
    public JavascriptExecutor js;
    public Actions act;

    /**
     *
     * @param result
     * @param context
     *
     * initializes all the required variables before the test methods run
     */
    @BeforeMethod(alwaysRun = true)
    public void init(ITestContext context, ITestResult result) {
        report = ExtentManager.getReport();
        test = report.createTest(result.getMethod().getMethodName().toUpperCase());
        result.setAttribute("testObject", test);
        softAssert = new SoftAssert();

        //reading parameter from testng.xml
        browser = context.getCurrentXmlTest().getParameter("browser");
        testerRole = getParamBasedOnGroup(context, "testerRole");
    }

    /**
     * wraps up the required actions after test methods' execution is completed
     */
    @AfterMethod(alwaysRun = true)
    public void wrapUp() {
        report.flush();
    }

    /**
     *
     * @param message
     *
     * logs the message as info
     */
    public void log(String message) {
        test.log(Status.INFO, message);
    }

    /**
     *
     * @param message
     *
     * logs the message as error
     */
    public void error(String message) {
        test.log(Status.ERROR, message);
    }

    /**
     *
     * @param message
     *
     * logs the message as success
     */
    public void pass(String message) {
        test.log(Status.PASS, message);
    }

    /**
     *
     * @param message
     *
     * logs the message as failure
     */
    public void fail(String message) { // only fails in extent reports
        test.log(Status.FAIL, message);
    }

    /**
     *
     * @param message
     *
     * solves the "errors going at the end of the output" problem
     * fails in both soft assert and extent report but continues the testing
     */
    public void softAssertCont(String message) {
        fail(message);
        softAssert.fail(message);
    }

    /**
     *
     * @param message
     *
     * solves the "errors going at the end of the output" problem
     * fails in both soft assert and extent report and stops the testing
     */
    public void softAssertStop(String message) {
        fail(message);
        softAssert.fail(message);
        softAssert.assertAll();
    }

    /**
     *
     * @param context
     * @param groupNameInitial
     * @return String
     *
     * Get the value of a parameter from xml file and set it as a variable based on the groups of the test methods
     */
    private String getParamBasedOnGroup(ITestContext context, String groupNameInitial) {
        /*
         This is being called from @BeforeMethod
         So every time it gets called there will only one test method in the context
         Get the groups of the test method in the context
        */
        for(String g: context.getAllTestMethods()[0].getGroups()) {
            if(g.startsWith(groupNameInitial))
                return context.getCurrentXmlTest().getParameter(g);
        }
        return null;
    }

    /**
     * launches browser based on the string provided by initializing the spring application context
     * Reads the preferences for the specific browser from properties file and set the browser accordingly
     * */
    public void launchBrowser(String browserName) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        WebDriverProvider webDriverProvider = applicationContext.getBean("webDriverProvider", WebDriverProvider.class);
        try {
            driver = webDriverProvider.getWebDriver(browserName);
            // setting a default explicit wait
            setExplicitWaitTime(20);

            js = (JavascriptExecutor)driver;
            act = new Actions(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the explicit wait time according to param @sectToWait
     * */
    public void setExplicitWaitTime(long secsToWait) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(secsToWait));
    }

    /**
     * param @filePath
     *
     * Check if the param has "//" in the path
     * If it does not have "//" then this is just a file name.
     * Assume the file is in resources folder.
     * Build the full path with the help of 'env' variable and return it
     *
     * Else if the path start with "//src" substring. If it does,
     * Then assume that the file is inside the project. Add the project path with it and return it.
     *
     * Else Assume  the file path is alright and return it as it is.
     * */
    public void loadPropertyFile(String filePath) {
        int lastOccurrence = filePath.lastIndexOf("//");
        if(lastOccurrence < 0)
            filePath = System.getProperty("user.dir") + "//src//test//resources//" + filePath;
        else if(filePath.substring(0, lastOccurrence).startsWith("//src"))
            filePath = System.getProperty("user.dir") + filePath;

        try {
            prop = new Properties();
            FileInputStream fs = new FileInputStream(filePath);
            prop.load(fs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProp(String key) {
        return prop.getProperty(key);
    }

    /**
     * sleep for n number of seconds passed as the parameter
     * */
    public void dynamicSleep(int time) {
        try {
            Thread.sleep(time*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitForPageToLoad(boolean includesJQuery){
        for(int i = 0; i!=10; i++){
            if(js.executeScript("return document.readyState;").equals("complete")) break; else dynamicSleep(2);
        }

        // check for jquery status
        if(includesJQuery) {
            for(int i = 0; i!=10; i++){
                if(((Long) js.executeScript("return jQuery.active;")) == 0 ) break; else dynamicSleep(2);
            }
        }
    }

    /**
     * param @locatorKey is used to find the element in the page
     * returns true if the element is present and visible
     * else returns false
     *
     * Uses explicit wait to wait for the element to to be present and visible
     * */
    public boolean isElementPresent(String locatorKey) {
        By locator = getObjectLocator(locatorKey);
        setExplicitWaitTime(10);
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        wait.until(ExpectedConditions.elementToBeClickable(locator));

        return true;
    }

    /**
     * param @locatorKey is the key of the properties file
     * a reference of the org.openqa.selenium.By class is returned depending on the ending sub-string of the @locatorKey
     * */
    public By getObjectLocator(@NotNull String locatorKey) {
        if(locatorKey.endsWith("_id")) return By.id(getProp(locatorKey));
        else if(locatorKey.endsWith("_name")) return By.name(getProp(locatorKey));
        else if(locatorKey.endsWith("_css")) return By.cssSelector(getProp(locatorKey));
        else return By.xpath(getProp(locatorKey));
    }

    /**
     * Overloaded screen shot methods
     * Both for full screen and web elements
    * */
    public void takeScreenShot(){
        takeScreenShot(null, "Screenshot image", ExtentManager.screenshotFolder);
    }

    /**
     * Overloaded screen shot methods
     * Both for full screen and web elements
     * */
    public void takeScreenShot(String title){
        if(title == null || title.equals("")) title = "Screenshot image";
        takeScreenShot(null, title, ExtentManager.screenshotFolder);
    }

    /**
     * Overloaded screen shot methods
     * Both for full screen and web elements
     * */
    public void takeScreenShot(String title, String filePath){
        if(title == null || title.equals("")) title = "Screenshot image";
        if(filePath == null || filePath.equals("")) filePath = ExtentManager.screenshotFolder;

        takeScreenShot(null, title, filePath);
    }

    /**
     * Overloaded screen shot methods
     * Both for full screen and web elements
     * */
    public void takeScreenShot(WebElement element) {
        if(element == null) takeScreenShot();
        else takeScreenShot(element, "Faulty <" + element.getTagName() + "> element", ExtentManager.screenshotFolder);
    }

    /**
     * Overloaded screen shot methods
     * Both for full screen and web elements
     * */
    public void takeScreenShot(WebElement element, String title) {
        if(title == null || title.equals("")) title = "Screenshot image";
        takeScreenShot(element, title, ExtentManager.screenshotFolder);
    }

    /**
     * Overloaded screen shot methods
     * Both for full screen and web elements
     * */
    public void takeScreenShot(WebElement element, String title, String filePath) {
        if(title == null || title.equals("")) title = "Screenshot image";
        if(filePath == null || filePath.equals("")) filePath = ExtentManager.screenshotFolder;

        // Get entire page screenshot
        File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            if(element != null) {
                BufferedImage fullImg = ImageIO.read(screenshot);
                // Get the location of element on the page
                Point point = element.getLocation();

                // Get width and height of the element
                int eleWidth = element.getSize().getWidth();
                int eleHeight = element.getSize().getHeight();

                // Crop the entire page screenshot to get only element screenshot
                BufferedImage elementScreenshot= fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
                ImageIO.write(elementScreenshot, "png", screenshot);
            }

            // Copy the element screenshot to report
            filePath = filePath.trim();
            FileUtils.copyFile(screenshot, new File(filePath));
            test.addScreenCaptureFromPath(filePath, title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mainWindow() {
        driver.switchTo().defaultContent();
    }

    /**
     * Opens a new tab or window
     * Or switches to a different window with the provided name
     * */
    public void switchWindow(String windowName) {
        windowName = windowName.trim();
        if(windowName.equalsIgnoreCase("tab") || windowName.equalsIgnoreCase("window"))
            driver.switchTo().newWindow(WindowType.fromString(windowName));
        driver.switchTo().window(windowName);
    }

    /**
     * Closes the current window and switches back to the default window
     * */
    public void closeAndSwitch() {
        driver.close();
        mainWindow();
    }

    /**
     * CLoses the current window and switches back to the window name provided
     * */
    public void closeAndSwitch(String windowName) {
        driver.close();
        switchWindow(windowName.trim());
    }

    /**
     * Vertical & Horizontal scroll
     * */
    public void scroll(double x, double y) {
        js.executeScript("window.scrollTo("+ x +","+ (y-200) +")");
    }

    /**
     * Vertical scroll
     * */
    public void scroll(double y) {
        scroll(0, y);
    }
}
