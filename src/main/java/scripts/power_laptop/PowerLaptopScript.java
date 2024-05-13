package scripts.power_laptop;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.BaseTest;

import java.io.IOException;

public class PowerLaptopScript extends BaseTest {

    @BeforeMethod
    public void setUp() throws IOException {
        WebDriver driver = initializeDriver();
    }

    @Test
    public void extractDataPowerLaptop() throws InterruptedException {
        driver.get("https://www.powerlaptop.ro/categories/baterie-laptop/");
        Thread.sleep(5000);
    }
}
