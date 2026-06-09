package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AdvancedSearchPage extends Page {

  @FindBy(xpath = "//h1[contains(., 'Поиск вакансий')]")
  private WebElement title;

  @FindBy(xpath = "//*[@data-qa='vacancysearch__keywords-input']")
  private WebElement keywordsInput;

  @FindBy(xpath = "//*[@data-qa='advanced-search-salary']")
  private WebElement salaryInput;

  @FindBy(xpath = "//*[@data-qa='advanced-search__experience-item_noExperience']")
  private WebElement noExperienceCheckbox;

  @FindBy(xpath = "//*[@data-qa='advanced-search__employment_form-item_FULL']")
  private WebElement fullEmploymentCheckbox;

  @FindBy(xpath = "//*[@data-qa='advanced-search-submit-button']")
  private WebElement submitButton;

  public AdvancedSearchPage(WebDriver driver) {
    super(driver);
  }

  public AdvancedSearchPage open() {
    openPath("/search/vacancy/advanced?area=1");
    return waitUntilOpened();
  }

  public AdvancedSearchPage waitUntilOpened() {
    visible(title);
    visible(keywordsInput);
    return this;
  }

  public VacancySearchPage searchEntryLevelFullTimeVacancies(String query, String salaryFrom) {
    type(keywordsInput, query);
    typeMasked(salaryInput, salaryFrom);
    clickCheckable(noExperienceCheckbox);
    clickCheckable(fullEmploymentCheckbox);
    click(submitButton);
    return new VacancySearchPage(driver).waitUntilSearchFinished(query);
  }
}
