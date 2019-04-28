package ru.stqa.training.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ShopCartAddRemoveProductTest extends TestBase {

    @Test
    public void addAndRemoveProducts() {
        int productsToAddCount = 2;
        for (int i = 1; i <= productsToAddCount; i++) {
            driver.get("http://localhost/litecart");
            driver.findElement(By.className("product")).click();
            //add product to the shopcart
            WebElement productCounter = driver.findElement(By.className("quantity"));
            // product size selected for products with sizes dropdown list (as yellow duck for example)
            if (isElementPresent(driver, By.name("options[Size]"))) {
                Select size = new Select(driver.findElement(By.name("options[Size]")));
                size.selectByIndex(1);
            }
            driver.findElement(By.name("add_cart_product")).click();
            //wait when shopcart product counter updated
            String expectedCount = String.valueOf(i);
            wait.until(ExpectedConditions.attributeToBe(productCounter, "textContent", expectedCount));
            String count = productCounter.getAttribute("textContent");
        }

        //remove products from the shopcart
        driver.get("http://localhost/litecart");
        driver.findElement(By.linkText("Checkout »")).click();
        //until there are elements to remove:
        while (driver.findElements(By.name("remove_cart_item")).size() > 0) {
            WebElement checkoutTableBeforeItemRemoval = driver.findElement(By.className("dataTable"));
            int itemRowsBeforeRemoval = checkoutTableBeforeItemRemoval
                    .findElements(By.className("item")).size();
            List<WebElement> elementsToRemove = driver.findElements(By.name("remove_cart_item"));
            wait.until(ExpectedConditions.elementToBeClickable(elementsToRemove.get(0))).click();
            //wait until old table disappears
            wait.until(ExpectedConditions.stalenessOf(checkoutTableBeforeItemRemoval));
            //if it's not the last element to remove and table should still exist:
            if (driver.findElements(By.name("remove_cart_item")).size() > 0) {
                WebElement checkoutTableAfterItemRemoval = driver.findElement(By.className("dataTable"));
                int itemRowsAfterRemoval = checkoutTableBeforeItemRemoval
                        .findElements(By.className("item")).size();
                Assert.assertTrue( (itemRowsBeforeRemoval - itemRowsAfterRemoval) == 1);
            } else {

            }

            checkoutSummaryTable.getSize();// stale element exception
        }

        System.out.println("test");

    }
}
