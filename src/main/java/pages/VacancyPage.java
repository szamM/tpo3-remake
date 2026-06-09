package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class VacancyPage extends Page {

  @FindBy(xpath = "//body")
  private WebElement pageBody;

  @FindBy(xpath = "//h1")
  private WebElement vacancyTitle;

  @FindBy(xpath = "//p[contains(., 'Опыт работы')]")
  private WebElement experienceText;

  @FindBy(xpath = "//*[@data-qa='vacancy-response-link-top']")
  private WebElement responseButton;

  public VacancyPage(WebDriver driver) {
    super(driver);
  }

  public VacancyPage waitUntilOpened() {
    visible(vacancyTitle);
    return this;
  }

  public String title() {
    return visible(vacancyTitle).getText();
  }

  public String experience() {
    return visible(experienceText).getText();
  }

  public boolean hasResponseButton() {
    return exists(responseButton);
  }

  public String pageText() {
    return normalizeText(visible(pageBody).getText());
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
