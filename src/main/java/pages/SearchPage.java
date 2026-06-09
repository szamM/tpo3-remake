package pages;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Locale;

public class SearchPage extends Page {

  @FindBy(xpath = "//h1[contains(., 'ваканс') or contains(., 'Вакансии')]")
  protected WebElement resultHeading;

  @FindBy(xpath = "//*[@data-qa='serp-item__title']")
  protected List<WebElement> resultTitles;

  @FindBy(xpath = "//*[@data-qa='vacancy-serp__vacancy-employer']")
  private List<WebElement> employerLinks;

  @FindBy(xpath = "//input[@name='with_salary']")
  private WebElement withSalaryFilter;

  public SearchPage(WebDriver driver) {
    super(driver);
  }

  public VacancySearchPage openResults(String query) {
    openPath("/search/vacancy?text=" + urlEncode(query) + "&area=1");
    return waitForResults(query);
  }

  public VacancySearchPage waitForResults(String query) {
    visible(resultHeading);
    wait.until(driver -> {
      skipIfCaptchaPresent();
      return normalizeText(resultHeading.getText()).contains(query)
          || !elements(resultTitles).isEmpty();
    });
    return new VacancySearchPage(driver);
  }

  public String heading() {
    return visible(resultHeading).getText();
  }

  public int resultCount() {
    return elements(resultTitles).size();
  }

  public int employerCount() {
    return elements(employerLinks).size();
  }

  public SearchPage selectOnlyWithSalary() {
    clickCheckable(withSalaryFilter);
    wait.until(driver -> withSalaryFilter.isSelected()
        || driver.getCurrentUrl().contains("with_salary")
        || driver.getCurrentUrl().contains("label=with_salary"));
    return this;
  }

  public VacancyPage openFirstVacancy() {
    WebElement firstVacancy = visible(resultTitles);
    String href = firstVacancy.getAttribute("href");
    click(firstVacancy);
    if (!waitShortForUrlContains("/vacancy/")) {
      driver.get(href);
    }
    return new VacancyPage(driver).waitUntilOpened();
  }

  public boolean hasResultWithText(String expectedText) {
    String expected = expectedText.toLowerCase(Locale.ROOT);
    return elements(resultTitles).stream()
        .map(WebElement::getText)
        .map(text -> text.toLowerCase(Locale.ROOT))
        .anyMatch(text -> text.contains(expected));
  }

  public boolean hasEmployerContaining(String expectedText) {
    String expected = expectedText.toLowerCase(Locale.ROOT);
    try {
      return wait.until(driver -> visibleEmployersContain(expected));
    } catch (TimeoutException exception) {
      return false;
    }
  }

  private boolean visibleEmployersContain(String expectedText) {
    try {
      return elements(employerLinks).stream()
          .filter(WebElement::isDisplayed)
          .map(WebElement::getText)
          .map(text -> text.toLowerCase(Locale.ROOT))
          .anyMatch(text -> text.contains(expectedText));
    } catch (StaleElementReferenceException exception) {
      return false;
    }
  }
}
