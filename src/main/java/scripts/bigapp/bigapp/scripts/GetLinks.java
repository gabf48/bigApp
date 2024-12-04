package scripts.bigapp.bigapp.scripts;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import scripts.bigapp.bigapp.pages.HomePage;
import utils.BaseTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class GetLinks extends BaseTest {

    private HomePage homePage;
    private WebDriver driver;
    private static final String EXCEL_FILE_PATH = "D:\\bigApp\\src\\main\\java\\scripts\\bigapp\\bigapp\\documents\\produse.xlsx";

    @BeforeMethod
    public void setUp() throws IOException {
        driver = initializeDriver();
        homePage = new HomePage(driver);
    }

    @Test
    public void deleteProductsBySKU() throws IOException {
        driver.get("https://bigapp.ro/");
        driver.manage().window().maximize();

        // Read SKUs from Excel file and search for each
        FileInputStream fis = new FileInputStream(EXCEL_FILE_PATH);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); // Skip header row

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell cell = row.getCell(0); // Column A (index 0)
            String sku = cell.getStringCellValue();

            // Search for the product SKU on the website
            homePage.searchProduct(sku);
            System.out.println(driver.getCurrentUrl());
            driver.navigate().back();
        }

        workbook.close();
        fis.close();
    }
}
