import org.junit.jupiter.api.*;
import java.util.List;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.qameta.allure.junit5.AllureJunit5;
import Lesson16.MTSPage;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AllureJunit5.class)
public class MTSTest {
    private WebDriver driver;
    private MTSPage mtsPage;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        mtsPage = new MTSPage(driver);
        driver.get("https://www.mts.by/");
        mtsPage.acceptCookies();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

   @Test
   @DisplayName("Проверка плейсхолеров для всех услуг")
    public void testPlaceholdersForEach() {
       String[][] expectedPlaceholders = {
               {"Номер телефона", "Сумма", "E-mail для отправки чека"},
               {"Номер абонента", "Сумма", "E-mail для отправки чека"},
               {"Номер счета на 44", "Сумма", "E-mail для отправки чека"},
               {"Номер счета на 2073", "Сумма", "E-mail для отправки чека"}
       };
       String[] paymentOptions = {"Услуги связи", "Домашний интернет", "Рассрочка", "Задолженность"};

       for (int i = 0; i < paymentOptions.length; i++) {
           String[] actualPlaceholders = mtsPage.getPlaceholders(paymentOptions[i]);
           assertArrayEquals(expectedPlaceholders[i], actualPlaceholders,
                   "Плейсхолдеры полей для опции '" + paymentOptions[i] + "' не совпадают с ожидаемыми значениями.");
       }}

    @Test
    @DisplayName("Проверка отображения логотипов платежных систем")
    public void testPaymentLogos() {
        String[] expectedAltTexts = {"Visa", "Verified By Visa", "MasterCard", "MasterCard Secure Code", "Белкарт", "MasterCard", "Белкарт"};
        assertArrayEquals(expectedAltTexts, mtsPage.getPartnerLogoAltTexts().toArray(), "Список alt текстов не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Проверка ссылки на страницу 'Подробнее о сервисе'")
    public void testServiceInfo() {
        mtsPage.clickServiceLink();
        assertTrue(mtsPage.isServicePageOpened(), "Ссылка 'Подробнее о сервисе' должна вести на страницу с информацией.");
    }

    @Test
    @DisplayName("Проверка формы 'Услуги связи' и окна подтверждения")
    public void testConnectionServiceFormWithConfirmation() {
        mtsPage.selectPaymentOption("Услуги связи");
        mtsPage.enterPhoneNumber("297777777");
        mtsPage.enterAmount("80");
        mtsPage.enterEmail("test1@gmail.com");
        mtsPage.clickProceedButton();

        String expectedPhoneNumber = "(29)777-77-77";
        String expectedAmount = "80";
        
        //Закомител часть с проверкой холдеров
        String[] expectedCardPlaceholders = {"Номер карты", "MM/YY", "CVC"};
        //String[] actualCardPlaceholders = mtsPage.getCardPlaceholders();

        assertEquals(expectedPhoneNumber, mtsPage.getDisplayedPhoneNumber(), "Номер телефона не совпадает.");
        assertEquals(expectedAmount, mtsPage.getDisplayedAmount(), "Сумма не совпадает.");
        //assertArrayEquals(expectedCardPlaceholders, actualCardPlaceholders, "Плейсхолдеры не совпадают.");

        List<String> expectedIcons = List.of("Visa", "MasterCard", "Белкарт");
        List<String> actualIcons = mtsPage.getPartnerLogoAltTexts();
        for (String expectedIcon : expectedIcons) {
            assertTrue(actualIcons.contains(expectedIcon), "Иконка платёжной системы '" + expectedIcon + "' не отображается.");
        }
    }

    @Test
    @DisplayName("Проверка полей для каждого варианта услуг")
    public void testFieldsForEachPaymentOption() {
        String[] paymentOptions = {"Услуги связи", "Домашний интернет", "Рассрочка", "Задолженность"};

        for (String option : paymentOptions) {
            mtsPage.selectPaymentOption(option);
            mtsPage.verifyEmptyFields();
        }
    }
}
