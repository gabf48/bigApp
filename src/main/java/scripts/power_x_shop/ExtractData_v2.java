package scripts.power_x_shop;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.CellType;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.apache.poi.ss.usermodel.Row;   // Import pentru clasa Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // Import pentru fișiere .xlsx

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.BasePage;
import utils.BaseTest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractData_v2 extends BaseTest {

    private WebDriver driver;
    private XSSFSheet sheet;
    private int rowNo;
    private BasePage basePage;

    @BeforeMethod
    public void setUp() throws IOException {
        driver = initializeDriver();
        basePage = new BasePage(driver);
    }

    // Helper method to write data to the Excel sheet
    @Test
    public void extractDataPowerLaptop() throws InterruptedException, IOException {
        login();
        String base_urle = "https://powerxshop.ro/akkumulatorok-288/laptop-akkumulator-289/dell-293";
        driver.get(base_urle);
        Thread.sleep(5000);

        FileInputStream fs1 = new FileInputStream("src/main/java/scripts/power_x_shop/products.xlsx");
        XSSFWorkbook workbook1 = new XSSFWorkbook(fs1);
        sheet = workbook1.getSheetAt(0);

        rowNo = 1; // Initialize row number
        Thread.sleep(5000);
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
                driver.get(base_urle + "?page=" + p + "#content");
                Thread.sleep(8000); // Ensure the page loads correctly
            }

            List<WebElement> productsOnThePage = driver.findElements(By.cssSelector("#snapshot_vertical > div > div > div.card-body.product-card-body > h2 > a"));
            int products = productsOnThePage.size();

            // Loop through products
            for (int j = 1; j <= products; j++) {
                Thread.sleep(5000);
                basePage.scrollToElementAndClick(By.cssSelector("#snapshot_vertical > div:nth-child(" + j + ") > div > div.card-body.product-card-body > h2 > a"));
                Thread.sleep(2000);
                clickOnFirstProd();
                clickOnFirstCap();
                String productTitle = driver.findElement(By.cssSelector("[class=\"product-page-product-name\"]")).getText();
                String descriere = driver.findElement(By.cssSelector("[class=\"parameter-table table m-0\"]")).getText();

                // Resetăm variabila 'cap' la fiecare iterație
                String cap = "";
                try {
                    WebElement elementCap = driver.findElement(By.cssSelector(".product-page-right-box.noprint > div:nth-child(2) > div > span > ul [class=\"variable selected\"]"));
                    cap = elementCap.getText();
                } catch (NoSuchElementException e1) {
                    try {
                        WebElement elementCap = driver.findElement(By.cssSelector("#product > div.product-attributes-select-box.product-page-right-box.noprint > div:nth-child(2) > div > span"));
                        cap = elementCap.getText();
                    } catch (NoSuchElementException e2) {
                        // Dacă nu este găsit niciun element în ambele cazuri
                    } catch (Exception e) {
                        // Prindem orice altă excepție neprevăzută
                    }
                } catch (Exception e) {
                    // Prindem orice altă excepție din primul bloc try
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }

                // Dacă 'cap' nu a fost setat din cauza lipsei elementului, îi putem da o valoare implicită
                if (cap.isEmpty()) {
                    cap = "N/A"; // Valoare implicită
                }


                // Write all data for this product in the same row
                writeDataToSheet(rowNo, 0, driver.getCurrentUrl());
                writeDataToSheet(rowNo, 1, productTitle);
                writeDataToSheet(rowNo, 3, descriere + "\nCapacitate: " + cap);
                writeDataToSheet(rowNo, 4, extractPrice());
                writeDataToSheet(rowNo, 6, getImages());
                writeDataToSheet(rowNo, 8, "1");

                rowNo++; // Move to the next row only after writing all data

                cap = "";

                // Handle multiple models
                List<WebElement> capacitate = driver.findElements(By.cssSelector(".product-page-right-box.noprint > div:nth-child(2) > div > span > ul > li > a"));
                int capacitati = capacitate.size();

                if (capacitati > 1) {
                    for (int model_no = 2; model_no <= capacitati; model_no++) {
                        driver.findElement(By.cssSelector(".product-page-right-box.noprint > div:nth-child(2) > div > span > ul > li:nth-child(" + model_no + ") > a")).click();
                        Thread.sleep(2000);
                        productTitle = driver.findElement(By.cssSelector("[class=\"product-page-product-name\"]")).getText();
                        descriere = driver.findElement(By.cssSelector("[class=\"parameter-table table m-0\"]")).getText();

                        try {
                            cap = driver.findElement(By.cssSelector(".product-page-right-box.noprint > div:nth-child(2) > div > span > ul [class=\"variable selected\"]")).getText();
                        } catch (NoSuchElementException e) {
                            cap = driver.findElement(By.cssSelector("#product > div.product-attributes-select-box.product-page-right-box.noprint > div:nth-child(2) > div > span")).getText();
                        }

                        // Write all data for this model in the same row
                        writeDataToSheet(rowNo, 0, driver.getCurrentUrl());
                        writeDataToSheet(rowNo, 1, productTitle);
                        writeDataToSheet(rowNo, 3, descriere + "\nCapacitate: " + cap);
                        if (productTitle.contains("Green Cell Pro Laptop Battery PA5212U-1BRS Toshiba Satellite Pro A30-C A40-C A40-C A50-C R50-B R50-C Tecra A50-C Z50-C")) {

                        } else {
                            writeDataToSheet(rowNo, 4, extractPrice());
                        }

                        writeDataToSheet(rowNo, 6, getImages());
                        writeDataToSheet(rowNo, 8, "2");
                        rowNo++; // Move to the next row only after writing all data
                        cap = "";

                    }
                }

                System.out.println("rownNo = " + rowNo);
                if (rowNo == 4) {
                    System.out.println(rowNo);
                    System.out.print("");
                }

                // Handle multiple producers
                List<WebElement> producator = driver.findElements(By.cssSelector(".product-page-right-box.noprint > div:nth-child(1) > div > span > ul > li > a"));
                int producatori = producator.size();

                if (producatori > 1) {
                    for (int prod_no = 2; prod_no <= producatori; prod_no++) {
                        driver.findElement(By.cssSelector(".product-page-right-box.noprint > div:nth-child(1) > div > span > ul > li:nth-child(" + prod_no + ") > a")).click();
                        clickOnFirstCap();
                        capacitate = driver.findElements(By.cssSelector(".product-page-right-box.noprint > div:nth-child(2) > div > span > ul > li > a"));
                        capacitati = 0;
                        capacitati = capacitate.size();
                        if (capacitati > 1) {
                            for (int model_no = 1; model_no <= capacitati; model_no++) {
                                driver.findElement(By.cssSelector(".product-page-right-box.noprint > div:nth-child(2) > div > span > ul > li:nth-child(" + model_no + ") > a")).click();
                                Thread.sleep(5000);
                                productTitle = driver.findElement(By.cssSelector("[class=\"product-page-product-name\"]")).getText();
                                descriere = driver.findElement(By.cssSelector("[class=\"parameter-table table m-0\"]")).getText();

                                // Verifică dacă elementul există înainte de a încerca să extragi textul
                                List<WebElement> capElements = driver.findElements(By.cssSelector(".product-page-right-box.noprint > div:nth-child(2) > div > span > ul [class=\"variable selected\"]"));

                                // Dacă elementul există, preia textul
                                if (!capElements.isEmpty()) {
                                    cap = capElements.get(0).getText();
                                } else {
                                    // Verifică dacă există al doilea element, folosind aceeași metodă
                                    List<WebElement> capFallback = driver.findElements(By.cssSelector("#product > div.product-attributes-select-box.product-page-right-box.noprint > div:nth-child(2) > div > span"));
                                    if (!capFallback.isEmpty()) {
                                        cap = capFallback.get(0).getText();
                                    } else {
                                        // Dacă niciunul dintre elemente nu există, poți seta cap la un string gol sau un mesaj default
                                        cap = "Capacitate indisponibilă"; // Sau lasă gol: cap = "";
                                    }
                                }

                                // Write all data for this model in the same row
                                writeDataToSheet(rowNo, 0, driver.getCurrentUrl());
                                writeDataToSheet(rowNo, 1, productTitle);
                                writeDataToSheet(rowNo, 3, descriere + "\nCapacitate: " + cap);
                                writeDataToSheet(rowNo, 4, extractPrice());
                                writeDataToSheet(rowNo, 6, getImages());
                                writeDataToSheet(rowNo, 8, "3");
                                rowNo++; // Move to the next row only after writing all data
                                cap = "";

                            }
                        } else {
                            Thread.sleep(2000);
                            productTitle = driver.findElement(By.cssSelector("[class=\"product-page-product-name\"]")).getText();
                            descriere = driver.findElement(By.cssSelector("[class=\"parameter-table table m-0\"]")).getText();

                            // Verifică dacă elementul există înainte de a încerca să extragi textul
                            List<WebElement> capElements = driver.findElements(By.cssSelector(".product-page-right-box.noprint > div:nth-child(2) > div > span > ul [class=\"variable selected\"]"));

                            // Dacă elementul există, preia textul
                            if (!capElements.isEmpty()) {
                                cap = capElements.get(0).getText();
                            } else {
                                // Verifică dacă există al doilea element, folosind aceeași metodă
                                List<WebElement> capFallback = driver.findElements(By.cssSelector("#product > div.product-attributes-select-box.product-page-right-box.noprint > div:nth-child(2) > div > span"));
                                if (!capFallback.isEmpty()) {
                                    cap = capFallback.get(0).getText();
                                } else {
                                    // Dacă niciunul dintre elemente nu există, poți seta cap la un string gol sau un mesaj default
                                    cap = "Capacitate indisponibilă"; // Sau lasă gol: cap = "";
                                }
                            }

                            // Write all data for this producer in the same row
                            writeDataToSheet(rowNo, 0, driver.getCurrentUrl());
                            writeDataToSheet(rowNo, 1, productTitle);
                            writeDataToSheet(rowNo, 3, descriere + "\nCapacitate: " + cap);
                            writeDataToSheet(rowNo, 4, extractPrice());
                            writeDataToSheet(rowNo, 6, getImages());
                            writeDataToSheet(rowNo, 8, "4");

                            rowNo++; // Move to the next row only after writing all data
                            cap = "";

                        }
                    }
                }
                // Return to the product listing page
                driver.get(base_urle + "?page=" + p + "#content");
                Thread.sleep(8000); // Add a small delay to ensure the page loads correctly
            }
        }

        FileOutputStream out = new FileOutputStream("src/main/java/scripts/power_x_shop/products.xlsx");
        workbook1.write(out);
        out.close();
        fs1.close();
        workbook1.close();
    }


    @Test
    public void extractImages() throws InterruptedException, IOException {
        // Apelăm funcția de login
        login();

        // Pauză pentru stabilizarea UI (deși nu e recomandat să folosești sleep-uri)
        Thread.sleep(5000);

        // Citim fișierul Excel
        FileInputStream fs1 = new FileInputStream("src/main/java/scripts/power_x_shop/urls.xlsx");
        XSSFWorkbook workbook1 = new XSSFWorkbook(fs1);
        sheet = workbook1.getSheetAt(0);

        // Obținem numărul total de rânduri din foaie
        int totalRows = sheet.getPhysicalNumberOfRows();

        // Iterăm prin fiecare rând
        for (int i = 0; i < totalRows; i++) {
            Row row = sheet.getRow(i);

            // Verificăm dacă rândul nu este null și extragem celula din prima coloană (index 0)
            if (row != null) {
                Cell cell = row.getCell(0);

                // Verificăm dacă celula nu este null și este de tip STRING (URL)
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String url = cell.getStringCellValue(); // Extragem URL-ul

                    // Navigăm către URL-ul respectiv
                    driver.get(url);

                    // Pauză pentru încărcarea completă a paginii
                    Thread.sleep(5000);

                    // Extragem URL-ul imaginii folosind selectorul CSS
                    String image_url = driver.findElement(By.cssSelector(".product-image-main a")).getAttribute("href");

                    // Verificăm și curățăm extensia imaginii (.jpg, .JPG, .png, .PNG)
                    image_url = cleanImageURL(image_url);

                    // Afișăm URL-ul curățat
                    System.out.println(image_url);

                    // Aici poți adăuga logica de extracție a imaginilor de pe pagină, dacă este necesar
                }
            }
        }

        // Închidem workbook-ul și fisierul de input
        workbook1.close();
        fs1.close();
    }

    // Funcție care curăță URL-ul imaginii în funcție de extensie
    private String cleanImageURL(String imageUrl) {
        // Verificăm pentru .jpg, .JPG, .png și .PNG
        int index = -1;

        // Folosim url.toLowerCase() pentru căutare case-insensitive
        if (imageUrl.toLowerCase().contains(".jpg")) {
            index = imageUrl.toLowerCase().indexOf(".jpg");
        } else if (imageUrl.toLowerCase().contains(".png")) {
            index = imageUrl.toLowerCase().indexOf(".png");
        }

        // Dacă găsim o extensie validă, păstrăm doar partea până la extensie inclusiv
        if (index != -1) {
            return imageUrl.substring(0, index + 4); // +4 pentru a include extensia (.jpg sau .png)
        }

        // Dacă nu găsim extensia, returnăm URL-ul original
        return imageUrl;
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

    private String getImages() throws InterruptedException {
        List<WebElement> imagini = driver.findElements(By.cssSelector(".product-image-outer img"));
        int imaginiNo = imagini.size();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 10 seconds timeout

        // StringBuilder to store all image URLs
        StringBuilder allImgUrls = new StringBuilder();

        for (int i = 2; i <= imaginiNo; i++) {
            // Get the new image URL after the change
            String img = driver.findElement(By.cssSelector("#product-image-link > img")).getAttribute("src");

            // Find the position of ".jpg"
            int endIndex = img.indexOf(".jpg") + 4; // +4 to include ".jpg"

            // Extract the substring up to ".jpg"
            if (endIndex != -1) {
                img = img.substring(0, endIndex);
            }

            // Append the image URL to the StringBuilder
            allImgUrls.append(img);

            // If it's not the last iteration, add a comma and space
            if (i < imaginiNo) {
                allImgUrls.append(", ");
            }

            // Get the current image URL before clicking
            String initialImg = driver.findElement(By.cssSelector("#product-image-link > img")).getAttribute("src");

            // If i is greater than 5, click the button to load more images
            if (i > 5) {
                basePage.scrollToElementAndClick(By.cssSelector("#product-image-container > div.product-images.slick-initialized.slick-slider.slick-vertical > button.slick-next.slick-arrow.slick-vertical-next-button > svg"));
                // Poți adăuga un wait pentru a te asigura că imaginile sunt încărcate înainte de a continua
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".slick-vertical > div > div > div:nth-child(" + i + ") > div > div > img")));
            }

            // Click on the next image thumbnail
            Thread.sleep(2000);
            basePage.scrollToElementAndClick(By.cssSelector(".slick-vertical > div > div > div:nth-child(" + i + ") > div > div > img"));

           Thread.sleep(2500);
        }

        // Convert the StringBuilder to a String and print it
        String allUrls = allImgUrls.toString();
        System.out.println(allUrls); // This will print all URLs separated by ", "

        return allUrls;
    }

    private String extractPrice() {
        try {
            // Extragem valoarea prețului din atributul 'content' al meta tag-ului
            String pret = driver.findElement(By.cssSelector("#product > div.product-cart-box > div.product-page-right-box.product-page-price-wrapper > div > meta:nth-child(2)"))
                    .getAttribute("content");
            return calculatePrice(pret);
        } catch (NoSuchElementException e) {
            // Dacă primul element nu este găsit, încercăm să extragem textul din al doilea meta tag
            String pret = driver.findElement(By.cssSelector("#product > div.product-cart-box > div.product-page-right-box.product-page-price-wrapper > div > meta:nth-child(3)"))
                    .getAttribute("content");
            return calculatePrice(pret);
        }
    }

    private String calculatePrice(String pret) {
        // Înlocuim virgula cu punct pentru a gestiona numerele decimale și eliminăm orice altceva în afară de cifre și punct
        pret = pret.replaceAll(",", ".").replaceAll("[^0-9.]", "");
        double price = Double.parseDouble(pret); // Convertim string-ul rezultat în double pentru a permite zecimale

        double priceWithCommission = price * 1.27; // Aplicăm comisionul de 27%

        double finalPrice;

        // Aplicăm logica de calcul în funcție de valoarea prețului cu comisionul adăugat
        if (priceWithCommission < 15) {
            finalPrice = 35;
        } else if (priceWithCommission >= 15 && priceWithCommission <= 30) {
            finalPrice = priceWithCommission * 2.5; // Adăugăm 150%
        } else if (priceWithCommission > 30 && priceWithCommission <= 50) {
            finalPrice = priceWithCommission * 2.1; // Adăugăm 110%
        } else if (priceWithCommission > 50 && priceWithCommission <= 70) {
            finalPrice = priceWithCommission * 1.9; // Adăugăm 90%
        } else if (priceWithCommission > 70 && priceWithCommission <= 90) {
            finalPrice = priceWithCommission * 1.8; // Adăugăm 80%
        } else if (priceWithCommission > 90 && priceWithCommission <= 120) {
            finalPrice = priceWithCommission * 1.7; // Adăugăm 70%
        } else {
            finalPrice = priceWithCommission * 1.65; // Adăugăm 65%
        }

        // Returnăm valoarea finală sub formă de String, eliminând zecimalele suplimentare dacă sunt zero
        return String.format(finalPrice % 1 == 0 ? "%.0f" : "%.2f", finalPrice);
    }

    private void login() {
        driver.get("https://powerxshop.ro/customer/login");
        driver.findElement(By.cssSelector("a.btn.btn-primary.nanobar-btn.js-nanobar-close-cookies")).click();
        driver.manage().window().maximize();
        driver.findElement(By.cssSelector("[id=\"email_login\"]")).sendKeys("comenzi@bigapp.ro");
        driver.findElement(By.cssSelector("[id=\"password_login\"]")).sendKeys("Asociat93");
        driver.findElement(By.cssSelector(".sr-login-row-login-button > div > div > button")).click();
    }

    private void clickOnFirstCap() {
        try {
            basePage.scrollToElementAndClick(By.cssSelector(".noprint > div:nth-child(2) > div > span > ul > li:nth-child(1) > a"));
        } catch (Exception ignore) {
        }
    }

    private void clickOnFirstProd() {
        try {
            basePage.scrollToElementAndClick(By.cssSelector(".noprint > div:nth-child(1) > div > span > ul > li:nth-child(1) > a"));
        } catch (Exception ignore) {
        }
    }


}

