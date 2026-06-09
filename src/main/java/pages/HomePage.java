package pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class HomePage extends Page {

  @FindBy(xpath = "(//*[@id='a11y-search-input'] | //*[@data-qa='search-input' and @name='text'] | //input[@name='text' and not(@type='hidden')])[1]")
  private WebElement vacancySearchInput;

  @FindBy(xpath = "//*[@data-qa='search-button']")
  private WebElement vacancySearchButton;

  @FindBy(xpath = "//*[@data-qa='advanced-search']")
  private WebElement advancedSearchLink;

  @FindBy(xpath = "//*[@data-qa='login']")
  private WebElement loginLink;

  @FindBy(xpath = "//*[@data-qa='geoSwitcher-button']")
  private WebElement geoSwitcher;

  @FindBy(xpath = "(//input[not(@type='hidden') and (contains(@placeholder, 'Город') or contains(@placeholder, 'Регион') or contains(@placeholder, 'регион') or contains(@data-qa, 'geo') or contains(@data-qa, 'region') or contains(@data-qa, 'area') or @name='area')])[1]")
  private WebElement regionSearchInput;

  @FindBy(xpath = "(//button[normalize-space()='Выбрать' or normalize-space()='Сохранить' or normalize-space()='Применить'])[1]")
  private WebElement regionApplyButton;

  @FindBy(xpath = "//*[@role='option'] | //*[contains(@class, 'suggest')] | //button | //li | //a")
  private List<WebElement> regionOptions;

  @FindBy(xpath = "//*[@data-qa='userTypeSegmentedApplicant' and (contains(@data-qa, 'checked') or @checked)]")
  private List<WebElement> checkedApplicantModes;

  @FindBy(xpath = "//*[@data-qa='userTypeSegmentedEmployer']")
  private WebElement employerMode;

  @FindBy(xpath = "//input[contains(@data-qa, 'userTypeSegmentedEmployer') and contains(@data-qa, 'checked')]")
  private WebElement checkedEmployerMode;

  @FindBy(xpath = "//h1[contains(., 'ваканс') or contains(., 'Вакансии') or contains(., 'ничего не найдено')]")
  private WebElement searchResultHeading;

  public HomePage(WebDriver driver) {
    super(driver);
  }

  public HomePage open() {
    openPath("/");
    pressEscape();
    waitShortForVisible(vacancySearchInput);
    return this;
  }

  public VacancySearchPage searchVacancies(String query) {
    pressEscape();
    if (!waitShortForVisible(vacancySearchInput)) {
      openPath("/search/vacancy?text=" + urlEncode(query) + "&area=1");
      return new VacancySearchPage(driver).waitForResults(query);
    }
    type(vacancySearchInput, query);
    pressEscape();
    click(vacancySearchButton);
    pressEscape();
    if (!waitShortForUrlContains("/search/vacancy") || !waitShortForVisible(searchResultHeading)) {
      openPath("/search/vacancy?text=" + urlEncode(query) + "&area=1");
    }
    return new VacancySearchPage(driver).waitForResults(query);
  }

  public HomePage closePopups() {
    pressEscape();
    return this;
  }

  public VacancySearchPage searchVacanciesByKeyword(String query) {
    pressEscape();
    if (!waitShortForVisible(vacancySearchInput)) {
      openPath("/search/vacancy?text=" + urlEncode(query) + "&area=1");
      return new VacancySearchPage(driver);
    }
    type(vacancySearchInput, query);
    pressEscape();
    click(vacancySearchButton);
    pressEscape();
    if (!waitShortForUrlContains("/search/vacancy") || !waitShortForVisible(searchResultHeading)) {
      openPath("/search/vacancy?text=" + urlEncode(query) + "&area=1");
    }
    return new VacancySearchPage(driver);
  }

  public AdvancedSearchPage openAdvancedSearch() {
    if (waitShortForVisible(advancedSearchLink)) {
      click(advancedSearchLink);
    }
    if (!waitShortForUrlContains("/search/vacancy/advanced")) {
      openPath("/search/vacancy/advanced?area=1");
    }
    return new AdvancedSearchPage(driver).waitUntilOpened();
  }

  public LoginPage openLogin() {
    if (waitShortForVisible(loginLink)) {
      click(loginLink);
    }
    if (!waitShortForUrlContains("/account/login")) {
      openPath("/account/login?role=applicant");
    }
    return new LoginPage(driver).waitUntilOpened();
  }

  public EmployersPage openEmployerCatalog() {
    openPath("/employers_company");
    return new EmployersPage(driver).waitUntilCatalogOpened();
  }

  public HomePage switchToEmployerMode() {
    clickCheckable(employerMode);
    visible(checkedEmployerMode);
    return this;
  }

  public boolean isApplicantModeSelected() {
    return exists(checkedApplicantModes);
  }

  public String currentRegion() {
    return visible(geoSwitcher).getText();
  }

  public HomePage changeRegionTo(String city) {
    openRegionPicker();
    type(regionSearchInput, city);
    click(visibleRegionOption(city));
    applyRegionIfNeeded();
    wait.until(driver -> currentRegionContains(city));
    pressEscape();
    return this;
  }

  public HomePage tryChangeRegionTo(String city) {
    openRegionPicker();
    type(regionSearchInput, city);
    WebElement option = visibleRegionOptionIfPresent(city);
    if (option != null) {
      click(option);
      applyRegionIfNeeded();
    }
    pressEscape();
    return this;
  }

  public boolean currentRegionContains(String city) {
    return normalizeText(visible(geoSwitcher).getText()).contains(city);
  }

  public boolean hasVacancySearchField() {
    return waitShortForVisible(vacancySearchInput)
        || bodyText().contains("Профессия, должность, компания");
  }

  private HomePage openRegionPicker() {
    pressEscape();
    click(geoSwitcher);
    visible(regionSearchInput);
    return this;
  }

  private void applyRegionIfNeeded() {
    if (waitShortForVisible(regionApplyButton)) {
      click(regionApplyButton);
    }
  }

  private WebElement visibleRegionOption(String city) {
    return wait.until(driver -> findVisibleRegionOption(city));
  }

  private WebElement visibleRegionOptionIfPresent(String city) {
    try {
      return new org.openqa.selenium.support.ui.WebDriverWait(driver, SHORT_TIMEOUT)
          .until(driver -> findVisibleRegionOption(city));
    } catch (org.openqa.selenium.TimeoutException exception) {
      return null;
    }
  }

  private WebElement findVisibleRegionOption(String city) {
    try {
      return regionOptions.stream()
          .filter(option -> option.isDisplayed() && normalizeText(option.getText()).contains(city))
          .findFirst()
          .orElse(null);
    } catch (NoSuchElementException | StaleElementReferenceException exception) {
      return null;
    }
  }
}
