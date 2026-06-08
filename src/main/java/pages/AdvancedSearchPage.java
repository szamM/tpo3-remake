package pages;

import org.openqa.selenium.WebDriver;

public class AdvancedSearchPage extends Page {

  private static final String TITLE = "//h1[contains(., 'Поиск вакансий')]";
  private static final String KEYWORDS_INPUT = "//*[@data-qa='vacancysearch__keywords-input']";
  private static final String SALARY_INPUT = "//*[@data-qa='advanced-search-salary']";
  private static final String EXPERIENCE_NO_EXPERIENCE = "//*[@data-qa='advanced-search__experience-item_noExperience']";
  private static final String EMPLOYMENT_FULL = "//*[@data-qa='advanced-search__employment_form-item_FULL']";
  private static final String SUBMIT_BUTTON = "//*[@data-qa='advanced-search-submit-button']";

  public AdvancedSearchPage(WebDriver driver) {
    super(driver);
  }

  public AdvancedSearchPage open() {
    openPath("/search/vacancy/advanced?area=1");
    return waitUntilOpened();
  }

  public AdvancedSearchPage waitUntilOpened() {
    visible(TITLE);
    visible(KEYWORDS_INPUT);
    return this;
  }

  public VacancySearchPage searchEntryLevelFullTimeVacancies(String query, String salaryFrom) {
    type(KEYWORDS_INPUT, query);
    type(SALARY_INPUT, salaryFrom);
    clickCheckable(EXPERIENCE_NO_EXPERIENCE);
    clickCheckable(EMPLOYMENT_FULL);
    click(SUBMIT_BUTTON);
    return new VacancySearchPage(driver).waitUntilSearchFinished(query);
  }
}
