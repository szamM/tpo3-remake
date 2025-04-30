import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomePage;
import pages.SearchPage;

public class SearchQuestionTest {

  private static Utils utils;
  private static WebDriver driver;
  private static WebDriverWait wait;
  private static JavascriptExecutor js;
  private static HomePage homePage;
  private static SearchPage searchPage;

  @BeforeEach
  public void setUp() {
    utils = new Utils();
    utils.setupDriver();
    driver = utils.getDriver();
    wait = utils.getWaitTime();
    js = utils.getJsExecutor();

    homePage = new HomePage(driver);
    searchPage = new SearchPage(driver);
  }

  @AfterEach
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void searchQuestionTest() {
    homePage.searchQuestion();
    searchPage.chooseQuestion();
  }
}
