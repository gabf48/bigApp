package scripts.sunex.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.BasePage;

import java.util.List;

public class ProductPage extends BasePage {

    public ProductPage(WebDriver driver) {
        super(driver);
    }


    public String getProductName() {
        return driver.findElement(By.cssSelector("#main > div.row > div.col-xs-12.col-lg-7 > div > h4")).getText();
    }

    public String getPrice() {
        // Preluăm textul prețului de pe pagina
        String priceText = driver.findElement(By.cssSelector("#main > div.row > div.col-xs-12.col-lg-7 > div > div.product-prices > div.product-price.h5 > div > span")).getText();

        // 1. Ștergem orice punct din preț (dacă există)
        priceText = priceText.replace(".", "");

        // 2. Înlocuim toate virgulele cu puncte
        priceText = priceText.replace(",", ".");

        // 3. Ștergem textul "RON" și orice spații suplimentare
        priceText = priceText.replace(" RON", "").trim();

        // 4. Convertim textul prețului la un double
        double price = Double.parseDouble(priceText);

        // 5. Aplicăm adaosuri la preț în funcție de interval
        if (price < 15) {
            price = 35; // Dacă prețul este sub 15 lei, setăm prețul la 35 lei
        } else if (price >= 15 && price <= 30) {
            price = price * 2.50; // Adaugăm 150% (prețul * 2.5)
        } else if (price > 30 && price <= 50) {
            price = price * 2.10; // Adaugăm 110% (prețul * 2.1)
        } else if (price > 50 && price <= 70) {
            price = price * 1.90; // Adaugăm 90% (prețul * 1.9)
        } else if (price > 70 && price <= 90) {
            price = price * 1.80; // Adaugăm 80% (prețul * 1.8)
        } else if (price > 90 && price <= 120) {
            price = price * 1.70; // Adaugăm 70% (prețul * 1.7)
        } else if (price > 120) {
            price = price * 1.65; // Adaugăm 65% (prețul * 1.65)
        }

        // 6. Formatează prețul ajustat cu 2 zecimale și returnează-l
        return String.format("%.2f", price);
    }



    public String getDescription() {
        return driver.findElement(By.cssSelector("#description .product-description")).getText();
    }

    public String extractImageSources() {
        // Găsește toate elementele img care se potrivesc cu selectorul
        List<WebElement> imageElements = driver.findElements(By.cssSelector("#thumbnails > div > ul > li > img"));

        // Creează un StringBuilder pentru a construi rezultatul
        StringBuilder imageSources = new StringBuilder();

        // Parcurge lista și extrage atributul "data-image-large-src"
        for (WebElement imgElement : imageElements) {
            String src = imgElement.getAttribute("data-image-large-src");

            // Verifică dacă atributul există (nu este null)
            if (src != null && !src.isEmpty()) {
                // Adaugă atributul extras în lista separată prin virgule
                imageSources.append(src).append(", ");
            }
        }

        // Șterge ultima virgulă și spațiu, dacă sunt adăugate
        if (imageSources.length() > 0) {
            imageSources.setLength(imageSources.length() - 2);
        }

        // Returnează lista de surse ca un singur string
        return imageSources.toString();
    }

}
