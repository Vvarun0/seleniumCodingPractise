package Selenium;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.Month;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalenderAutomation {

    public static void main(String[] args) throws InterruptedException {
        // Set up WebDriver (assuming ChromeDriver is in your PATH or specified)
        // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Navigate to the demo page
            driver.get("https://jqueryui.com/datepicker/");
            driver.manage().window().maximize();

            // The datepicker is inside an iframe, so switch to it
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector(".demo-frame")));

            // 1. Click on the date input field to open the calendar
            WebElement dateInputField = wait.until(ExpectedConditions.elementToBeClickable(By.id("datepicker")));
            dateInputField.click();
            System.out.println("Date input field clicked. Calendar opened.");

            // Desired Date: December 25, 2025
            LocalDate desiredDate = LocalDate.of(2025, Month.DECEMBER, 25);
            System.out.println("Attempting to select: " + desiredDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));

            // Automate date selection
            selectDateInJQueryUIDatePicker(driver, wait, desiredDate);

            // Optional: Verify the selected date
            String selectedDateValue = dateInputField.getAttribute("value");
            System.out.println("Selected date in input field: " + selectedDateValue);

            // Verify if the selected date matches the desired date
            if (selectedDateValue.equals(desiredDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")))) {
                System.out.println("Date selection successful!");
            } else {
                System.err.println("Date selection failed. Expected " + desiredDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + ", got " + selectedDateValue);
            }

            Thread.sleep(2000); // For visual confirmation

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    /**
     * Automates selecting a date in a jQuery UI Datepicker.
     * @param driver The WebDriver instance.
     * @param wait The WebDriverWait instance.
     * @param targetDate The LocalDate object representing the desired date.
     */
    public static void selectDateInJQueryUIDatePicker(WebDriver driver, WebDriverWait wait, LocalDate targetDate) {
        int targetMonth = targetDate.getMonthValue();
        int targetYear = targetDate.getYear();
        int targetDay = targetDate.getDayOfMonth();

        // Loop until the correct month and year are displayed
        while (true) {
            // Wait for the month and year elements to be visible
            WebElement currentMonthElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-datepicker-month")));
            WebElement currentYearElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-datepicker-year")));

            String currentMonthStr = currentMonthElement.getText().trim();
            int currentYear = Integer.parseInt(currentYearElement.getText().trim());

            // Convert current month string to its corresponding Month enum value
            Month currentMonthEnum = Month.valueOf(currentMonthStr.toUpperCase());
            int currentMonth = currentMonthEnum.getValue();

            // Compare current month/year with target month/year
            if (currentMonth == targetMonth && currentYear == targetYear) {
                System.out.println("Reached target month/year: " + currentMonthStr + " " + currentYear);
                break; // Correct month and year are displayed
            } else if (targetDate.isAfter(LocalDate.of(currentYear, currentMonth, 1))) {
                // Target date is in the future, click "Next"
                WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".ui-datepicker-next")));
                nextButton.click();
                System.out.println("Clicked Next. Current month: " + currentMonthStr + ", Year: " + currentYear);
            } else {
                // Target date is in the past, click "Previous"
                WebElement prevButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".ui-datepicker-prev")));
                prevButton.click();
                System.out.println("Clicked Prev. Current month: " + currentMonthStr + ", Year: " + currentYear);
            }
        }

        // Now, select the day
        // Find all day elements (a tags with class 'ui-state-default')
        // Ensure we only select days from the current month (not ui-datepicker-other-month)
        List<WebElement> days = driver.findElements(By.xpath("//table[@class='ui-datepicker-calendar']//td[not(contains(@class, 'ui-datepicker-other-month'))]/a"));

        boolean daySelected = false;
        for (WebElement dayElement : days) {
            String dayText = dayElement.getText().trim();
            if (dayText.equals(String.valueOf(targetDay))) {
                wait.until(ExpectedConditions.elementToBeClickable(dayElement)).click();
                System.out.println("Selected day: " + targetDay);
                daySelected = true;
                break;
            }
        }

        if (!daySelected) {
            System.err.println("Error: Desired day " + targetDay + " not found or not clickable in the calendar.");
        }
    }
}