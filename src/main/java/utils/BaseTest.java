package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static java.lang.System.setProperty;
import static org.apache.commons.lang3.SystemUtils.*;

public class BaseTest {

    public static WebDriver driver;


    public static WebDriver initializeDriver() {
        if (!IS_OS_WINDOWS && !IS_OS_LINUX && !IS_OS_MAC) {
            throw new RuntimeException("Could not initialize browser due to unknown operating system!");
        }
        if (IS_OS_WINDOWS) {
            setProperty("webdriver.chrome.driver", "src/main/java/browsers/chromedriver.exe");
        }
        if (IS_OS_LINUX) {
            setProperty("webdriver.chrome.driver", "src/main/java/browsers/chromedriver");
        }
        if (IS_OS_MAC) {
            setProperty("webdriver.chrome.driver", "src/main/java/browsers/chromedriverMac");
        }

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--allow-running-insecure-content");
        chromeOptions.addArguments("window-size=1920x1080");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--proxy-server='direct://'");
        chromeOptions.addArguments("--proxy-bypass-list=*");
        chromeOptions.addArguments("--ignore-certificate-errors");

//        try {
//            Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
//        } catch (Exception e) {
//        }

        return driver = new ChromeDriver();
    }

//    @AfterMethod
//    public void tearDown() {
//        driver.quit();
//    }

}
