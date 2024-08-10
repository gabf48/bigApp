package scripts.power_x_shop;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.BaseTest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractData extends BaseTest {

    private WebDriver driver;
    private XSSFSheet sheet;
    private int rowNo;
    private int cellNo;
    private boolean newRow;

    @BeforeMethod
    public void setUp() throws IOException {
        driver = initializeDriver();
    }

    // Helper method to write data to the Excel sheet
    @Test
    public void extractDataPowerLaptop() throws InterruptedException, IOException {
        driver.get("https://powerxshop.ro/akkumulatorok-288/laptop-akkumulator-289/acer-291");
        driver.findElement(By.cssSelector("a.btn.btn-primary.nanobar-btn.js-nanobar-close-cookies")).click();
        driver.manage().window().maximize();
        Thread.sleep(5000);

        FileInputStream fs1 = new FileInputStream("src/main/java/scripts/power_x_shop/products.xlsx");
        XSSFWorkbook workbook1 = new XSSFWorkbook(fs1);
        sheet = workbook1.getSheetAt(0);

        rowNo = 1; // Initialize row number
        cellNo = 1; // Initialize cell number
        newRow = true; // Change this to false to write in a new cell

        String pagini = driver.findElement(By.cssSelector("div.sortbar.sortbar-bottom > nav > div")).getText();
        Pattern pattern = Pattern.compile("\\b(\\d+)\\s+Pagini\\b");
        Matcher matcher = pattern.matcher(pagini);
        int totalPageNumber = 0;
        if (matcher.find()) {
            totalPageNumber = Integer.parseInt(matcher.group(1));
        }

        // Loop through pages
        for (int p = 1; p <= totalPageNumber; p++) {
            if (p > 1) {
                driver.get("https://powerxshop.ro/akkumulatorok-288/laptop-akkumulator-289/acer-291?page=" + p + "#content");
                Thread.sleep(8000); // Ensure the page loads correctly
            }

            List<WebElement> productsOnThePage = driver.findElements(By.cssSelector("#snapshot_vertical > div > div > div.card-body.product-card-body > h2 > a"));
            int products = productsOnThePage.size();

            System.out.println("Sunt " + products + " produse pe pagina " + p);

            // Loop through products
            for (int j = 1; j <= products; j++) {
                System.out.println();
                Thread.sleep(3000);
                driver.findElement(By.cssSelector("#snapshot_vertical > div:nth-child(" + j + ") > div > div.card-body.product-card-body > h2 > a")).click();
                System.out.println("Produsul nr. " + j + "/" + products + " de pe pagina " + p + " a fost deschis.");
                String productTitle = driver.findElement(By.cssSelector("[class=\"product-page-product-name\"]")).getText();
                String descriere = driver.findElement(By.cssSelector("[class=\"parameter-table table m-0\"]")).getText();
                try {
                    String pret = driver.findElement(By.cssSelector("#product > div.product-cart-box > div.product-page-right-box.product-page-price-wrapper > div > meta:nth-child(2)")).getAttribute("content");
                    String pret1 = driver.findElement(By.cssSelector("#product > div.product-cart-box > div.product-page-right-box.product-page-price-wrapper > div > meta:nth-child(3)")).getAttribute("content");
                    writeDataToSheet(3, pret);
                    writeDataToSheet(4, pret1);
                } catch (Exception ignore) {
                }
                // Write product title to the sheet
                writeDataToSheet(rowNo++, 1, productTitle);
                writeDataToSheet(2, descriere);

                List<WebElement> producator = driver.findElements(By.cssSelector(".product-page-right-box.noprint > div:nth-child(1) > div > span > ul > li > a"));
                List<WebElement> capacitate = driver.findElements(By.cssSelector(".product-page-right-box.noprint > div:nth-child(2) > div > span > ul > li > a"));
                int producatori = producator.size();
                int capacitati = capacitate.size();

                if (capacitati > 1) {
                    for (int model_no = 2; model_no <= capacitati; model_no++) {
                        driver.findElement(By.cssSelector(".product-page-right-box.noprint > div:nth-child(2) > div > span > ul > li:nth-child(" + model_no + ") > a")).click();
                        Thread.sleep(2000);
                        productTitle = driver.findElement(By.cssSelector("[class=\"product-page-product-name\"]")).getText();
                        descriere = driver.findElement(By.cssSelector("[class=\"parameter-table table m-0\"]")).getText();
                        String pret = "";
                        String pret1 = "";
                        try {
                            pret = driver.findElement(By.cssSelector("#product > div.product-cart-box > div.product-page-right-box.product-page-price-wrapper > div > meta:nth-child(2)")).getAttribute("content");
                            pret1 = driver.findElement(By.cssSelector("#product > div.product-cart-box > div.product-page-right-box.product-page-price-wrapper > div > meta:nth-child(3)")).getAttribute("content");
                        } catch (Exception ignore) {
                        }
                        writeDataToSheet(3, pret);
                        writeDataToSheet(4, pret1);

                        Thread.sleep(2000);
                        System.out.println(productTitle);
                        writeDataToSheet(rowNo++, 1, productTitle);
                        writeDataToSheet(2, descriere);

                        Thread.sleep(2000);
                    }
                }

                if (producatori > 1) {
                    for (int prod_no = 2; prod_no <= producatori; prod_no++) {
                        driver.findElement(By.cssSelector(".product-page-right-box.noprint > div:nth-child(1) > div > span > ul > li:nth-child(" + prod_no + ") > a")).click();
                        capacitate = driver.findElements(By.cssSelector(".product-page-right-box.noprint > div:nth-child(2) > div > span > ul > li > a"));
                        capacitati = capacitate.size();
                        if (capacitati > 1) {
                            for (int model_no = 1; model_no <= capacitati; model_no++) {
                                driver.findElement(By.cssSelector(".product-page-right-box.noprint > div:nth-child(2) > div > span > ul > li:nth-child(" + model_no + ") > a")).click();
                                Thread.sleep(5000);
                                productTitle = driver.findElement(By.cssSelector("[class=\"product-page-product-name\"]")).getText();
                                descriere = driver.findElement(By.cssSelector("[class=\"parameter-table table m-0\"]")).getText();
                                String pret = "";
                                String pret1 = "";
                                try {
                                    pret = driver.findElement(By.cssSelector("#product > div.product-cart-box > div.product-page-right-box.product-page-price-wrapper > div > meta:nth-child(2)")).getAttribute("content");
                                    pret1 = driver.findElement(By.cssSelector("#product > div.product-cart-box > div.product-page-right-box.product-page-price-wrapper > div > meta:nth-child(3)")).getAttribute("content");
                                } catch (Exception ignore) {
                                }
                                writeDataToSheet(3, pret);
                                rowNo++;
                                writeDataToSheet(4, pret1);

                                Thread.sleep(2000);
                                writeDataToSheet(rowNo++, 1, productTitle);
                                writeDataToSheet(2, descriere);
                                Thread.sleep(2000);
                            }
                        } else {
                            Thread.sleep(2000);
                            productTitle = driver.findElement(By.cssSelector("[class=\"product-page-product-name\"]")).getText();
                            descriere = driver.findElement(By.cssSelector("[class=\"parameter-table table m-0\"]")).getText();
                            String pret = "";
                            String pret1 = "";
                            try {
                                pret = driver.findElement(By.cssSelector("#product > div.product-cart-box > div.product-page-right-box.product-page-price-wrapper > div > meta:nth-child(2)")).getAttribute("content");
                                pret1 = driver.findElement(By.cssSelector("#product > div.product-cart-box > div.product-page-right-box.product-page-price-wrapper > div > meta:nth-child(3)")).getAttribute("content");
                            } catch (Exception ignore) {
                            }
                            writeDataToSheet(3, pret);
                            writeDataToSheet(4, pret1);

                            Thread.sleep(2000);
                            writeDataToSheet(rowNo++, 1, productTitle);
                            writeDataToSheet(2, descriere);
                            Thread.sleep(2000);
                        }
                    }
                }

                // Return to the product listing page
                driver.get("https://powerxshop.ro/akkumulatorok-288/laptop-akkumulator-289/acer-291?page=" + p + "#content");
                Thread.sleep(8000); // Add a small delay to ensure the page loads correctly
            }
        }

        FileOutputStream out = new FileOutputStream("src/main/java/scripts/power_x_shop/products.xlsx");
        workbook1.write(out);
        out.close();
        fs1.close();
        workbook1.close();
    }

    private void writeDataToSheet(int rowNo, int cellNo, String value) {
        XSSFRow row = sheet.getRow(rowNo);
        if (row == null) {
            row = sheet.createRow(rowNo);
        }
        XSSFCell cell = row.getCell(cellNo);
        if (cell == null) {
            cell = row.createCell(cellNo);
        }
        cell.setCellValue(value);
    }

    private void writeDataToSheet(int cellNo, String value) {
        XSSFRow row = sheet.getRow(rowNo);
        if (row == null) {
            row = sheet.createRow(rowNo);
        }
        XSSFCell cell = row.getCell(cellNo);
        if (cell == null) {
            cell = row.createCell(cellNo);
        }
        cell.setCellValue(value);
    }
}

