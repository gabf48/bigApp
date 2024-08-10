package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;

import java.io.IOException;

import static java.lang.System.setProperty;
import static org.apache.commons.lang3.SystemUtils.*;

public class BaseTest {

    public WebDriver driver;

    public WebDriver initializeDriver() throws IOException {

        System.setProperty("webdriver.http.factory", "jdk-http-client");

        if (!IS_OS_WINDOWS && !IS_OS_LINUX && !IS_OS_MAC) {
            throw new RuntimeException("Could not initialize browser due to unknown operating system!");
        }
        //        if (IS_OS_WINDOWS) {
        //            setProperty("webdriver.gecko.driver", "src/test/java/browsers/geckodriver.exe");
        //        }
        if (IS_OS_WINDOWS) {
            setProperty("webdriver.chrome.driver", "src\\main\\java\\browsers\\chromedriver.exe");
        }
        if (IS_OS_LINUX) {
            setProperty("webdriver.chrome.driver", "src/test/java/browsers/chromedriver");
        }
        if (IS_OS_MAC) {
            setProperty("webdriver.chrome.driver", "src/test/java/browsers/chromedriverMac");
        }

        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--allow-running-insecure-content");
        chromeOptions.addArguments("window-size=2560,1440"); // Change resolution here (e.g., 2560x1440)
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--proxy-server='direct://'");
        chromeOptions.addArguments("--proxy-bypass-list=*");
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--disable-search-engine-choice-screen");

        try {
            Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
        } catch (Exception ignore) {
        }

        // return driver = new FirefoxDriver();
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize(); // Maximize the browser window

        return driver;
    }

//    @AfterMethod
//    public void tearDown() throws InterruptedException {
//        driver.quit();
//        Thread.sleep(2500);
//    }
}