package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DebitCardTest {
    WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-dev-shm-usage");
//        options.addArguments("--no-sandbox");
//        options.addArguments("--headless");
        driver = new ChromeDriver(options);

        driver.get("http://localhost:9999");
    }

    @AfterEach
    void teardown() {
        driver.quit();
        driver = null;

    }

    // Вввод валидных данных без дефиса, флажок выставлен
    @Test
    void shouldSendValidData() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кузьма Петров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79112223344");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[type=button]")).click();
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id= 'order-success']")).getText().trim();
        assertEquals(expectedText, actualText);

    }

    // Ввод валидных данных с дефисом, флажок выставлен
    @Test
    void shouldSendValidateHyphen() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кузьма Петров-Водкин");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79112223344");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[type=button]")).click();
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id= 'order-success']")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    // Ввод фамилии/имени латиницей флажок выставлен
    @Test
    void shouldNotSendRoman() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Kuzma Petrov");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79112223344");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[type=button]")).click(); //send
        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id= name] span.input__sub")).getText().trim();
        assertEquals(expectedText, actualText);

    }

    // Поле с именем остается незаполненным
    @Test
    void shouldNotSendVoidName() {
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79112223344");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[type=button]")).click(); //send
        String expectedText = "Поле обязательно для заполнения";
        String actualText = driver.findElement(By.cssSelector("[data-test-id= name] span.input__sub")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    // Поле телефона остается незаполненным
    @Test
    void shouldNotSendVoidPhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кузьма Петров");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[type=button]")).click(); //send
        String expectedText = "Поле обязательно для заполнения";
        String actualText = driver.findElement(By.cssSelector("[data-test-id= phone] span.input__sub")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    // Ввод в поле телефона 12 цифр с +
    @Test
    void shouldNotSendInvalidPhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кузьма Петров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+791122233445");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[type=button]")).click(); //send
        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id= phone] span.input__sub")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    // Ввод в поле телефона 10 цифр с +
    @Test
    void shouldNotSendInvalidPhone1() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кузьма Петров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7911222334");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[type=button]")).click(); //send
        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id= phone] span.input__sub")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    // Ввод номера телефона из 11 цифр, без +
    @Test
    void shouldNotSendInvalidPhone2() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кузьма Петров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("79112223345");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[type=button]")).click(); //send
        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id= phone] span.input__sub")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    // Флажок согласия не выставлен
    @Test
    void NoConsentCheckbox() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кузьма Петров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79012345678");
        driver.findElement(By.cssSelector("[type=button]")).click(); //send
        String expectedText = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actualText = driver.findElement(By.cssSelector("[data-test-id= agreement].input_invalid.checkbox")).getText().trim();
        assertEquals(expectedText, actualText);
    }
}
