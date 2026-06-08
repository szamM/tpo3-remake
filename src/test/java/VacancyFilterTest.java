import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.VacancySearchPage;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
public class VacancyFilterTest extends BaseBrowserTest {

  private static final String QUERY = "Java";
  private static final int MIN_SALARY = 110000;
  private static final String INVALID_SALARY = "abc";
  private static final String INVALID_FILTER_VALUE = "INVALID";

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void salaryFilterShowsVacanciesWithSuitableIncome(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      VacancySearchPage searchPage = new VacancySearchPage(driver)
          .openWithFilters(QUERY, "only_with_salary=true&salary=" + MIN_SALARY)
          .waitUntilSalaryTextsLoaded();

      assertEquals("110 000", searchPage.salaryFilterValue());
      assertTrue(searchPage.visibleSalaryTexts().size() > 0);
      assertTrue(searchPage.allVisibleSalaryTextsCanContainIncome(MIN_SALARY));
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void fullEmploymentFilterIsSelected(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      VacancySearchPage searchPage = new VacancySearchPage(driver)
          .openWithFilters(QUERY, "employment_form=FULL");

      assertTrue(searchPage.hasHiddenFilterValue("employment_form", "FULL"));
      assertTrue(searchPage.resultCount() > 0);
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void noExperienceFilterShowsOnlyNoExperienceCards(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      VacancySearchPage searchPage = new VacancySearchPage(driver)
          .openWithFilters(QUERY, "experience=noExperience");

      assertTrue(searchPage.hasHiddenFilterValue("experience", "noExperience"));
      assertTrue(searchPage.allVisibleCardsContain("Без опыта"));
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void remoteWorkFormatFilterShowsOnlyRemoteCards(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      VacancySearchPage searchPage = new VacancySearchPage(driver)
          .openWithFilters(QUERY, "work_format=REMOTE");

      assertTrue(searchPage.hasHiddenFilterValue("work_format", "REMOTE"));
      assertTrue(searchPage.allVisibleCardsContain("удал"));
    });
  }

  private static Stream<Arguments> invalidFilterCases() {
    return browsers().flatMap(browserType -> Stream.of(
        Arguments.of(browserType, "salary=" + INVALID_SALARY, "salary", INVALID_SALARY),
        Arguments.of(browserType, "employment_form=" + INVALID_FILTER_VALUE, "employment_form", INVALID_FILTER_VALUE),
        Arguments.of(browserType, "experience=" + INVALID_FILTER_VALUE, "experience", INVALID_FILTER_VALUE),
        Arguments.of(browserType, "work_format=" + INVALID_FILTER_VALUE, "work_format", INVALID_FILTER_VALUE)
    ));
  }

  @ParameterizedTest(name = "{0} - {2}")
  @MethodSource("invalidFilterCases")
  void invalidFilterValuesAreIgnored(
      BrowserType browserType,
      String filterParams,
      String filterName,
      String invalidValue
  ) {
    runInBrowser(browserType, driver -> {
      VacancySearchPage searchPage = new VacancySearchPage(driver)
          .openWithFilters(QUERY, filterParams)
          .waitUntilResultsLoaded();

      assertEquals(QUERY, searchPage.queryInputValue());
      assertTrue(searchPage.resultCount() > 0);
      assertFalse(searchPage.hasHiddenFilterValue(filterName, invalidValue));
      if ("salary".equals(filterName)) {
        assertTrue(searchPage.salaryFilterValue().isBlank());
      }
    });
  }
}
