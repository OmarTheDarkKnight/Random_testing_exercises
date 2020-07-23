package exercises;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SakraWorldExercise extends BaseTest {
    @Test
    public void appointmentTest() {
        String value = "";
        launchBrowser("chrome");
        loadPropertyFile("sakra_world_appointment.properties");
        driver.get(getProp("url"));
        waitForPageToLoad(true);
        log("Went to the site : " + getProp("url"));

        // Click on a Doctor name
        value = getProp("doctor_name");
        driver.findElement(By.linkText(value)).click();
        log("Clicked on " + value + "'s name");

        // wait for close button of the modal to appear
        // that way we can be sure that the modal is properly displayed and can be interacted
        wait.until(ExpectedConditions.visibilityOfElementLocated(getObjectLocator("modal_close_xpath")));
        log("Appointment modal opened properly.");

        // Validate the presence and visibility of Name field
        if(!isElementPresent("name_xpath")) {
            takeScreenShot("Name filed not available");
            softAssertStop("Name field no present");
        }

        // Enter name
        value = getProp("first_name");
        driver.findElement(getObjectLocator("name_xpath")).sendKeys(value);
        log("Entered name : " + value);

        // Enter email
        value = getProp("email");
        driver.findElement(getObjectLocator("email_xpath")).sendKeys(value);
        log("Entered email : " + value);

        // Enter mobile number
        value = getProp("phone_number");
        driver.findElement(getObjectLocator("mobile_xpath")).sendKeys(value);
        log("Entered phone number : " + value);

        // Validate the gender fields
        String[] genderOptions = getProp("expected_gender_options").split(",");
        Select genderSelect = new Select(driver.findElement(getObjectLocator("gender_id")));
        List<WebElement> pageGenderOptions = genderSelect.getOptions();

        // check the length
        if(genderOptions.length != pageGenderOptions.size()) {
            softAssertStop("Gender list is not okay.");
        }

        // check the values
        for(int  i = 0; i < genderOptions.length; i++) {
            if(!genderOptions[i].equals(pageGenderOptions.get(i).getText())) {
                softAssertStop("Gender values do not match. Expected value - " + genderOptions[i] + ", found - " + pageGenderOptions.get(i).getText());
            }
        }
        log("Gender list is good");
        value = getProp("gender");
        genderSelect.selectByVisibleText(value);
        log("Select gender " + value);

        // Validate the presence and visibility of DOB field
        if(!isElementPresent("dob_id"))
            softAssertStop("DOB field is not present/visible");
        driver.findElement(getObjectLocator("dob_id")).click();
        value = getProp("dob_val");
        selectDate(value);
        log("DOB selected successfully : " + value);

        // Check UHID functionality
        if(driver.findElement(getObjectLocator("uhid_id")).isDisplayed()) {
            takeScreenShot("UHID displayed by default");
            softAssertCont("UHID is displayed");
        }
        log("UHID not displayed by default.");

        log("Clicking on YES");
        driver.findElement(getObjectLocator("yes_radio_css")).click();
        if(!driver.findElement(getObjectLocator("uhid_id")).isDisplayed()) {
            takeScreenShot("UHID not showing after clicking YES");
            softAssertCont("UHID not displayed");
        }
        log("UHID is shown after clicking YES");

        value = getProp("uhild_val");
        driver.findElement(getObjectLocator("uhid_id")).sendKeys(value);
        log("Write UHID value - " + value);

        log("Clicking on NO");
        driver.findElement(getObjectLocator("no_radio_css")).click();
        if(driver.findElement(getObjectLocator("uhid_id")).isDisplayed()) {
            takeScreenShot("UHID showing after clicking NO");
            softAssertCont("UHID displayed");
        }
        log("UHID is hidden after clicking NO");

        log("Clicking on YES again.");
        driver.findElement(getObjectLocator("yes_radio_css")).click();
        if(!driver.findElement(getObjectLocator("uhid_id")).getAttribute("value").equals(value)) {
            softAssertStop("UHID field value is not ok");
        }
        log("UHID functionality is OK");

        // Check preferred date 1
        if(!isElementPresent("prefer_date1_id"))
            softAssertStop("Preferred Date 1 field is not present/visible");
        driver.findElement(getObjectLocator("prefer_date1_id")).click();
        value = getProp("preferred_date1_val");
        selectDate(value);
        log("Preferred Date 1 selected successfully : " + value);

        // Check preferred date 2
        if(!isElementPresent("prefer_date2_id"))
            softAssertStop("Preferred Date 2 field is not present/visible");
        driver.findElement(getObjectLocator("prefer_date2_id")).click();
        value = getProp("preferred_date2_val");
        selectDate(value);
        log("Preferred Date 2 selected successfully : " + value);

        pass("Appointment test passed successfully");
        driver.quit();
        softAssert.assertAll();
    }

    public void selectDate(String dateVal) {
        try{
            SimpleDateFormat MMMM__yyyy = new SimpleDateFormat("MMMM yyyy");

            Date dateToSelect = new SimpleDateFormat("dd-MM-yyyy").parse(dateVal); // parse date from properties file
            String dayToSelect = new SimpleDateFormat("d").format(dateToSelect); // get only the day from that date
            Date monthYearToSelectDate = MMMM__yyyy.parse(MMMM__yyyy.format(dateToSelect)); // get the month and year from that date

            String monthYearDisplayed = driver.findElement(getObjectLocator("month_year_css")).getText(); // get month abd year from page
            Date mothYearOnPage = MMMM__yyyy.parse(monthYearDisplayed); // convert it to Date from String
            int dateCompare = monthYearToSelectDate.compareTo(mothYearOnPage); // compare the date

            while(dateCompare != 0) {
                if(dateCompare == 1) {
                    // the date to select is in future. So go forward
                    driver.findElement(getObjectLocator("calendar_forward_xpath")).click();
                } else if(dateCompare == -1) {
                    // the date to select is in future. So go back
                    driver.findElement(getObjectLocator("calendar_back_xpath")).click();
                }
                monthYearDisplayed = driver.findElement(getObjectLocator("month_year_css")).getText();
                mothYearOnPage = MMMM__yyyy.parse(monthYearDisplayed);
                dateCompare = monthYearToSelectDate.compareTo(mothYearOnPage);
            }
            // click on day
            driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/descendant::a[text()='" + dayToSelect + "']")).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
