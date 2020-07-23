package dataproviders;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class SuiteAProvider {
    @DataProvider
    public static Object[][] getTestData(Method method) {
        Object[][] data = null;
        if(method.getName().toLowerCase().equals("testwithdataprovider")) {
            data = new Object[2][2];
            data[0][0] = "User 1";
            data[0][1] = "Password 1";

            data[1][0] = "User 2";
            data[1][1] = "Password 2";
        }
        return data;
    }
}
