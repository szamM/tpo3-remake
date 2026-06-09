import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.AdvancedSearchPage;
import pages.EmployersPage;
import pages.HomePage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
public class HomeNavigationTest extends BaseBrowserTest {

  private static final String EXISTING_CITY = "Санкт-Петербург";
  private static final String NON_EXISTING_CITY = "ГородКоторогоНет123124";

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void homePageOpensWithSearchField(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      HomePage homePage = new HomePage(driver).open();
      assertTrue(homePage.hasVacancySearchField());
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

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void existingRegionCanBeSelected(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      HomePage homePage = new HomePage(driver)
          .open()
          .changeRegionTo(EXISTING_CITY);

      assertTrue(homePage.currentRegionContains(EXISTING_CITY));
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void nonExistingRegionDoesNotChangeCurrentRegion(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      HomePage homePage = new HomePage(driver).open();
      String regionBefore = homePage.currentRegion();

      homePage.tryChangeRegionTo(NON_EXISTING_CITY);

      assertEquals(regionBefore, homePage.currentRegion());
    });
  }
}
