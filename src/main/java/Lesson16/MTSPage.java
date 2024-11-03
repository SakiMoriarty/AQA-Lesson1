package Lesson16;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MTSPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By phoneNumberLocatorConnection = By.id("connection-phone");
    private By amountLocatorConnection = By.id("connection-sum");
    private By emailLocatorConnection = By.id("connection-email");
    private By phoneNumberLocatorHomeInternet = By.id("internet-phone");
    private By amountLocatorHomeInternet = By.id("internet-sum");
    private By emailLocatorHomeInternet = By.id("internet-email");
    private By numberLocatorInstalment = By.id("score-instalment");
    private By amountLocatorInstalment = By.id("instalment-sum");
    private By emailLocatorInstallment = By.id("instalment-email");
    private By numberLocatorArrears = By.id("score-arrears");
    private By amountLocatorArrears = By.id("arrears-sum");
    private By emailLocatorArrears = By.id("arrears-email");

    private By cardNumberPlaceholderLocator = By.cssSelector("input[formcontrolname='creditCard']");
    private By cardExpirationLocator = By.cssSelector("input[formcontrolname='expirationDate']");
    private By cardCvcLocator = By.cssSelector("input[formcontrolname='cvc']");

    private By proceedButtonLocator = By.xpath("//*[@id='pay-connection']/button");
    private By paymentOptionsDropdownLocator  = By.id("pay");
    private By serviceLinkLocator = By.linkText("Подробнее о сервисе");

    public MTSPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void acceptCookies() {
        try {
            WebElement acceptCookiesButton = driver.findElement(By.id("cookie-agree"));
            if (acceptCookiesButton.isDisplayed()) {
                acceptCookiesButton.click();
            }
        } catch (NoSuchElementException e) {
            System.out.println("Кнопка подтверждения Cookie отсутствует.");
        }
    }

    public String[] getPlaceholders(String option) {
        selectPaymentOption(option);
        switch (option) {
            case "Услуги связи":
                return new String[]{
                        driver.findElement(phoneNumberLocatorConnection).getAttribute("placeholder"),
                        driver.findElement(amountLocatorConnection).getAttribute("placeholder"),
                        driver.findElement(emailLocatorConnection).getAttribute("placeholder")
                };
            case "Домашний интернет":
                return new String[]{
                        driver.findElement(phoneNumberLocatorHomeInternet).getAttribute("placeholder"),
                        driver.findElement(amountLocatorHomeInternet).getAttribute("placeholder"),
                        driver.findElement(emailLocatorHomeInternet).getAttribute("placeholder")
                };
            case "Рассрочка":
                return new String[]{
                        driver.findElement(numberLocatorInstalment).getAttribute("placeholder"),
                        driver.findElement(amountLocatorInstalment).getAttribute("placeholder"),
                        driver.findElement(emailLocatorInstallment).getAttribute("placeholder")
                };
            case "Задолженность":
                return new String[]{
                        driver.findElement(numberLocatorArrears).getAttribute("placeholder"),
                        driver.findElement(amountLocatorArrears).getAttribute("placeholder"),
                        driver.findElement(emailLocatorArrears).getAttribute("placeholder")
                };
            default:
                return new String[]{};
        }
    }

    public List<String> getPartnerLogoAltTexts() {
        List<WebElement> logos = driver.findElements(By.xpath("//img[contains(@alt, 'Visa') or contains(@alt, 'Verified By Visa') or" +
                " contains(@alt, 'MasterCard') or contains(@alt, 'MasterCard Secure Code') or contains(@alt, 'Белкарт')]"));
        List<String> altTexts = new ArrayList<>();
        for (WebElement logo : logos) {
            altTexts.add(logo.getAttribute("alt"));
        }
        return altTexts;
    }

    public void selectPaymentOption(String optionText) {
        WebElement dropdownElement = driver.findElement(paymentOptionsDropdownLocator);
        Select dropdown = new Select(dropdownElement);
        dropdown.selectByVisibleText(optionText);
        WebElement selectedOption = dropdown.getFirstSelectedOption();
        String dataOpenId = selectedOption.getAttribute("data-open");
        By formLocator = By.id(dataOpenId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(formLocator));
    }

    public void enterPhoneNumber(String phoneNumber) {
        driver.findElement(phoneNumberLocatorConnection).sendKeys(phoneNumber);
    }

    public void enterAmount(String amount) {
        driver.findElement(amountLocatorConnection).sendKeys(amount);
    }

    public void enterEmail(String email) {
        driver.findElement(emailLocatorConnection).sendKeys(email);
    }

    public void clickProceedButton() {
        WebElement proceedButton = driver.findElement(proceedButtonLocator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", proceedButton);
    }

    public String getDisplayedPhoneNumber() {
        return driver.findElement(phoneNumberLocatorConnection).getAttribute("value");
    }

    public String getDisplayedAmount() {
        return driver.findElement(amountLocatorConnection).getAttribute("value");
    }

    public boolean isServicePageOpened() {
        wait.until(ExpectedConditions.urlContains("/help/poryadok-oplaty-i-bezopasnost-internet-platezhey/"));
        return driver.getCurrentUrl().contains("/help/poryadok-oplaty-i-bezopasnost-internet-platezhey/");
    }

    public void verifyEmptyFields() {
        assertTrue(driver.findElement(phoneNumberLocatorConnection).getAttribute("value").isEmpty(), "Поле 'Номер телефона' должно быть пустым.");
        assertTrue(driver.findElement(amountLocatorConnection).getAttribute("value").isEmpty(), "Поле 'Сумма' должно быть пустым.");
        assertTrue(driver.findElement(emailLocatorConnection).getAttribute("value").isEmpty(), "Поле 'Email' должно быть пустым.");
    }

    public void clickServiceLink() {
        WebElement serviceLink = driver.findElement(serviceLinkLocator);
        wait.until(ExpectedConditions.elementToBeClickable(serviceLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", serviceLink);
    }

    //Не робит, все перепробовал(
    /*public String[] getCardPlaceholders() {
        WebElement cardNumberField = wait.until(ExpectedConditions.visibilityOfElementLocated(cardNumberPlaceholderLocator));
        WebElement expirationField = wait.until(ExpectedConditions.visibilityOfElementLocated(cardExpirationLocator));
        WebElement cvcField = wait.until(ExpectedConditions.visibilityOfElementLocated(cardCvcLocator));

        return new String[]{
                cardNumberField.getAttribute("placeholder") != null ? cardNumberField.getAttribute("placeholder") : "Отсутствует",
                expirationField.getAttribute("placeholder") != null ? expirationField.getAttribute("placeholder") : "Отсутствует",
                cvcField.getAttribute("placeholder") != null ? cvcField.getAttribute("placeholder") : "Отсутствует"
        };
    }*/
}
