package Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.opentelemetry.api.incubator.trace.SpanRunnable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DynamicWebTable {
    public static void main(String[] args){
//        System.setProperty("webdriver.chrome.driver","");
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        //waits
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://coinmarketcap.com/"); //url

        //get the price of BitCoin
        String coin = "Bitcoin";
        String coinPrice = driver.findElement(By.xpath("//p[normalize-space()=\""+coin+"\"]//ancestor::td/following-sibling::td/div/span")).getText();
        System.out.println(coinPrice);

        //get the price details of first 5 coins
        List<String> list = new ArrayList<>();
        for (int i=1;i<6;i++)
        {
            String before_xpath ="//p[normalize-space()=";
            String after_xpath ="]//ancestor::td/following-sibling::td/div/span";
            list.add(driver.findElement(By.xpath(before_xpath+i+after_xpath)).getText());
        }
        for(int i=0;i<list.size();i++)
        {
            System.out.println(list.get(i));
        }

        driver.quit();


    }

}
