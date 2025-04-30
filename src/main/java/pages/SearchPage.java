package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchPage extends Page {

  public SearchPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = "//label/div/div/div/div/span")
  private WebElement categoriesDropdown;

  @FindBy(xpath = "//div[8]/div/span")
  private WebElement foodOption;

  @FindBy(xpath = "//div[2]/label/div/div/div/div/span")
  private WebElement subCategoriesDropdown;

  @FindBy(xpath = "//div[12]/div/span")
  private WebElement holidayOption;

  @FindBy(xpath = "//div[3]/span")
  private WebElement advancedSearch;

  @FindBy(xpath = "//div[3]/label/div/div/div/div/span")
  private WebElement timeDropDown;

  @FindBy(xpath = "//div[2]/div[6]/div/span")
  private WebElement forYearOption;

  @FindBy(xpath = "//div[4]/label/div/div/div/div/span")
  private WebElement questionTypeOption;

  @FindBy(xpath = "//div[2]/div[2]/div/span")
  private WebElement openQuestionsOption;

  @FindBy(css = ".XR_wi > svg")
  private WebElement onlyInQuestionCheckbox;

  @FindBy(xpath = "//div[4]/div/div[2]/div[2]/div/a")
  private WebElement question;

  public void chooseQuestion() {
    categoriesDropdown.click();
    foodOption.click();
    subCategoriesDropdown.click();
    holidayOption.click();
    advancedSearch.click();
    timeDropDown.click();
    forYearOption.click();
    questionTypeOption.click();
    openQuestionsOption.click();
    onlyInQuestionCheckbox.click();
    question.click();
  }
}
