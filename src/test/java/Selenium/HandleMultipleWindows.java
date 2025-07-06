package Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HandleMultipleWindows {
    private static WebDriver driver;

    public static void main(String[] args) {
//        System.setProperty("webdriver.chrome.driver","");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        //waits
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://ultimateqa.com/dummy-automation-websites/");
        driver.findElement(By.xpath("//a[@title='Follow on LinkedIn']")).click();
        driver.findElement(By.xpath("//a[@title='Follow on X']")).click();
        driver.findElement(By.xpath("//a[@title='Follow on Facebook']")).click();

        ////a[normalize-space()='Ultimate QA HTML Elements']
        //*[@id="2_Ultimate_QA_HTML_Elements"]/a
        String targetElement = "2_Ultimate_QA_HTML_Elements";
        String foundInWindowHandle = null;
        String parentWindowHandle = driver.getWindowHandle();
        Set<String> windowsSet = driver.getWindowHandles();
        List<String> windowList = new ArrayList<>(windowsSet);
        findElementOnWindows(targetElement, windowList, parentWindowHandle);


    }

    public static void findElementOnWindows(String targetElement, List<String> windowList, String parentWindowHandle) {
        for (String str : windowList) {
            driver.switchTo().window(str);
            try {
                WebElement targetFound = driver.findElement(By.xpath("//*[@id='" + targetElement + "']/a"));
                if (targetFound.isDisplayed()) {
                    System.out.println("The target element is found at : " + driver.getTitle() + " and :" + driver.getCurrentUrl());
                    closeWindow(parentWindowHandle, str);
                    break;
                }
            } catch (org.openqa.selenium.NoSuchElementException e) {
                System.out.println("Element NOT found in window: ");
            } catch (Exception e) {
                System.err.println("An error occurred while checking window " + driver.getTitle() + ": " + e.getMessage());
            }
        }
    }

    public static void closeWindow(String parentWindowHandle, String foundInWindowHandle) {
        if (foundInWindowHandle != null) {
            if (foundInWindowHandle.equals(parentWindowHandle)) {
                System.out.println("the element is on parent window , no need to close it ");
                driver.switchTo().window(parentWindowHandle);
            } else {
                System.out.println("the element is on non parent window, closing it");
                driver.switchTo().window(foundInWindowHandle);
                driver.close();
            }
        } else {
            System.out.println("The target element was NOT found in any of the opened windows.");
            driver.switchTo().window(parentWindowHandle);
        }
        System.out.println("closing all browser windows");
        driver.quit();

    }
}

