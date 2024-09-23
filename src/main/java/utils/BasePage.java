package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class BasePage {

    public WebDriver driver;
    public WebDriverWait wait;

    // Constructor
    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofMillis(10000));
    }

    // Wait Wrapper Method
    public void waitVisibility(By elementBy) {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(elementBy));
    }

    // Click Method
    public void click(By elementBy) {
        waitVisibility(elementBy);
        driver.findElement(elementBy).click();
    }

    // Write Text
    public void writeText(By elementBy, String text) {
        waitVisibility(elementBy);
        driver.findElement(elementBy).sendKeys(text);
    }

    // assert if a text is displayed on the page
    public void assertIfTextIsDisplayedOnTheScreen(String text) {
        boolean presence = false;

        if (driver.getPageSource().contains(text)) {
            presence = true;
        }
        Assert.assertTrue(presence);
    }

    public void scrollToElementAndClick(By selector) {
        WebElement element = driver.findElement(selector);

        // Scrollează astfel încât elementul să fie în centrul paginii
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);

        // Poți adăuga o mică pauză dacă este nevoie, pentru a te asigura că pagina s-a scrollat complet
        try {
            Thread.sleep(500); // pauză de 500ms (opțional)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Acum dă click pe element
        element.click();
    }

}
