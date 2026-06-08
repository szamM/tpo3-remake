package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends Page {

  private static final String SEARCH_INPUT = "//*[@id=\"a11y-search-input\"]";
  private static final String SEARCH_BUTTON = "//*[@data-qa='search-button']";
  private static final String ADVANCED_SEARCH_LINK = "//*[@data-qa='advanced-search']";
  private static final String LOGIN_LINK = "//*[@data-qa='login']";
  private static final String GEO_SWITCHER = "//*[@data-qa='geoSwitcher-button']";
  private static final String APPLICANT_MODE = "//*[@data-qa='userTypeSegmentedApplicant']";
  private static final String EMPLOYER_MODE = "//*[@data-qa='userTypeSegmentedEmployer']";
  private static final String SEARCH_RESULT_HEADING =
      "//h1[contains(., 'ваканс') or contains(., 'Вакансии') or contains(., 'ничего не найдено')]";

  @FindBy(xpath = SEARCH_INPUT)
  private WebElement vacancySearchInput;

  @FindBy(xpath = SEARCH_BUTTON)
  private WebElement vacancySearchButton;

  public HomePage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  public HomePage open() {
    openPath("/");
    visible(vacancySearchInput);
    return this;
  }

  public VacancySearchPage searchVacancies(String query) {
    type(vacancySearchInput, query);
    click(vacancySearchButton);
    if (!waitShortForUrlContains("/search/vacancy") || !waitShortForVisible(SEARCH_RESULT_HEADING)) {
      openPath("/search/vacancy?text=" + urlEncode(query) + "&area=1");
    }
    return new VacancySearchPage(driver).waitForResults(query);
  }

  public VacancySearchPage searchVacanciesByKeyword(String query) {
    type(vacancySearchInput, query);
    pressEscape();
    click(vacancySearchButton);
    if (!waitShortForUrlContains("/search/vacancy") || !waitShortForVisible(SEARCH_RESULT_HEADING)) {
      openPath("/search/vacancy?text=" + urlEncode(query) + "&area=1");
    }
    return new VacancySearchPage(driver);
  }

  public AdvancedSearchPage openAdvancedSearch() {
    click(ADVANCED_SEARCH_LINK);
    if (!waitShortForUrlContains("/search/vacancy/advanced")) {
      openPath("/search/vacancy/advanced?area=1");
    }
    return new AdvancedSearchPage(driver).waitUntilOpened();
  }

  public LoginPage openLogin() {
    click(LOGIN_LINK);
    waitForUrlContains("/account/login");
    return new LoginPage(driver).waitUntilOpened();
  }

  public EmployersPage openEmployerCatalog() {
    openPath("/employers_company");
    return new EmployersPage(driver).waitUntilCatalogOpened();
  }

  public HomePage switchToEmployerMode() {
    clickCheckable(EMPLOYER_MODE);
    visible("//input[contains(@data-qa, 'userTypeSegmentedEmployer') and contains(@data-qa, 'checked')]");
    return this;
  }

  public boolean isApplicantModeSelected() {
    return exists(APPLICANT_MODE + "[contains(@data-qa, 'checked') or @checked]");
  }

  public String currentRegion() {
    return visible(GEO_SWITCHER).getText();
  }
}
