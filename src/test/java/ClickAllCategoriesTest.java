import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import pages.HomePage;

public class ClickAllCategoriesTest {

  private static WebDriver driver;
  private static HomePage homePage;

  @BeforeEach
  public void setUp() {
    Utils utils = new Utils();
    utils.setupDriver();
    driver = utils.getDriver();

    homePage = new HomePage(driver);
  }

  @AfterEach
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void clickCategoriesTest() {
    homePage.categoriesClicker();
  }
}
