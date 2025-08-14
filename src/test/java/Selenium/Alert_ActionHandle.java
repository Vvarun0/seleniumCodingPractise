package Selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class Alert_ActionHandle {
    private static WebDriver driver;

    @BeforeClass
    public void setUp(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    }

    @AfterClass
    public void tearDown(){
        if(driver != null)
            driver.quit();
    }

    @Test
    public void test_Actions() throws InterruptedException {
        driver.get("https://www.amazon.com/");
        driver.manage().window().maximize();

        //locators
        By continueShoppingBtn = By.xpath("//button[contains(text(),'Continue shopping')]");
        By alertDialog = By.xpath("//*[@role='alertdialog']");
        By alertDialogDismissBtn =  By.xpath("//*[@role='alertdialog']//input[@data-action-type='DISMISS']");
        By searchBarDialog = By.id("twotabsearchtextbox");

        driver.findElement(continueShoppingBtn).click();

        //Handle Alert Modal
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(alertDialog));
        driver.findElement(alertDialog).findElement(alertDialogDismissBtn).click();

        //Handle Actions
        Actions actions = new Actions(driver);
        WebElement searchBar = driver.findElement(searchBarDialog);
        Thread.sleep(2);
        actions.moveToElement(searchBar).click().keyDown(Keys.SHIFT).sendKeys("Box").build().perform();


    }



}
