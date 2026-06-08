package pages;

import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class VacancyPage extends Page {

  private static final String PAGE_BODY = "//body";
  private static final String VACANCY_TITLE = "//h1";
  private static final String EXPERIENCE_TEXT = "//p[contains(., 'Опыт работы')]";
  private static final String RESPONSE_BUTTON = "//*[@data-qa='vacancy-response-link-top']";

  public VacancyPage(WebDriver driver) {
    super(driver);
  }

  public VacancyPage waitUntilOpened() {
    visible(VACANCY_TITLE);
    return this;
  }

  public String title() {
    return visible(VACANCY_TITLE).getText();
  }

  public String experience() {
    return visible(EXPERIENCE_TEXT).getText();
  }

  public boolean hasResponseButton() {
    return exists(RESPONSE_BUTTON);
  }

  public String pageText() {
    return normalizeText(visible(PAGE_BODY).getText());
  }

  public boolean containsAllQueryWords(String query) {
    String text = pageText().toLowerCase(Locale.ROOT);
    List<String> words = Arrays.stream(query.toLowerCase(Locale.ROOT).split("\\s+"))
        .filter(word -> word.length() > 1)
        .toList();
    return !words.isEmpty() && words.stream().allMatch(text::contains);
  }

  public boolean hasNoExperience() {
    String experience = experience().toLowerCase(Locale.ROOT);
    return experience.contains("не требуется") || experience.contains("без опыта");
  }

  public boolean hasFullEmployment() {
    return pageText().toLowerCase(Locale.ROOT).contains("полная занятость");
  }

  public VacancySearchPage backToSearchResults(String query) {
    driver.navigate().back();
    return new VacancySearchPage(driver).waitUntilSearchFinished(query);
  }
}
