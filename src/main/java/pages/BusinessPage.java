package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BusinessPage extends Page {

  public BusinessPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = "//input[@id='company_email']")
  private WebElement inputCompanyEmail;

  @FindBy(xpath = "//input[@id='company_site']")
  private WebElement inputCompanySite;

  @FindBy(xpath = "//input[@id='company_url']")
  private WebElement inputCompanyUrl;

  @FindBy(xpath = "//div[@id='signup']/div/div/div[2]/form/a/div")
  private WebElement submitButton;

  public void registerCompany() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    JavascriptExecutor js = (JavascriptExecutor) driver;
    WebElement registerButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div/div/div/div[2]/a/div")));
    js.executeScript("arguments[0].click();", registerButton);

    WebElement companyName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("company_name")));
    companyName.sendKeys("maxbarsukov");
    inputCompanyEmail.sendKeys("noreply@maxbarsukov.ru");
    inputCompanySite.sendKeys("https://cv.maxbarsukov.ru");
    inputCompanyUrl.sendKeys("maxbarsukov");
  }
}
