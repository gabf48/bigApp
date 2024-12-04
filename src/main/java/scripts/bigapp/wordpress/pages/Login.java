package scripts.bigapp.wordpress.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.BasePage;

public class Login extends BasePage {
    public Login(WebDriver driver) {
        super(driver);
    }

    public void login() {
        driver.get("https://bigapp.ro/wp-admin");
        driver.manage().window().maximize();
        driver.findElement(By.id("user_login")).sendKeys("administrator93");
        driver.findElement(By.id("user_pass")).sendKeys("tTeLl@aUo&ln%ecnhYmNllzI");
        driver.findElement(By.id("wp-submit")).click();
    }
}
