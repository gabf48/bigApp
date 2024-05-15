package scripts.sep;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.sep.Login;
import pages.sep.Navigation;
import utils.BaseTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExtractDetails extends BaseTest {



    public Login login;
    public Navigation navigation;

    @BeforeMethod
    public void setUp() throws IOException {
        WebDriver driver = initializeDriver();
        login = new Login(driver);
        navigation = new Navigation(driver);
    }

    @Test
    public void getDetailsFromAllProducts() {
        login.loginSep();
        driver.get("https://sepmobile.ro/piese-gsm/");
        driver.findElement(By.cssSelector("#continut > div.accesorii-gsm-browse > div:nth-child(1) > div > h2 > a")).click();

            for (int i = 1; i <= 15; i++) {
                String productName = driver.findElement(By.cssSelector("#continut > div.accesorii-gsm-browse > div > h2 > a")).getText();
                System.out.println(productName);
                String descriere = driver.findElement(By.cssSelector("[class=\"accesoriu-gsm-descriere\"]")).getText() + " " + driver.findElement(By.cssSelector("[class=\"compatibilitati\"]")).getText();
                System.out.println(descriere);
                String pret = driver.findElement(By.cssSelector("[class=\"eroare\"]")).getText();
                int pret1 = Integer.parseInt(pret);
                System.out.println(pret1);
                List<WebElement> images = driver.findElements(By.cssSelector("[class=\"accesoriu-gsm-poze\"] a"));

                List<String> strings = new ArrayList<>();
                for (WebElement e : images) {
                    strings.add(e.getAttribute("href"));
                }

                System.out.println(strings);
                System.out.println();
                navigation.goNextProduct();
        }


    }
}
