import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import pages.HomePage;
import pages.VacancyPage;
import pages.VacancySearchPage;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(4)
public class VacancySearchTest extends BaseBrowserTest {

  private static final String EMPTY_QUERY = "";
  private static final String UNKNOWN_VACANCY = "123124";
  private static final String KEYWORD_QUERY = "Java";
  private static final String COMPANY_QUERY = "HeadHunter";
  private static final String DETAILS_QUERY = "Java";

  @Order(1)
  @Test
  void searchUnknownVacancyByKeyword() {
    runInBrowsers(driver -> {
      VacancySearchPage searchPage = new HomePage(driver)
          .open()
          .closePopups()
          .searchVacanciesByKeyword(UNKNOWN_VACANCY)
          .closePopups()
          .waitUntilNothingFoundLoaded(UNKNOWN_VACANCY);

      assertEquals("По запросу «" + UNKNOWN_VACANCY + "» ничего не найдено", searchPage.nothingFoundText());
    });
  }

  @Order(2)
  @Test
  void emptySearchShowsVacancyList() {
    runInBrowsers(driver -> {
      VacancySearchPage searchPage = new HomePage(driver)
          .open()
          .closePopups()
          .searchVacanciesByKeyword(EMPTY_QUERY)
          .closePopups()
          .waitUntilResultsLoaded();

      assertTrue(searchPage.currentUrl().contains("/search/vacancy"));
      assertEquals(EMPTY_QUERY, searchPage.queryInputValue());
      assertTrue(searchPage.resultCount() > 0);
    });
  }

  @Order(3)
  @Test
  void searchVacancyByKeyword() {
    runInBrowsers(driver -> {
      VacancySearchPage searchPage = new HomePage(driver)
          .open()
          .closePopups()
          .searchVacanciesByKeyword(KEYWORD_QUERY)
          .closePopups()
          .waitUntilResultsLoaded();

      assertEquals(KEYWORD_QUERY, searchPage.queryInputValue());
      assertTrue(searchPage.heading().contains(KEYWORD_QUERY));
      assertTrue(searchPage.hasResultWithText(KEYWORD_QUERY));
      assertTrue(searchPage.resultCount() > 0);
    });
  }

  @Order(4)
  @Test
  void searchVacanciesByCompanyName() {
    runInBrowsers(driver -> {
      VacancySearchPage searchPage = new VacancySearchPage(driver)
          .openWithFilters(COMPANY_QUERY, "search_field=company_name")
          .closePopups()
          .waitUntilResultsLoaded();

      assertEquals(COMPANY_QUERY, searchPage.queryInputValue());
      assertTrue(searchPage.hasEmployerContaining(COMPANY_QUERY));
    });
  }

  @Order(5)
  @Test
  void openVacancyDetails() {
    runInBrowsers(driver -> {
      VacancyPage vacancyPage = new HomePage(driver)
          .open()
          .closePopups()
          .searchVacancies(DETAILS_QUERY)
          .closePopups()
          .openFirstVacancy();
      String title = vacancyPage.title().toLowerCase(Locale.ROOT);

      assertTrue(!title.isBlank());
      assertTrue(vacancyPage.hasResponseButton());
      assertTrue(vacancyPage.experience().contains("Опыт работы"));
    });
  }
}
