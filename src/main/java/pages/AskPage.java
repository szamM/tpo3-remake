package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AskPage extends Page {

  public AskPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = "//div[2]/ul/li/a")
  private WebElement questionInset;

  @FindBy(xpath = "//div[2]/ul/li[2]/a")
  private WebElement pollInset;

  @FindBy(xpath = "//textarea[@id='question_text']")
  private WebElement questionTopicTextArea;

  @FindBy(xpath = "//div[@id='question_additional']/div[2]/div")
  private WebElement questionTextArea;

  @FindBy(xpath = "//label/div[2]/div/div/div/span")
  private WebElement categoryDropdown;

  @FindBy(xpath = "//div[17]/div/span")
  private WebElement programmingOption;

  @FindBy(xpath = "//div[@name='select_childs_categories']")
  private WebElement subCategoryDropdown;

  @FindBy(xpath = "//div[15]/div/span")
  private WebElement otherTechnologiesOption;

  @FindBy(xpath = "//div[4]/a/div")
  private WebElement questionPublicationButton;

  @FindBy(css = ".Z6xu6:nth-child(3) svg")
  private WebElement allowManyOptions;

  @FindBy(css = ".Z6xu6:nth-child(2) svg")
  private WebElement getMessageCheckbox;

  @FindBy(css = ".Z6xu6:nth-child(1) svg")
  private WebElement allowCommentsCheckbox;

  @FindBy(xpath = "//input[@id='poll_option']")
  private WebElement firstPollOption;

  @FindBy(xpath = "(//input[@id='poll_option'])[2]")
  private WebElement secondPollOption;

  @FindBy(xpath = "(//input[@id='poll_option'])[3]")
  private WebElement thirdPollOption;

  @FindBy(xpath = "(//input[@id='poll_option'])[4]")
  private WebElement fourthPollOption;

  public void manageDropdowns() {
    categoryDropdown.click();
    programmingOption.click();
    subCategoryDropdown.click();
    otherTechnologiesOption.click();
  }

  public void createPoll() {
    pollInset.click();
    questionTopicTextArea.click();
    questionTopicTextArea.sendKeys("Это тестовый опрос для проверки работы Selenium WebDriver");
    questionTextArea.click();
    questionTextArea.sendKeys("Просто тест опроса");
    firstPollOption.click();
    firstPollOption.sendKeys("1");
    secondPollOption.click();
    secondPollOption.sendKeys("2");
    thirdPollOption.click();
    thirdPollOption.sendKeys("3");
    fourthPollOption.click();
    fourthPollOption.sendKeys("4");
    manageDropdowns();
    allowCommentsCheckbox.click();
    getMessageCheckbox.click();
    getMessageCheckbox.click();
    allowManyOptions.click();
    allowManyOptions.click();
    questionPublicationButton.click();
  }

  public void createQuestion() {
    questionInset.click();
    questionTopicTextArea.click();
    questionTopicTextArea.sendKeys("Это просто тестовый вопрос для проверки работы Selenium WebDriver");
    questionTextArea.click();
    questionTextArea.sendKeys("Просто тест вопроса");
    manageDropdowns();
    allowCommentsCheckbox.click();
    allowCommentsCheckbox.click();
    getMessageCheckbox.click();
    getMessageCheckbox.click();
  }

  public void createInvalidQuestion() {
    questionInset.click();
    questionTopicTextArea.click();
    questionTopicTextArea.sendKeys("\t\t\t");
    questionTextArea.click();
    questionTextArea.sendKeys("   ");
    manageDropdowns();
    allowCommentsCheckbox.click();
    allowCommentsCheckbox.click();
    getMessageCheckbox.click();
    getMessageCheckbox.click();
  }
}
