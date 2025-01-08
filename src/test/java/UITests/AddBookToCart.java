package UITests;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Iterator;
import java.util.Set;

public class AddBookToCart {

    private WebDriver driver;
    private WebDriverWait wait;

    // Initialize WebDriver and WebDriverWait before the test class
    @BeforeClass
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Open eBay and search for a book
    public void openEbayAndSearchForBook() {
        driver.get("https://www.ebay.com");
        WebElement searchBox = driver.findElement(By.id("gh-ac"));
        searchBox.sendKeys("book");
        searchBox.sendKeys(Keys.RETURN);
    }

    // Select the first item from the search results
    public void selectFirstItemFromSearchResults() {
        WebElement firstBook = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='srp-river-results']/ul/li[1]//div[@class='s-item__image-section']")));
        firstBook.click();
        switchToNewTab();
    }

    // Switch to the new tab after clicking the item
    public void switchToNewTab() {
        Set<String> windowHandles = driver.getWindowHandles();
        Iterator<String> iterator = windowHandles.iterator();
        String parentWindow = iterator.next();
        String childWindow = iterator.next();
        driver.switchTo().window(childWindow);
    }

    // Add the item to the cart
    public void addItemToCart() {
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@data-testid='x-atc-action']//span[text()='Add to cart']")));
        addToCartButton.click();
    }

    // Verify the book is added to the cart
    public void verifyBookInCart() {
        WebElement cartIcon = driver.findElement(By.id("gh-minicart-hover"));
        String cartText = cartIcon.getText().trim();
        Assert.assertEquals(cartText, "1", "Test failed: Expected 1 item in the cart, but found " + cartText);
    }

    // Main test method to run all the steps
    @Test
    public void testAddBookToCart() {
        try {
            openEbayAndSearchForBook();
            selectFirstItemFromSearchResults();
            addItemToCart();
            verifyBookInCart();
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    // Quit the driver after the tests
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

