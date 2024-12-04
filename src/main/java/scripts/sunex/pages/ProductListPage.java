package scripts.sunex.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.BasePage;

import java.util.List;

public class ProductListPage extends BasePage {
    
    public ProductListPage(WebDriver driver) {
        super(driver);
    }


    public int takeNumberOfPorductsFromTheCurrentPage() {
        List < WebElement> products = driver.findElements(By.cssSelector("#box-product-grid .left-product > a"));
        return products.size();
    }

}
