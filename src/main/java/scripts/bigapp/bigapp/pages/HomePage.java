package scripts.bigapp.bigapp.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import utils.BasePage;

public class HomePage extends BasePage {
    public HomePage(WebDriver driver) {
        super(driver);
    }

    By searchInput = By.cssSelector("#header > div.header-wrapper > div.header-main-wrapper > div > div > div > div.et_column.et_col-xs-6.et_col-xs-offset-0.pos-static > div > div.et_element.et_b_header-search.flex.align-items-center.et-content-right.justify-content-center.mob-justify-content-.flex-basis-full.et_element-top-level.et-content-dropdown > form > div.input-row.flex.align-items-center > input.form-control");

    public void searchProduct(String searchText) {
        driver.findElement(searchInput).clear();
        writeText(searchInput,searchText);
        driver.findElement(searchInput).sendKeys(Keys.ENTER);
    }
}
