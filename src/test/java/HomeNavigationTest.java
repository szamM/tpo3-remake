import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.AdvancedSearchPage;
import pages.EmployersPage;
import pages.HomePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
public class HomeNavigationTest extends BaseBrowserTest {

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void homePageOpensWithSearchField(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      HomePage homePage = new HomePage(driver).open();
      assertTrue(homePage.currentRegion().contains("Москва"));
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void advancedSearchOpens(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      AdvancedSearchPage advancedSearchPage = new HomePage(driver).open().openAdvancedSearch();
      assertTrue(advancedSearchPage.currentUrl().contains("/search/vacancy/advanced"));
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void employerCatalogOpens(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      EmployersPage employersPage = new HomePage(driver).openEmployerCatalog();
      assertTrue(employersPage.currentUrl().contains("/employers_company"));
    });
  }
}
