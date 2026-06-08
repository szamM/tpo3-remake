package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VacancySearchPage extends SearchPage {

  private static final String NBSP = "\u00A0";
  private static final String SEARCH_INPUT = "//*[@data-qa='search-input' and @name='text']";
  private static final String NOTHING_FOUND_TEXT = "//h1[contains(translate(., '" + NBSP + "', ' '), 'ничего не найдено')]";
  private static final String VACANCY_CARDS =
      "//*[contains(concat(' ', normalize-space(@data-qa), ' '), ' vacancy-serp__vacancy ')]";
  private static final String SALARY_FILTER_INPUT = "//*[@data-qa='novafilters-custom-compensation']";
  private static final Pattern SALARY_TEXT = Pattern.compile(
      "(?iu)(?:от\\s+|до\\s+)?\\d[\\d\\s]*\\s*(?:[–-]\\s*\\d[\\d\\s]*)?\\s*₽"
  );
  private static final Pattern SALARY_RANGE = Pattern.compile("(?iu)(\\d[\\d\\s]*)\\s*[–-]\\s*(\\d[\\d\\s]*)\\s*₽");
  private static final Pattern SALARY_FROM = Pattern.compile("(?iu)от\\s+(\\d[\\d\\s]*)\\s*₽");
  private static final Pattern SALARY_TO = Pattern.compile("(?iu)до\\s+(\\d[\\d\\s]*)\\s*₽");
  private static final Pattern SALARY_EXACT = Pattern.compile("(?iu)(\\d[\\d\\s]*)\\s*₽");

  @FindBy(xpath = NOTHING_FOUND_TEXT)
  private WebElement nothingFoundText;

  public VacancySearchPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  public VacancySearchPage openWithFilters(String query, String filterParams) {
    String path = "/search/vacancy?text=" + urlEncode(query) + "&area=1";
    if (!filterParams.isBlank()) {
      path += "&" + filterParams;
    }
    openPath(path);
    return waitForResults(query);
  }

  public VacancySearchPage waitUntilSearchFinished(String query) {
    wait.until(driver -> {
      skipIfCaptchaPresent();
      return exists(RESULT_TITLES) || exists(NOTHING_FOUND_TEXT);
    });
    return this;
  }

  public VacancySearchPage waitUntilResultsLoaded() {
    visible(RESULT_HEADING);
    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(RESULT_TITLES)));
    return this;
  }

  public VacancySearchPage waitUntilNothingFoundLoaded(String query) {
    wait.until(driver -> normalizeText(visible(nothingFoundText).getText()).contains(query));
    return this;
  }

  public String queryInputValue() {
    return normalizeText(visible(SEARCH_INPUT).getAttribute("value"));
  }

  public String nothingFoundText() {
    return normalizeText(visible(nothingFoundText).getText());
  }

  public void checkNothingFoundFor(String query) {
    assertEquals(nothingFoundMessage(query), normalizeText(visible(nothingFoundText).getText()));
  }

  public String salaryFilterValue() {
    return normalizeText(visible(SALARY_FILTER_INPUT).getAttribute("value"));
  }

  public boolean isCheckboxFilterSelected(String name, String value) {
    return present("//input[@name='" + name + "' and @value='" + value + "']").isSelected();
  }

  public VacancySearchPage selectCheckboxFilter(String name, String value) {
    clickCheckable("//input[@name='" + name + "' and @value='" + value + "']");
    wait.until(driver -> isCheckboxFilterSelected(name, value));
    return this;
  }

  public boolean hasHiddenFilterValue(String name, String value) {
    return exists("//input[@type='hidden' and @name='" + name + "' and @value='" + value + "']");
  }

  public List<String> visibleVacancyCardTexts() {
    for (int attempt = 0; attempt < 3; attempt++) {
      try {
        List<String> texts = elements(VACANCY_CARDS).stream()
            .filter(WebElement::isDisplayed)
            .map(WebElement::getText)
            .map(this::normalizeText)
            .filter(text -> !text.isBlank())
            .toList();
        if (!texts.isEmpty()) {
          return texts;
        }
      } catch (StaleElementReferenceException ignored) {
        // список карточек перерисовался, пробуем еще раз
      }
    }
    return List.of();
  }

  public boolean allVisibleCardsContain(String expectedText) {
    String expected = expectedText.toLowerCase(Locale.ROOT);
    List<String> cards = visibleVacancyCardTexts();
    return !cards.isEmpty() && cards.stream()
        .map(text -> text.toLowerCase(Locale.ROOT))
        .allMatch(text -> text.contains(expected));
  }

  public List<String> visibleSalaryTexts() {
    return visibleVacancyCardTexts().stream()
        .map(this::extractSalaryText)
        .filter(text -> !text.isBlank())
        .toList();
  }

  public boolean allVisibleCardsHaveSalaryText() {
    List<String> cards = visibleVacancyCardTexts();
    return !cards.isEmpty() && cards.stream()
        .map(this::extractSalaryText)
        .allMatch(text -> !text.isBlank());
  }

  public VacancySearchPage waitUntilSalaryTextsLoaded() {
    wait.until(driver -> !visibleSalaryTexts().isEmpty());
    return this;
  }

  public boolean allVisibleSalaryTextsCanContainIncome(int income) {
    List<String> salaryTexts = visibleSalaryTexts();
    return !salaryTexts.isEmpty() && salaryTexts.stream()
        .map(this::parseSalaryRange)
        .allMatch(range -> range.canContainIncome(income));
  }

  public boolean allVisibleSalaryRangesIntersect(int from, int to) {
    List<String> salaryTexts = visibleSalaryTexts();
    return !salaryTexts.isEmpty() && salaryTexts.stream()
        .map(this::parseSalaryRange)
        .allMatch(range -> range.intersects(from, to));
  }

  public List<String> vacancyLinks(int limit) {
    wait.until(driver -> !visibleVacancyLinks(limit).isEmpty());
    return visibleVacancyLinks(limit);
  }

  public VacancyPage openVacancyByIndex(int index) {
    wait.until(driver -> visibleVacancyTitles().size() > index);
    WebElement vacancyTitle = visibleVacancyTitles().get(index);
    String href = cleanVacancyLink(vacancyTitle.getAttribute("href"));
    click(vacancyTitle);
    if (!waitShortForUrlContains("/vacancy/")) {
      openPath(href);
    }
    return new VacancyPage(driver).waitUntilOpened();
  }

  public VacancyPage openVacancy(String url) {
    openPath(url);
    return new VacancyPage(driver).waitUntilOpened();
  }

  private List<WebElement> visibleVacancyTitles() {
    try {
      return elements(RESULT_TITLES).stream()
          .filter(WebElement::isDisplayed)
          .toList();
    } catch (StaleElementReferenceException exception) {
      return List.of();
    }
  }

  private List<String> visibleVacancyLinks(int limit) {
    try {
      return elements(RESULT_TITLES).stream()
          .filter(WebElement::isDisplayed)
          .map(element -> element.getAttribute("href"))
          .filter(href -> href != null && href.contains("/vacancy/"))
          .map(this::cleanVacancyLink)
          .distinct()
          .limit(limit)
          .toList();
    } catch (StaleElementReferenceException exception) {
      return List.of();
    }
  }

  private String cleanVacancyLink(String href) {
    int queryStart = href.indexOf('?');
    if (queryStart >= 0) {
      return href.substring(0, queryStart);
    }
    return href;
  }

  private String nothingFoundMessage(String query) {
    return "По запросу «" + query + "» ничего не найдено";
  }

  private String extractSalaryText(String text) {
    Matcher matcher = SALARY_TEXT.matcher(text);
    if (matcher.find()) {
      return normalizeText(matcher.group());
    }
    return "";
  }

  private SalaryRange parseSalaryRange(String salaryText) {
    String text = normalizeText(salaryText);

    Matcher range = SALARY_RANGE.matcher(text);
    if (range.find()) {
      return new SalaryRange(toNumber(range.group(1)), toNumber(range.group(2)));
    }

    Matcher from = SALARY_FROM.matcher(text);
    if (from.find()) {
      return new SalaryRange(toNumber(from.group(1)), Integer.MAX_VALUE);
    }

    Matcher to = SALARY_TO.matcher(text);
    if (to.find()) {
      return new SalaryRange(0, toNumber(to.group(1)));
    }

    Matcher exact = SALARY_EXACT.matcher(text);
    if (exact.find()) {
      int salary = toNumber(exact.group(1));
      return new SalaryRange(salary, salary);
    }

    return new SalaryRange(0, Integer.MAX_VALUE);
  }

  private int toNumber(String text) {
    return Integer.parseInt(text.replaceAll("\\D", ""));
  }

  private record SalaryRange(int from, int to) {

    private boolean canContainIncome(int income) {
      return to >= income;
    }

    private boolean intersects(int expectedFrom, int expectedTo) {
      return from <= expectedTo && to >= expectedFrom;
    }
  }
}
