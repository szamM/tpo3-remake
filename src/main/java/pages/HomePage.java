package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomePage extends Page {

  public HomePage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[3]/a")
  private WebElement askButton;

  @FindBy(xpath = "//div[@id='ph-whiteline']/div/div/button")
  private WebElement enterButton;

  @FindBy(xpath = "//div/div/div/div/div/div/form/div/div/div/div/span/div/div[2]/div/div/div/input")
  private WebElement askQuestionInput;

  @FindBy(xpath = "//div[@id='ph-whiteline']/div/div/a")
  private WebElement registrationButton;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[4]/a")
  private WebElement leadersButton;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[5]/a")
  private WebElement forBusinessButton;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div/span")
  private WebElement categoriesButton;

  @FindBy(xpath = "//input[@id='search']")
  private WebElement searchByQuestionInput;

  @FindBy(xpath = "//div[2]/div/div[2]/div/div[2]/div/div[2]/div/div[2]/div/div")
  private WebElement openButton;

  @FindBy(xpath = "//div[2]/div/div[2]/div/div[2]/div/div[2]/div/div[2]/div/div[2]")
  private WebElement onVotingButton;

  @FindBy(xpath = "//div[2]/div/div[2]/div/div[3]")
  private WebElement bestButton;

  @FindBy(xpath = "//div[@id='root']/div/div/div/div[2]")
  private WebElement enterAccountModelPage;

  @FindBy(xpath = "//input[@name='username']")
  private WebElement accountNameInput;

  @FindBy(xpath = "//div[@data-test-id='error-footer-text']/small[@data-test-id='required']")
  private WebElement accountNameError;

  @FindBy(xpath = "//div[@id='root']/div/div/div/div[2]/div/form/div[2]/div[2]/div/div/div/div/div/div/div[3]/div/div/div/div")
  private WebElement mailChoseDropdown;

  @FindBy(xpath = "//div[@id='react-select-2-option-0']/div/div/div[2]/span")
  private WebElement secondDropdownOption;

  @FindBy(xpath = "//div[@id='root']/div/div/div/div[2]/div/form/div[2]/div[2]/div[3]/div/div/div/button")
  private WebElement enterModelPageButton;

  @FindBy(xpath = "//div[@id='root']/div/div/div/div[2]/div/form/div[2]/div/div/div/div/div/div/div/div/div/div[2]/div[6]/div/div/input")
  private WebElement firstNumber;

  @FindBy(xpath = "//div[@id='root']/div/div/div/div[2]/div/form/div[2]/div/div/div/div/div/div/div/div/div/div[2]/div[7]/div/div/input")
  private WebElement secondNumber;

  @FindBy(xpath = "//div[@id='root']/div/div/div/div[2]/div/form/div[2]/div/div/div/div/div/div/div/div/span/input")
  private WebElement codeInput;

  @FindBy(xpath = "//div[@id='root']/div/div/div/div[2]/div/form/div[2]/div/div[2]/div/button")
  private WebElement submitEnterButton;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[9]/div")
  private WebElement bellButton;

  @FindBy(xpath = "//div[2]/div[2]/a")
  private WebElement chosenQuestion;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[6]/form/div/div/div[2]/a/div")
  private WebElement magnifierButton;

  @FindBy(xpath = "//div[@id='ph-whiteline']/div/div/div/span[2]")
  private WebElement logoutButton;

  @FindBy(xpath = "//div[@id='ph-whiteline']/div/div[2]/div/div[2]/div/div[2]/div/div[2]")
  private WebElement exitButton;

  @FindBy(xpath = "//div[3]/div/div[2]/div[2]/a")
  private WebElement question;

  @FindBy(xpath = "//h1")
  private WebElement heading;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div/div[2]/a")
  private WebElement homeworkCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div/div[3]/a")
  private WebElement autoCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div/div[4]/a")
  private WebElement businessCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div/div[5]/a")
  private WebElement citiesAndCountriesCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div/div[6]/a")
  private WebElement magicCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div/div[7]/a")
  private WebElement entertainmentCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div/div[8]/a")
  private WebElement foodCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div/div[9]/a")
  private WebElement animalsAndPlantsCategories;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div/div[10]/a")
  private WebElement meetingsCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div/div[11]/a")
  private WebElement artAndCulture;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div[2]/div/a")
  private WebElement computerAndGamesCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div[2]/div[2]/a")
  private WebElement computerAndTechnologies;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div[2]/div[3]/a")
  private WebElement beautyAndHealthCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div[2]/div[4]/a")
  private WebElement scienceCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div[2]/div[5]/a")
  private WebElement educationCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div[2]/div[6]/a")
  private WebElement societyCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div[2]/div[7]/a")
  private WebElement programmingCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div[2]/div[8]/a")
  private WebElement adventuresCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div[2]/div[9]/a")
  private WebElement workAndCareerCategory;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div[2]/div[10]/a")
  private WebElement homeAndFamily;

  @FindBy(xpath = "//div[@id='portal-menu']/div/div/div[2]/div[2]/div[2]/div[11]/a")
  private WebElement sportCategory;


  public void clickEnterButton() {
    enterButton.click();
  }

  public void enterLoginAndChooseMailType(String login) {
    mailChoseDropdown.click();
    secondDropdownOption.click();
    accountNameInput.sendKeys(login);
    enterModelPageButton.click();
  }

  public void enterNumbersAndCode(String first, String second) {
    firstNumber.click();
    firstNumber.sendKeys(first);
    secondNumber.click();
    secondNumber.sendKeys(second);
  }

  public void submitLogin() {
    submitEnterButton.click();
    driver.switchTo().defaultContent();
    bellButton.click();
  }

  public void clickLeadersButton() {
    leadersButton.click();
  }

  public void clickOnVotingButton() {
    onVotingButton.click();
    chosenQuestion.click();
  }

  public void checkAccountError() {
    assertEquals(heading.getAccessibleName(), "Поле «Имя аккаунта» должно быть заполнено");
  }

  public void clickBestButton() {
    bestButton.click();
    chosenQuestion.click();
  }

  public void clickOpenButton() {
    openButton.click();
    chosenQuestion.click();
  }

  public void searchQuestion() {
    searchByQuestionInput.click();
    searchByQuestionInput.sendKeys("Тортик рецепты");
    magnifierButton.click();
  }

  public void searchInvalidQuestion() {
    searchByQuestionInput.click();
    searchByQuestionInput.sendKeys("\t");
    magnifierButton.click();
  }

  public void logout() {
    logoutButton.click();
    exitButton.click();
  }

  public void clickOnQuestion() {
    question.click();
  }

  public void clickAskButton() {
    askButton.click();
  }

  public void clickBusinessButton() {
    forBusinessButton.click();
  }

  public void categoriesClicker() {
    categoriesButton.click();
    homeworkCategory.click();
    assertEquals(heading.getAccessibleName(), "Домашние задания");
    categoriesButton.click();
    autoCategory.click();
    assertEquals(heading.getAccessibleName(), "Авто, Мото");
    categoriesButton.click();
    businessCategory.click();
    assertEquals(heading.getAccessibleName(), "Бизнес, Финансы");
    categoriesButton.click();
    citiesAndCountriesCategory.click();
    assertEquals(heading.getAccessibleName(), "Города и Страны");
    categoriesButton.click();
    magicCategory.click();
    assertEquals(heading.getAccessibleName(), "Гороскопы, Магия, Гадания");
    categoriesButton.click();
    entertainmentCategory.click();
    assertEquals(heading.getAccessibleName(), "Досуг, Развлечения");
    categoriesButton.click();
    foodCategory.click();
    assertEquals(heading.getAccessibleName(), "Еда, Кулинария");
    categoriesButton.click();
    animalsAndPlantsCategories.click();
    assertEquals(heading.getAccessibleName(), "Животные, Растения");
    categoriesButton.click();
    meetingsCategory.click();
    assertEquals(heading.getAccessibleName(), "Знакомства, Любовь, Отношения");
    categoriesButton.click();
    artAndCulture.click();
    assertEquals(heading.getAccessibleName(), "Искусство и Культура");
    categoriesButton.click();
    computerAndGamesCategory.click();
    assertEquals(heading.getAccessibleName(), "Компьютерные и Видео игры");
    categoriesButton.click();
    computerAndTechnologies.click();
    assertEquals(heading.getAccessibleName(), "Компьютеры, Связь");
    categoriesButton.click();
    beautyAndHealthCategory.click();
    assertEquals(heading.getAccessibleName(), "Красота и Здоровье");
    categoriesButton.click();
    scienceCategory.click();
    assertEquals(heading.getAccessibleName(), "Наука, Техника, Языки");
    categoriesButton.click();
    educationCategory.click();
    assertEquals(heading.getAccessibleName(), "Образование");
    categoriesButton.click();
    societyCategory.click();
    assertEquals(heading.getAccessibleName(), "Общество, Политика, СМИ");
    categoriesButton.click();
    programmingCategory.click();
    assertEquals(heading.getAccessibleName(), "Программирование");
    categoriesButton.click();
    adventuresCategory.click();
    assertEquals(heading.getAccessibleName(), "Путешествия, Туризм");
    categoriesButton.click();
    workAndCareerCategory.click();
    assertEquals(heading.getAccessibleName(), "Работа, Карьера");
    categoriesButton.click();
    homeAndFamily.click();
    assertEquals(heading.getAccessibleName(), "Семья, Дом, Дети");
    categoriesButton.click();
    sportCategory.click();
    assertEquals(heading.getAccessibleName(), "Спорт");
  }
}
