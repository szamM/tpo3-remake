import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import pages.AdvancedSearchPage;
import pages.EmployersPage;
import pages.HomePage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(1)
public class HomeNavigationTest extends BaseBrowserTest {

  private static final String EXISTING_CITY = "Санкт-Петербург";
  private static final String NON_EXISTING_CITY = "ГородКоторогоНет123124";

  @Order(1)
  @Test
  void homePageOpensWithSearchField() {
    runInBrowsers(driver -> {
      HomePage homePage = new HomePage(driver).open();
      assertTrue(homePage.hasVacancySearchField());
    });
  }

  @Order(2)
  @Test
  void advancedSearchOpens() {
    runInBrowsers(driver -> {
      AdvancedSearchPage advancedSearchPage = new HomePage(driver).open().openAdvancedSearch();
      assertTrue(advancedSearchPage.currentUrl().contains("/search/vacancy/advanced"));
    });
  }

  @Order(3)
  @Test
  void employerCatalogOpens() {
    runInBrowsers(driver -> {
      EmployersPage employersPage = new HomePage(driver).openEmployerCatalog();
      assertTrue(employersPage.currentUrl().contains("/employers_company"));
    });
  }

  @Order(4)
  @Test
  void existingRegionCanBeSelected() {
    runInBrowsers(driver -> {
      HomePage homePage = new HomePage(driver)
          .open()
          .changeRegionTo(EXISTING_CITY);

      assertTrue(homePage.currentRegionContains(EXISTING_CITY));
    });
  }

  @Order(5)
  @Test
  void nonExistingRegionDoesNotChangeCurrentRegion() {
    runInBrowsers(driver -> {
      HomePage homePage = new HomePage(driver).open();
      String regionBefore = homePage.currentRegion();

      homePage.tryChangeRegionTo(NON_EXISTING_CITY);

      assertEquals(regionBefore, homePage.currentRegion());
    });
  }
}
