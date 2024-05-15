package pages.sep;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.BasePage;

public class Login extends BasePage {
    public Login(WebDriver driver) {
        super(driver);
    }

    By username = By.id("login_username");
    By password = By.id("login_password");
    By loginButton = By.cssSelector("[type=\"submit\"]");

    public void loginSep(){
        driver.get("https://sepmobile.ro/");
        driver.manage().window().maximize();
        writeText(username,"Dahlila");
        writeText(password,"Asociat93");
        click(loginButton);
    }
}
