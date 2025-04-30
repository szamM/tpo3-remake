import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomePage;
import pages.LeadersPage;

public class SearchLeadersTest {

  private static Utils utils;
  private static WebDriver driver;
  private static WebDriverWait wait;
  private static JavascriptExecutor js;
  private static HomePage homePage;
  private static LeadersPage leadersPage;

  @BeforeEach
  public void setUp() {
    utils = new Utils();
    utils.setupDriver();
    driver = utils.getDriver();
    wait = utils.getWaitTime();
    js = utils.getJsExecutor();

    homePage = new HomePage(driver);
    leadersPage = new LeadersPage(driver);
  }

  @AfterEach
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void searchLeadersTest() {
    homePage.clickLeadersButton();
    leadersPage.searchLeaders();
  }
}
