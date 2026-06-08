package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Locale;

public class SearchPage extends Page {

  protected static final String RESULT_HEADING = "//h1[contains(., 'ваканс') or contains(., 'Вакансии')]";
  protected static final String RESULT_TITLES = "//*[@data-qa='serp-item__title']";
  protected static final String EMPLOYER_LINKS = "//*[@data-qa='vacancy-serp__vacancy-employer']";
  private static final String WITH_SALARY_FILTER = "//input[@name='with_salary']";

  public SearchPage(WebDriver driver) {
    super(driver);
  }

  public VacancySearchPage openResults(String query) {
    openPath("/search/vacancy?text=" + urlEncode(query) + "&area=1");
    return waitForResults(query);
  }

  public VacancySearchPage waitForResults(String query) {
    visible(RESULT_HEADING);
    wait.until(ExpectedConditions.or(
        ExpectedConditions.textToBePresentInElementLocated(By.xpath(RESULT_HEADING), query),
        ExpectedConditions.presenceOfElementLocated(By.xpath(RESULT_TITLES))
    ));
    return new VacancySearchPage(driver);
  }

  public String heading() {
    return visible(RESULT_HEADING).getText();
  }

  public int resultCount() {
    return elements(RESULT_TITLES).size();
  }

  public int employerCount() {
    return elements(EMPLOYER_LINKS).size();
  }

  public SearchPage selectOnlyWithSalary() {
    clickCheckable(WITH_SALARY_FILTER);
    wait.until(driver -> driver.findElement(By.xpath(WITH_SALARY_FILTER)).isSelected()
        || driver.getCurrentUrl().contains("with_salary")
        || driver.getCurrentUrl().contains("label=with_salary"));
    return this;
  }

  public VacancyPage openFirstVacancy() {
    WebElement firstVacancy = visible(RESULT_TITLES);
    String href = firstVacancy.getAttribute("href");
    click(RESULT_TITLES);
    if (!waitShortForUrlContains("/vacancy/")) {
      driver.get(href);
    }
    return new VacancyPage(driver).waitUntilOpened();
  }

  public boolean hasResultWithText(String expectedText) {
    String expected = expectedText.toLowerCase(Locale.ROOT);
    List<WebElement> titles = elements(RESULT_TITLES);
    return titles.stream()
        .map(WebElement::getText)
        .map(text -> text.toLowerCase(Locale.ROOT))
        .anyMatch(text -> text.contains(expected));
  }

  public boolean hasEmployerContaining(String expectedText) {
    String expected = expectedText.toLowerCase(Locale.ROOT);
    List<WebElement> employers = elements(EMPLOYER_LINKS);
    return employers.stream()
        .map(WebElement::getText)
        .map(text -> text.toLowerCase(Locale.ROOT))
        .anyMatch(text -> text.contains(expected));
  }
}
