package suiteA;

import base.BaseTest;
import dataproviders.SuiteAProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestA extends BaseTest {

    @Test(dataProviderClass = SuiteAProvider.class, dataProvider = "getTestData", groups = {"testerRoleUser"})
    public void testWithDataProvider(String userName, String password) {
        log("Starting test with data provider");
        log("Tester Role : " + testerRole);
        log("Browser : " + browser);
        log(userName + " ------- " + password);
        pass("Passed test with data provider");
        log("Ending test with data provider");
    }

    // Either parameter or data provider can be used, two can not be used at the same time
    @Parameters("param")
    @Test(groups = {"testerRoleAdmin"})
    public void testWithParameter(String param) {
        log("Starting test with parameter");
        log("Tester Role : " + testerRole);
        log("Browser : " + browser);
        log("----->  " + param + "  <-----");
        pass("Passed test with parameter");
        log("Ending test with parameter");
    }
}
