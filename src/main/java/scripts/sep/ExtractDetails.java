package scripts.sep;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.sep.Login;
import pages.sep.Navigation;
import pages.sep.Price;
import utils.BaseTest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExtractDetails extends BaseTest {



    public Login login;
    public Navigation navigation;
    public Price price;

    @BeforeMethod
    public void setUp() throws IOException {
        WebDriver driver = initializeDriver();
        login = new Login(driver);
        navigation = new Navigation(driver);
        price = new Price(driver);
    }

    @Test
    public void getDetailsFromAllProducts() throws IOException, InterruptedException {
        login.loginSep();
        driver.get("https://sepmobile.ro/accesorii-gsm/folie-protectie-nanoabs/pagina-3/");
        FileInputStream fs = new FileInputStream("src/main/java/scripts/sep/sep.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fs);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowIndex = 1; // Initialize rowIndex
        for (int j = 1; j <= 2; j++) {
            for (int i = 1; i <= 6  ; i++) {
                driver.findElement(By.cssSelector("#continut > div.accesorii-gsm-browse > div:nth-child(" + i + ") > div > h2 > a")).click();
                Thread.sleep(1000);
                XSSFRow row = sheet.createRow(rowIndex); // Create a new row
                String productName = driver.findElement(By.cssSelector("#continut > div.accesorii-gsm-browse > div > h2 > a")).getText();
                System.out.println(productName);
                String descriere = driver.findElement(By.cssSelector("[class=\"accesoriu-gsm-descriere\"]")).getText() + " " + driver.findElement(By.cssSelector("[class=\"compatibilitati\"]")).getText();
                System.out.println(descriere);
                String pret = driver.findElement(By.cssSelector("[class=\"eroare\"]")).getText();

                if (pret.contains(",")) {
                    pret = pret.substring(0, pret.indexOf(","));
                } else if (pret.contains(".")) {
                    pret = pret.replace(".", "");
                }

                double pret1 = Double.parseDouble(pret);
                System.out.println(pret1);
                List<WebElement> images = driver.findElements(By.cssSelector("[class=\"accesoriu-gsm-poze\"] a"));
                List<String> imageUrls = new ArrayList<>();
                for (WebElement e : images) {
                    imageUrls.add(e.getAttribute("href"));
                }
                System.out.println(imageUrls);
                System.out.println();

                String imageUrlsString = String.join(", ", imageUrls); // Joining URLs with comma

                row.createCell(1).setCellValue(productName);
                row.createCell(2).setCellValue(descriere);
                row.createCell(3).setCellValue(price.increasePrice(pret1));
                row.createCell(4).setCellValue(imageUrlsString);

                driver.navigate().back();
                Thread.sleep(1000);
                rowIndex++; // Increment rowIndex
            }
            navigation.goNextPage();
            Thread.sleep(1000);
        }

        FileOutputStream out = new FileOutputStream("src/main/java/scripts/sep/sep.xlsx");
        workbook.write(out);
        out.close();

        // Close the workbook and file stream
        workbook.close();
        fs.close();
    }

    @Test
    public void getDetailsFromSingleProduct() throws IOException, InterruptedException {
        login.loginSep();

       driver.get("https://sepmobile.ro/accesorii-telefon/--33942/");
                String productName = driver.findElement(By.cssSelector("#continut > div.accesorii-gsm-browse > div > h2 > a")).getText();
                System.out.println(productName);
                String descriere = driver.findElement(By.cssSelector("[class=\"accesoriu-gsm-descriere\"]")).getText() + " " + driver.findElement(By.cssSelector("[class=\"compatibilitati\"]")).getText();
                System.out.println(descriere);
                String pret = driver.findElement(By.cssSelector("[class=\"eroare\"]")).getText();
        System.out.println("Pret initial = " + pret);
        if (pret.contains(",")) {
            pret = pret.substring(0, pret.indexOf(","));
        } else if (pret.contains(".")) {
            pret = pret.replace(".", "");
        }
                double pret1 = Double.parseDouble(pret);
        System.out.println("Pret dupa editare = " + pret1);
                List<WebElement> images = driver.findElements(By.cssSelector("[class=\"accesoriu-gsm-poze\"] a"));
                List<String> imageUrls = new ArrayList<>();
                for (WebElement e : images) {
                    imageUrls.add(e.getAttribute("href"));
                }

        System.out.println(price.increasePrice(pret1));

                String imageUrlsString = String.join(", ", imageUrls); // Joining URLs with comma


    }


    @Test
    public void getLinkNameAndPriceForProductsExpensiveThenSpecificAmount() throws IOException, InterruptedException {
        login.loginSep();
        Thread.sleep(3000);
        driver.get("https://sepmobile.ro/accesorii-gsm/piese/pagina-147");

        for (int j = 1; j <= 230; j++) {
            for (int i = 1; i <= 15; i++) {

                String price = driver.findElement(By.cssSelector("#continut > div.accesorii-gsm-browse > div:nth-child("+i+") > div > div > span.tag_on")).getText();
               // System.out.println("Pret initial = " + price);
                if (price.contains(",")) {
                    price = price.substring(0, price.indexOf(","));
                } else if (price.contains(".")) {
                    price = price.replace(".", "");
                }
                double pret1 = Double.parseDouble(price);
               // System.out.println("Pret dupa editare = " + pret1);

                if (pret1>=1000) {
                    System.out.println(driver.findElement(By.cssSelector("#continut > div.accesorii-gsm-browse > div:nth-child("+i+") > div > h2 > a")).getAttribute("href"));
                }


                Thread.sleep(1000);

            }
            navigation.goNextPage();
            Thread.sleep(1000);
        }

    }



}
