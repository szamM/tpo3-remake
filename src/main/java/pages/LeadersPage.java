package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LeadersPage extends Page {

  public LeadersPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = "//label/div/div/div/div")
  private WebElement timeDropdown;

  @FindBy(xpath = "//div[3]/label/div/div/div/div")
  private WebElement categoriesDropdown;

  @FindBy(xpath = "//div[2]/label/div/div/div/div")
  private WebElement activeDropdown;

  @FindBy(xpath = "//div[4]/label/div/div/div/div")
  private WebElement subcategoryDropdown;

  @FindBy(xpath = "//div[2]/div[2]/div/span")
  private WebElement forMonthVariant;

  @FindBy(xpath = "//div[18]/div/span")
  private WebElement programmingVariant;

  @FindBy(xpath = "//div[2]/div[2]/div/span")
  private WebElement forCountOfAnswersVariant;

  @FindBy(xpath = "//div[16]/div/span")
  private WebElement otherLanguagesAndTechnologiesVariant;


  public void searchLeaders() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    timeDropdown.click();
    forMonthVariant.click();
    activeDropdown.click();
    forCountOfAnswersVariant.click();
    categoriesDropdown.click();
    programmingVariant.click();
    wait.until(ExpectedConditions.elementToBeClickable(subcategoryDropdown));
    subcategoryDropdown.click();
    otherLanguagesAndTechnologiesVariant.click();
  }
}
