package Selenium;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public class Check_SSL_Cookie_BrokenLinks_Screenshot {
    private static WebDriver driver;

    @BeforeClass
    public void setUp(){
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        driver= new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

    }
    @AfterClass
    public void tearDown(){
        if(driver!=null)
            driver.quit();

    }

    @Test
    public void test_SSLCertificates(){
        driver.get("https://expired.badssl.com/");
        System.out.println(driver.getTitle());
    }

    @Test
    public void DeleteCookies(){
        driver.get("https://expired.badssl.com/");
        driver.manage().deleteAllCookies();
        System.out.println(driver.getTitle());
    }

    @Test
    public void test_Screenshot() throws IOException {
        driver.get("https://google.com/");
       File scr =  ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
       String projectPath = System.getProperty("user.dir");
       FileUtils.copyFile(scr,new File(projectPath+"src/test/java/Screenshots/screenshot.png"));
    }

    @Test
    public void test_BrokenLinks() throws IOException {
        By linksLocator = By.cssSelector("li[class='gf-li'] a");
        driver.get("https://rahulshettyacademy.com/AutomationPractice/");
        List<WebElement> links = driver.findElements(linksLocator);

        SoftAssert softAssert = new SoftAssert();

        for(WebElement link :links)
        {
            String url= link.getAttribute("href");
            HttpURLConnection conn =(HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("HEAD");
            conn.connect();
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code : "+responseCode);
//            Assert.assertTrue(responseCode<400,"The link with Text "+link.getText()+" is broken with code : "+responseCode);

            softAssert.assertTrue(responseCode<400,"The link with Text "+link.getText()+" is broken with code : "+responseCode);
        }
        softAssert.assertAll();

    }

}
