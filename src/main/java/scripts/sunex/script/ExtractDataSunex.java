package scripts.sunex.script;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import scripts.sunex.pages.LoginPage;
import scripts.sunex.pages.ProductListPage;
import scripts.sunex.pages.ProductPage;
import utils.BaseTest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExtractDataSunex extends BaseTest {


    private WebDriver driver;
    private XSSFSheet sheet;
    private int rowNo;
    private LoginPage loginPage;
    private ProductListPage productListPage;
    private ProductPage productPage;

    @BeforeMethod
    public void setUp() throws IOException {
        driver = initializeDriver();
        loginPage = new LoginPage(driver);
        productPage = new ProductPage(driver);
        productListPage = new ProductListPage(driver);
    }


    @Test
    public void extractDataSunex() throws IOException, InterruptedException {
        loginPage.login();
        String base_url = "https://sunex.ro/nou/518-preuri-speciale";
        driver.get(base_url);

        FileInputStream fs1 = new FileInputStream("src/main/java/scripts/sunex/script/sunex.xlsx");
        XSSFWorkbook workbook1 = new XSSFWorkbook(fs1);
        sheet = workbook1.getSheetAt(0);
        rowNo = 1;

        for (int page = 1; page <= 121; page++) {
            driver.get(base_url + "?page=" + page);
            int totalProductOnThePage = productListPage.takeNumberOfPorductsFromTheCurrentPage();
            Thread.sleep(5000);
            for (int i = 1; i <= totalProductOnThePage; i++) {
                driver.findElement(By.cssSelector("#box-product-grid > div > div:nth-child(" + i + ") .left-product > a")).click();
                Thread.sleep(3000);
                writeDataToSheet(rowNo, 0, productPage.getProductName());
                writeDataToSheet(rowNo, 1, productPage.getPrice());
                writeDataToSheet(rowNo, 2, productPage.getDescription());
                writeDataToSheet(rowNo, 3, productPage.extractImageSources());
                rowNo++;
                driver.navigate().back();
                Thread.sleep(5000);
            }
        }

        // Adaugă aceasta linie pentru a salva modificările
        FileOutputStream fos = new FileOutputStream("src/main/java/scripts/sunex/script/sunex.xlsx");
        workbook1.write(fos);
        fos.close();

        workbook1.close();
        fs1.close();
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


}

