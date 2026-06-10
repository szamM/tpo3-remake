import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.VacancySearchPage;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(5)
public class VacancyFilterTest extends BaseBrowserTest {

  private static final String QUERY = "Java";
  private static final int MIN_SALARY = 110000;
  private static final String INVALID_SALARY = "abc";
  private static final String INVALID_FILTER_VALUE = "INVALID";
  private static final String EXCLUDED_WORD = "Senior";

  @Order(1)
  @Test
  void salaryFilterShowsVacanciesWithSuitableIncome() {
    runInBrowsers(driver -> {
      VacancySearchPage searchPage = new VacancySearchPage(driver)
          .openWithFilters(QUERY, "only_with_salary=true&salary=" + MIN_SALARY)
          .waitUntilSalaryTextsLoaded();

      assertEquals("110 000", searchPage.salaryFilterValue());
      assertTrue(searchPage.visibleSalaryTexts().size() > 0);
      assertTrue(searchPage.allVisibleSalaryTextsCanContainIncome(MIN_SALARY));
    });
  }

  @Order(2)
  @Test
  void fullEmploymentFilterIsSelected() {
    runInBrowsers(driver -> {
      VacancySearchPage searchPage = new VacancySearchPage(driver)
          .openWithFilters(QUERY, "employment_form=FULL");

      assertTrue(searchPage.hasHiddenFilterValue("employment_form", "FULL"));
      assertTrue(searchPage.resultCount() > 0);
    });
  }

  @Order(3)
  @Test
  void noExperienceFilterShowsOnlyNoExperienceCards() {
    runInBrowsers(driver -> {
      VacancySearchPage searchPage = new VacancySearchPage(driver)
          .openWithFilters(QUERY, "experience=noExperience");

      assertTrue(searchPage.hasHiddenFilterValue("experience", "noExperience"));
      assertTrue(searchPage.allVisibleCardsContain("Без опыта"));
    });
  }

  @Order(4)
  @Test
  void remoteWorkFormatFilterShowsOnlyRemoteCards() {
    runInBrowsers(driver -> {
      VacancySearchPage searchPage = new VacancySearchPage(driver)
          .openWithFilters(QUERY, "work_format=REMOTE");

      assertTrue(searchPage.hasHiddenFilterValue("work_format", "REMOTE"));
      assertTrue(searchPage.allVisibleCardsContain("удал"));
    });
  }

  @Order(5)
  @Test
  void excludedWordFilterHidesMatchingVacancies() {
    runInBrowsers(driver -> {
      VacancySearchPage searchPage = new VacancySearchPage(driver)
          .openWithFilters(QUERY, "excluded_text=" + EXCLUDED_WORD)
          .closePopups()
          .waitUntilVisibleCardsLoaded();

      assertTrue(searchPage.resultCount() > 0);
      assertTrue(searchPage.allVisibleCardsDoNotContain(EXCLUDED_WORD));
    });
  }

  private static Stream<Arguments> invalidFilterCases() {
    return Stream.of(
        Arguments.of("salary=" + INVALID_SALARY, "salary", INVALID_SALARY),
        Arguments.of("employment_form=" + INVALID_FILTER_VALUE, "employment_form", INVALID_FILTER_VALUE),
        Arguments.of("experience=" + INVALID_FILTER_VALUE, "experience", INVALID_FILTER_VALUE),
        Arguments.of("work_format=" + INVALID_FILTER_VALUE, "work_format", INVALID_FILTER_VALUE)
    );
  }

  @Order(6)
  @ParameterizedTest(name = "{1}")
  @MethodSource("invalidFilterCases")
  void invalidFilterValuesAreIgnored(
      String filterParams,
      String filterName,
      String invalidValue
  ) {
    runInBrowsers(driver -> {
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
