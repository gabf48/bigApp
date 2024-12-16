package scripts.bigapp.wordpress.scripts;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import scripts.bigapp.wordpress.pages.Login;
import utils.BaseTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class DeleteProducts extends BaseTest {

    private Login login;
    private static final String EXCEL_FILE_PATH = "src/main/java/scripts/bigapp/bigapp/documents/produse.xlsx";


    @BeforeMethod
    public void setUp() throws IOException {
        WebDriver driver = initializeDriver();
        login = new Login(driver);
    }

    @Test
    public void deleteProductsBySKU() throws InterruptedException, IOException {
        login.login();
        Thread.sleep(10000);

        driver.get("https://bigapp.ro/wp-admin/edit.php?post_type=product");

        // Construiește calea completă relativă pornind de la directorul curent
        String fullExcelPath = System.getProperty("user.dir") + "/" + EXCEL_FILE_PATH;

        // Citește SKUs din fișierul Excel
        FileInputStream fis = new FileInputStream(fullExcelPath);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); // Skip header row
        int i = 3846;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell cell = row.getCell(0); // Column A (index 0)
            String sku = cell.getStringCellValue();

            // Search for the product SKU on the website
            driver.findElement(By.id("post-search-input")).sendKeys(sku);
            driver.findElement(By.id("search-submit")).click();
            Thread.sleep(10000);
            driver.findElement(By.cssSelector("th > input[type=checkbox]")).click();
            Thread.sleep(1500);
            String classDelete = driver.findElement(By.cssSelector(".row-actions [class=\"trash\"] a")).getAttribute("class");
            if (classDelete.equals("submitdelete")) {
            driver.findElement(By.cssSelector("[class=\"submitdelete\"]")).click();
            Thread.sleep(10000);}
            else {
                driver.findElement(By.cssSelector(".row-actions [class=\"trash\"] a")).click();
                Thread.sleep(10000);
                driver.switchTo().alert().accept(); // Apasă OK
                Thread.sleep(10000);

            }

            driver.findElement(By.id("post-search-input")).clear();
        i ++;
        int ramas = 4284 - i;
            System.out.println("Sterse " + i + " / 4284, au mai ramas de sters: " + ramas);
        }

        workbook.close();
        fis.close();
    }
}


