package suiteB;

import base.BaseTest;
import org.testng.annotations.Test;

public class TestB extends BaseTest {
    @Test(groups = {"testerRoleAdmin"})
    public void testB() {
        log("Starting test b");
        log("Tester Role : " + testerRole);
        log("Browser : " + browser);
        softAssertCont("failed purposefully");
        fail("FAILED TEST B");
        log("Ending test B");

        softAssert.assertAll();
    }
}
