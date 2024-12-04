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

    // Constructor with WebDriver initialization
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofMillis(25000));
    }

    // Wait until visibility of an element located by a selector
    public void waitVisibility(By elementBy) {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(elementBy));
    }

    // Click method after waiting for visibility
    public void click(By elementBy) {
        waitVisibility(elementBy);
        driver.findElement(elementBy).click();
    }

    // Write text after waiting for visibility
    public void writeText(By elementBy, String text) {
        waitVisibility(elementBy);
        driver.findElement(elementBy).sendKeys(text);
    }

    // Assert if a given text is present on the page
    public void assertIfTextIsDisplayedOnTheScreen(String text) {
        boolean presence = driver.getPageSource().contains(text);
        Assert.assertTrue(presence, "Text '" + text + "' is not displayed on the screen.");
    }

    // Scroll to the element and click
    public void scrollToElementAndClick(By selector) {
        WebElement element = driver.findElement(selector);

        // Scroll element into view (center)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);

        // Wait for the element to be clickable
        wait.until(ExpectedConditions.elementToBeClickable(selector));

        // Now click on the element
        element.click();
    }

    // Wait for element to be clickable and then click
    public void waitForElementToBeClickableAndClick(By elementBy) {
        wait.until(ExpectedConditions.elementToBeClickable(elementBy)).click();
    }

    // Check if element is displayed on the page
    public boolean isElementDisplayed(By elementBy) {
        try {
            return driver.findElement(elementBy).isDisplayed();
        } catch (Exception e) {
            return false; // Element is not found or not displayed
        }
    }
}