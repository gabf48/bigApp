package scripts.sep;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.sep.Login;
import pages.sep.Navigation;
import utils.BaseTest;

import java.io.IOException;

public class GetAllProductsName extends BaseTest {

    public Login login;
    public Navigation navigation;

    @BeforeMethod
    public void setUp() throws IOException {
        WebDriver driver = initializeDriver();
        login = new Login(driver);
        navigation = new Navigation(driver);
    }

    @Test
    public void extractNameFromAllProducts(){
        login.loginSep();
        driver.get("https://sepmobile.ro/piese-gsm/");

        for (int j = 1;j<=375;j++) {
            for (int i = 1; i <= 15; i++) {
                String productName = driver.findElement(By.cssSelector("#continut > div.accesorii-gsm-browse > div:nth-child(" + i + ") > div > h2 > a")).getText();
                System.out.println(productName);
                driver.findElement(By.cssSelector("#continut > div:nth-child(5) > a:nth-child(7) > i"));
            }
            navigation.goNextPage();
        }
    }
}
