import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.AskPage;
import pages.HomePage;

import java.util.List;
import java.util.NoSuchElementException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreatePollTest {
  private static Utils utils;
  private static WebDriver driver;
  private static WebDriverWait wait;
  private static HomePage homePage;
  private static AskPage askPage;

  @BeforeAll
  public static void setUp() {
    utils = new Utils();
    utils.setupDriver();
    driver = utils.getDriver();
    wait = utils.getWaitTime();

    homePage = new HomePage(driver);
    askPage = new AskPage(driver);
  }

  @AfterAll
  public static void tearDown() {
    if(utils != null){
      utils.quitDriver();
    }
  }

  public void switchToFrameWithLocator(By element) {
    List<WebElement> frames = driver.findElements(By.tagName("iframe"));
    for (WebElement frame : frames) {
      driver.switchTo().frame(frame);
      List<WebElement> elements = driver.findElements(element);

      if (!elements.isEmpty()) {
        System.out.println("Нужный frame найден!");
        return;
      }
      driver.switchTo().defaultContent();
    }
    throw new NoSuchElementException("Frame с элементом не найден!");
  }

  @Test
  @Order(1)
  public void authUser(){
    homePage.clickEnterButton();
    By username = By.xpath("//input[@placeholder='Имя аккаунта']");
    switchToFrameWithLocator(username);
    homePage.enterLoginAndChooseMailType("test_qa_lab3");
    homePage.enterNumbersAndCode("9", "5");
    WebElement codeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='Code']")));
    wait.until(driver -> codeField.getAttribute("value").length() == 8);
    System.out.println("Код введен: " + codeField.getAttribute("value"));
    homePage.submitLogin();
  }

  @Test
  @Order(2)
  public void createPollTest() {
    homePage.clickAskButton();
    askPage.createPoll();
  }
}
