package scripts.piese_auto;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.BaseTest;

import java.io.IOException;
import java.util.List;

public class PieseAuto extends BaseTest{
    @BeforeMethod
    public void setUp() throws IOException {
        WebDriver driver = initializeDriver();
    }

    @Test
    public void extract() throws InterruptedException{
        //accesare link
        driver.get("https://www.pieseauto.ro/bare-portbagaj/?utilizator=auto_farm");

        //maximalizare
        driver.manage().window().maximize();

        //accept cookiuri
        Thread.sleep(4000);
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();

        //intra pe fiecare produs in parte
        List<WebElement> myElements = driver.findElements(By.cssSelector(".js-sr-item-title a"));
        System.out.println(myElements.size());
        for (int x = 0; x < 3; x++){
            List<WebElement> myElements1 = driver.findElements(By.cssSelector(".js-sr-item-title a"));
            WebElement client = myElements1.get(x);
            client.click();
            Thread.sleep(5000);

            //afiseaza numele produsului deschis
            System.out.println();
            System.out.println(driver.findElement(By.cssSelector(".prod-name-wrap h1")).getText());


            //stocheaza numarul de poze pentru produsul current si afiseaza
            List<WebElement> photos = driver.findElements(By.cssSelector("[class=\"prod-thumbs-jcarousel\"] img"));
            System.out.println("Produsul are " + photos.size() + " poze!");


            //full-screen poza de la produs
            driver.findElement(By.cssSelector(".main-image")).click();

            //afiseaza url-ul primei poze
            Thread.sleep(2000);
            System.out.println(driver.findElement(By.cssSelector("div:nth-child(2) > div > img:nth-child(2)")).getAttribute("src"));

            //incearca sa ia url-ul si pentru a 2-a poza daca exista
            try {
                //next photo
                driver.findElement(By.cssSelector("[title=\"Next (arrow right)\"]")).click();
                Thread.sleep(2000);

                //afiseaza url-ul pentru poza 2
                System.out.println(driver.findElement(By.cssSelector("div:nth-child(3) > div > img:nth-child(2)")).getAttribute("src"));

            } catch (Exception ignore){}

            //inchide fereastra full screen cu poze
            driver.navigate().back();

            //afiseaza pretul
            String pret = driver.findElement(By.cssSelector("[class=\"price-item\"]")).getText();
            String pret_0 = pret.replace(" lei","");
            double pret_2 = Double.parseDouble(pret_0);
            System.out.println(pret_2);


            //afiseaza descrierea
            System.out.println("Descriere: ");
            System.out.println(driver.findElement(By.cssSelector("[itemprop=\"description\"]")).getText());

            //descrierea tehnica
            System.out.println();
            System.out.println("Descriere tehnica: ");
            System.out.println(driver.findElement(By.cssSelector("[class=\"product-tab-content selected \"]")).getText());
            //inapoi la pagina cu produse
            driver.navigate().back();

        }

    }

}
