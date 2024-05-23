package pages.sep;

import org.openqa.selenium.WebDriver;
import utils.BasePage;

public class Price extends BasePage {
    public Price(WebDriver driver) {
        super(driver);
    }

    public double increasePrice(double price) {
        if (price <= 15) { return 35;}
        if ((price >15) && (price <=30)) {return price+price*1.5;}
        if ((price >30) && (price <=50)) {return price+price*1.1;}
        if ((price >50) && (price <=70)) {return price+price*0.9;}
        if ((price >70) && (price <=90)) {return price+price*0.8;}
        if ((price >90) && (price <=120)) {return price+price*0.7;}
        if (price >120) {return price+price*0.65;}
        return 0;
    }
}
