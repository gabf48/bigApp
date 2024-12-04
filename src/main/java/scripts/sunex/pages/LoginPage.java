package scripts.sunex.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.BasePage;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login() {
        driver.get("https://sunex.ro/nou/");
        driver.manage().window().maximize();
        driver.findElement(By.id("email")).sendKeys("comenzi@bigapp.ro");
        driver.findElement(By.id("passwd")).sendKeys("sunex");
        driver.findElement(By.id("SubmitLogin")).click();
    }
}
