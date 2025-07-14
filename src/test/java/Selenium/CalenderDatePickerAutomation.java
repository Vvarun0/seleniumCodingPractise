package Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CalenderDatePickerAutomation {
    private static WebDriver driver;

    public static void main(String[] args) {//https://www.tutorialspoint.com/selenium/practice/date-picker.php

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://seleniumpractise.blogspot.com/2016/08/how-to-handle-calendar-in-selenium.html");
        automateCalender("16","April","2026");
        driver.close();
    }

    private static void automateCalender(String expDay,String expMonth,String expYear) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By selectDateTextField = By.id("datepicker");
        By selectDateCalender = By.className("ui-datepicker-calendar");
        By monthDatetitle = By.className("ui-datepicker-title");
        driver.findElement(selectDateTextField).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(selectDateCalender));

        if(expMonth.equals("February") &&(Integer.parseInt(expDay))>29)
        {
            System.out.println("wrong day : "+expMonth+ " "+expDay);
        }


        String MonthYearValue = driver.findElement(monthDatetitle).getText();
        while (!(getMonthYearValue(MonthYearValue)[0].equals(expMonth) && getMonthYearValue(MonthYearValue)[1].equals(expYear))) {

            driver.findElement(By.xpath("//a[@title=\"Next\"]")).click();
            MonthYearValue = driver.findElement(monthDatetitle).getText();
//            System.out.println(MonthYearValue);
        }
        try {
            driver.findElement(By.xpath("//a[text()='" + expDay + "']"));
        }catch(Exception e)
        {
            System.out.println("wrong date : " +expMonth+ " "+expYear);
        }
    }

    private static String[] getMonthYearValue(String MonthYearValue) {
        return MonthYearValue.split(" ");
    }


}







