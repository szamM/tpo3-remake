import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.AdvancedSearchPage;
import pages.HomePage;
import pages.VacancyPage;
import pages.VacancySearchPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
public class AdvancedSearchTest extends BaseBrowserTest {

  private static final String ENTRY_LEVEL_QUERY = "QA";
  private static final String ENTRY_LEVEL_SALARY = "100000";
  private static final String IMPOSSIBLE_QUERY = "QANonexistentVacancy123124";
  private static final String IMPOSSIBLE_SALARY = "9999999";
  private static final int VACANCIES_TO_CHECK = 2;

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void entryLevelFullTimeSearchOpensMatchingVacancies(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      AdvancedSearchPage advancedSearchPage = new HomePage(driver).open().openAdvancedSearch();
      VacancySearchPage searchPage = advancedSearchPage
          .searchEntryLevelFullTimeVacancies(ENTRY_LEVEL_QUERY, ENTRY_LEVEL_SALARY);
      int vacanciesToCheck = Math.min(VACANCIES_TO_CHECK, searchPage.resultCount());

      assertTrue(searchPage.hasHiddenFilterValue("experience", "noExperience"));
      assertTrue(searchPage.hasHiddenFilterValue("employment_form", "FULL"));
      assertTrue(vacanciesToCheck > 0);

      for (int vacancyIndex = 0; vacancyIndex < vacanciesToCheck; vacancyIndex++) {
        VacancyPage vacancyPage = searchPage.openVacancyByIndex(vacancyIndex);

        assertTrue(vacancyPage.containsAllQueryWords(ENTRY_LEVEL_QUERY));
        assertTrue(vacancyPage.hasNoExperience());
        assertTrue(vacancyPage.hasFullEmployment());
        assertTrue(vacancyPage.hasResponseButton());

        searchPage = vacancyPage.backToSearchResults(ENTRY_LEVEL_QUERY);
      }
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void impossibleEntryLevelFullTimeSalaryShowsNothingFound(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      VacancySearchPage searchPage = new HomePage(driver)
          .open()
          .openAdvancedSearch()
          .searchEntryLevelFullTimeVacancies(IMPOSSIBLE_QUERY, IMPOSSIBLE_SALARY)
          .waitUntilNothingFoundLoaded(IMPOSSIBLE_QUERY);

      assertEquals(0, searchPage.resultCount());
      assertTrue(searchPage.nothingFoundText().contains(IMPOSSIBLE_QUERY));
      assertTrue(searchPage.nothingFoundText().contains("ничего не найдено"));
    });
  }
}
