import com.bat.configurations.SpringConfig;
import com.bat.webdrivers.provider.WebDriverProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainClass {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        WebDriverProvider webDriverProvider = applicationContext.getBean("webDriverProvider", WebDriverProvider.class);

        /*
          for chrome -> "Chrome"
          for firefox -> "Firefox"
          for edge -> "Edge"
         */
        String browserName = "Chrome";
        try {
            WebDriver webDriver = webDriverProvider.getWebDriver(browserName);
            webDriver.get("http://google.com");
            webDriver.quit();
        } catch (Exception exception) {
            System.out.println("FAILED TO INITIATE " + browserName.toUpperCase());
            System.out.println(exception);
        }
    }
}
