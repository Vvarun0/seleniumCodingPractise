package Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class RunSeleniumOnOpenBrowser {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
//        System.setProperty("webdriver.chrome.driver","path");
        //run cmd -> chrome.exe --remote-debugging-port=9222 --user-data-dir=pathOfUserNewFolderToSaveData
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress","localhost:9222");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();


        driver.get("https://www.google.com/"); //url
    }
    }
