package manager;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * All these methods are executed before the @AfterMethod
 */
public class MyTestNGListener implements ITestListener {
    /**
     *
     * @param context
     *
     * Invoked after all the test methods belonging to the classes inside the <test> tag have run and
     * all their Configuration methods have been called.
     */
    public void onFinish​(ITestContext context) {}

    /**
     *
     * @param context
     *
     * Invoked before running all the test methods belonging to the classes inside the <test> tag and
     * calling all their Configuration methods.
     */
    public void onStart​(ITestContext context) {}

    /**
     *
     * @param result
     *
     * Invoked each time a method fails but has been annotated with successPercentage and
     * this failure still keeps it within the success percentage requested.
     */
    public void onTestFailedButWithinSuccessPercentage​(ITestResult result) {}

    /**
     *
     * @param result
     *
     * Invoked each time a test fails due to a timeout.
     */
    public void	onTestFailedWithTimeout​(ITestResult result) {}

    /**
     *
     * @param result
     *
     * Invoked each time a test fails.
     */
    public void onTestFailure(ITestResult result) {
        ExtentTest test = (ExtentTest)result.getAttribute("testObject");
        test.log(Status.FAIL, "Inside Listener");

        System.out.println("'" + result.getMethod().getMethodName() + "' from '" + result.getClass().getName() + "' class failed");
        System.out.println("Failure message ====> " + result.getThrowable().getMessage());
    }

    /**
     * Invoked each time a test is skipped.
     */
    public void onTestSkipped() {}

    /**
     *
     * @param result
     *
     * Invoked each time before a test will be invoked.
     */
    public void	onTestStart​(ITestResult result) {}

    /**
     *
     * @param result
     *
     * Invoked each time a test succeeds.
     */
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = (ExtentTest)result.getAttribute("testObject");
        test.log(Status.PASS, "Inside Listener");

        System.out.println("'" + result.getMethod().getMethodName() + "' from '" + result.getClass().getName() + "' class passed");
    }
}
