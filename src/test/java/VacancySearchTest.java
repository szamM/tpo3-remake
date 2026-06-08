import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.HomePage;
import pages.VacancyPage;
import pages.VacancySearchPage;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
public class VacancySearchTest extends BaseBrowserTest {

  private static final String EMPTY_QUERY = "";
  private static final String UNKNOWN_VACANCY = "123124";
  private static final String KEYWORD_QUERY = "Java";
  private static final String COMPANY_QUERY = "HeadHunter";
  private static final String DETAILS_QUERY = "Java";

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void searchUnknownVacancyByKeyword(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      VacancySearchPage searchPage = new HomePage(driver)
          .open()
          .searchVacanciesByKeyword(UNKNOWN_VACANCY)
          .waitUntilNothingFoundLoaded(UNKNOWN_VACANCY);

      assertEquals("По запросу «" + UNKNOWN_VACANCY + "» ничего не найдено", searchPage.nothingFoundText());
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void emptySearchShowsVacancyList(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      VacancySearchPage searchPage = new HomePage(driver)
          .open()
          .searchVacanciesByKeyword(EMPTY_QUERY)
          .waitUntilResultsLoaded();

      assertTrue(searchPage.currentUrl().contains("/search/vacancy"));
      assertEquals(EMPTY_QUERY, searchPage.queryInputValue());
      assertTrue(searchPage.resultCount() > 0);
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void searchVacancyByKeyword(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      VacancySearchPage searchPage = new HomePage(driver)
          .open()
          .searchVacanciesByKeyword(KEYWORD_QUERY)
          .waitUntilResultsLoaded();

      assertEquals(KEYWORD_QUERY, searchPage.queryInputValue());
      assertTrue(searchPage.heading().contains(KEYWORD_QUERY));
      assertTrue(searchPage.hasResultWithText(KEYWORD_QUERY));
      assertTrue(searchPage.resultCount() > 0);
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void searchVacanciesByCompanyName(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      VacancySearchPage searchPage = new HomePage(driver)
          .open()
          .searchVacanciesByKeyword(COMPANY_QUERY)
          .waitUntilResultsLoaded();

      assertEquals(COMPANY_QUERY, searchPage.queryInputValue());
      assertTrue(searchPage.hasEmployerContaining(COMPANY_QUERY));
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void openVacancyDetails(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      VacancyPage vacancyPage = new HomePage(driver).open().searchVacancies(DETAILS_QUERY).openFirstVacancy();
      String title = vacancyPage.title().toLowerCase(Locale.ROOT);

      assertTrue(!title.isBlank());
      assertTrue(vacancyPage.hasResponseButton());
      assertTrue(vacancyPage.experience().contains("Опыт работы"));
    });
  }
}
