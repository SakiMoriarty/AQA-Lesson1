import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MTSTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.mts.by/");

        try {
            WebElement acceptCookiesButton = driver.findElement(By.xpath("/html/body/div[6]/main/div/div[2]/div"));
            if (acceptCookiesButton.isDisplayed()) {
                acceptCookiesButton.click();
            }
        } catch (NoSuchElementException e) {
            System.out.println("Кнопка подтверждения Cookie отсутствует.");
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.getWindowHandle();
            driver.quit();
        }
    }

    @Test
    @DisplayName("Тайтл 'Онлайн пополнение без комиссии'")
    public void testTitle() {
        WebElement blockTitle = driver.findElement(By.xpath("//*[@id=\"pay-section\"]/div/div/div[2]/section/div/h2"));
        assertNotNull(blockTitle, "Блок с названием 'Онлайн пополнение без комиссии' должен существовать.");
    }

    @Test
    @DisplayName("Логотипы платежных систем")
    public void testPayment() {
        WebElement paymentLogoVisa = driver.findElement(By.xpath("//*[@id=\"pay-section\"]/div/div/div[2]/section/div/div[2]/ul/li[1]/img"));
        WebElement paymentLogoMasterCard = driver.findElement(By.xpath("//*[@id=\"pay-section\"]/div/div/div[2]/section/div/div[2]/ul/li[3]"));
        WebElement paymentLogoBelcart = driver.findElement(By.xpath("//*[@id=\"pay-section\"]/div/div/div[2]/section/div/div[2]/ul/li[5]/img"));
        assertNotNull(paymentLogoVisa, "Логотип Visa должен быть на странице.");
        assertNotNull(paymentLogoMasterCard, "Логотип MasterCard должен быть на странице.");
        assertNotNull(paymentLogoBelcart, "Логотип Белкарт должен быть на странице.");
    }

    @Test
    @DisplayName("Страница 'Подробнее о сервисе'")
    public void testServiceInfo() {
        WebElement serviceLink = driver.findElement(By.linkText("Подробнее о сервисе"));
        wait.until(ExpectedConditions.elementToBeClickable(serviceLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", serviceLink);
        wait.until(ExpectedConditions.urlContains("/help/poryadok-oplaty-i-bezopasnost-internet-platezhey/"));
        assertTrue(driver.getCurrentUrl().contains("/help/poryadok-oplaty-i-bezopasnost-internet-platezhey/"), "Ссылка 'Подробнее о сервисе' должна вести на страницу с информацией.");
    }

    @Test
    @DisplayName("Ввод данных в поля")
    public void testButton() {
        WebElement phoneNumber = driver.findElement(By.id("connection-phone"));
        phoneNumber.sendKeys("297777777");
        assertNotNull(phoneNumber, "Поле ;Номера телефона' должно быть на странице.");

        WebElement amount = driver.findElement(By.id("connection-sum"));
        amount.sendKeys("80");
        assertNotNull(amount, "Поле 'Сумма' должно быть на странице.");

        WebElement email = driver.findElement(By.id("connection-email"));
        email.sendKeys("test1@gmail.com");
        assertNotNull(email,"Поле 'E-mail для отправки чека' должно быть на странице");

        WebElement proceedButton = driver.findElement(By.xpath("//*[@id=\"pay-connection\"]/button"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", proceedButton);
        assertNotNull(proceedButton, "Кнопка 'Продолжить' должна быть на странице.");
    }
}
