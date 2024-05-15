package pages.sep;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.BasePage;

public class Navigation extends BasePage {
    public Navigation(WebDriver driver) {
        super(driver);
    }

    By nextPage = By.cssSelector("#continut > div:nth-child(5) > a:nth-child(7) > i");
    By nextProduct = By.cssSelector("[class=\"fa fa-step-forward\"]");

    public void goNextPage(){
        click(nextPage);
    }

    public void goNextProduct() {
        click(nextProduct);
    }
}
