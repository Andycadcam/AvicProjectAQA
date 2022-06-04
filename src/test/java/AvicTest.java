import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.*;


public class AvicTest {

    private WebDriver driver;

    @BeforeTest
    public void profileSetUp() {

        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

    }

    @BeforeMethod
    public void testSetUp() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://avic.ua/");

    }

    @Test
    public void checkThatUrlContainsSearchWord() {
        driver.findElement(By.xpath("//input[@id='input_search']")).sendKeys("iPhone 13");
        // вводим в поле поиска iPhone 13
        driver.findElement(By.xpath("//button[@class='button-reset search-btn']")).click();
        // проверяем или адресная строка содержит "query=iPhone+13"
        assertTrue(driver.getCurrentUrl().contains("query=iPhone+13"));
    }

    @Test
    public void checkPageTitle() {

        assertTrue(driver
                .getTitle()
                .contentEquals("AVIC™ - удобный интернет-магазин бытовой техники и электроники в Украине. | Avic"));
        // проверяем или заголовок страницы соответствует заданному

    }

    @Test
    public void checkSelectedCheckbox() {

        WebElement gameZone = driver.findElement(By.xpath("//ul[contains(@class,'sidebar-list')]//a[contains(@href, 'game-zone')]"));
        gameZone.click(); // заходим в меню GameZone
        WebElement gamersNotebook = driver.findElement(By.xpath("//div[@class='brand-box__title']/a[contains(@href,'gejmerskij-noutbuk')]"));
        gamersNotebook.click(); // выбираем вкладку "Игровые ноутбуки"
        WebElement checkbox = driver.findElement(By.xpath("//div[@class='filter__items checkbox']/div/input[contains(@value,'gejmerskij-noutbuk')]"));

        boolean check = checkbox.isSelected(); // проверяем, или стоит галочка напротив пункта Геймерские ноутбуки
        Assert.assertEquals(check, true);

    }

    @Test
    public void checkFloatingText() {

        WebElement audioTexnika = driver.findElement(By.xpath("//ul[contains(@class,'sidebar-list')]//a[contains(@href, 'audio-texnika')]"));
        audioTexnika.click();
        // выбираем пункт меню аудиотехника
        WebElement soundSystems = driver.findElement(By.xpath("//div[@class='brand-box__title']/a[contains(@href,'kolonki')]"));
        soundSystems.click();
// выбираем "Акустические системы"
        Actions actions = new Actions(driver);
        WebElement element = driver.findElement(By.xpath("//a[@class='prod-cart__buy'][contains(@data-ecomm-cart,'Marshall Emberton')]"));
        actions.moveToElement(element).build().perform(); // переходим к одной из позиций - колонке Marshall

        String hoverMessage = driver.findElement(By.xpath("//div[@data-product='247343']/div/div[@class='description']")).getText();
        System.out.println(hoverMessage); // получаем выплывающий текст

        String expectedString = "Вид: портативная колонка / Звук: 2.0 / Мощность: 20 Вт / Диапазон: 60-20000 Гц / Подключение: беспроводное / Размеры: 68 x 160 x 76 мм";
// проверяем, или данный текст соответствует заданному
        Assert.assertEquals(hoverMessage, expectedString);


    }


    @Test
    public void checkPasswordRecoveryFormWithWrongEmail() {

        Duration duration = Duration.ofSeconds(30);
        // задаём значение задержки 30 секунд
        WebDriverWait wait = new WebDriverWait(driver, duration);
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete")); // ждём, пока страница полностью загрузится

//        WebElement element = driver.findElement(By.xpath("//div/div/i[@class='icon icon-user-big']"));
        WebElement element = driver.findElement(By.xpath("//div[@class='header-bottom__right-icon']/i[@class='icon icon-user-big']"));

        element.click();
        // выбираем кнопку "с человечком"
        WebElement forgot = driver.findElement(By.xpath("//a[@href='#js_forgotPassword']"));
        forgot.click();
        //  выбираем пункт "Я забыл пароль"
        WebElement field = driver.findElement(By.xpath("//div[@id='js_forgotPassword']//input[@class='validate']"));
        field.sendKeys("test1@gmail.com");
        // вводим заведомо неправильный e-mail
        WebElement butt = driver.findElement(By.xpath("//div[@class='form-field input-field']//button[@class='button-reset main-btn main-btn--green submit']"));
        butt.click();
        // нажимаем кнопку "Получить"
        String message = driver.findElement(By.xpath("//div[@class='col-xs-12 js_message']")).getText();
        // проверяем, или мы получили сообщение "Неверные данные авторизации."
        Assert.assertEquals(message, "Неверные данные авторизации.");
    }


    @AfterMethod(enabled = true)
    public void tearDown() {
        driver.quit();
    }

}
