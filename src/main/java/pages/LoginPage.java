package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends Page {

  @FindBy(xpath = "//*[@data-qa='applicant-login-card' or contains(@data-qa, 'account-type-card-APPLICANT')]")
  private WebElement accountTypeCard;

  @FindBy(xpath = "//*[@data-qa='submit-button']")
  private WebElement submitButton;

  @FindBy(xpath = "//*[@data-qa='credential-type-switch']")
  private WebElement credentialSwitch;

  @FindBy(xpath = "//*[contains(@data-qa, 'credential-type-EMAIL')]")
  private WebElement emailCredential;

  @FindBy(xpath = "//*[@data-qa='applicant-login-input-email']")
  private WebElement emailInput;

  @FindBy(xpath = "//input[@inputmode='tel']")
  private WebElement phoneInput;

  @FindBy(xpath = "//input[@type='password']")
  private WebElement passwordInput;

  @FindBy(xpath = "//*[@data-qa='form-helper-error']")
  private WebElement errorMessage;

  @FindBy(xpath = "//*[@id='HH-React-Root']/div/div[1]/div/div/div[1]/div/div/div/div/div/div[1]/div/div/form/div/div/div[5]/button[2]")
  private WebElement passwordButton;

  public LoginPage(WebDriver driver) {
    super(driver);
  }

  public LoginPage open() {
    openPath("/account/login?role=applicant");
    return waitUntilOpened();
  }

  public LoginPage waitUntilOpened() {
    visible(accountTypeCard);
    return this;
  }

  public LoginPage continueByPhoneNumber(String query) {
    typeMasked(phoneInput, query);
    click(submitButton);
    skipIfCaptchaPresent();
    return this;
  }

  public LoginPage continueAsApplicant() {
    if (!exists(credentialSwitch)) {
      click(submitButton);
    }
    visible(credentialSwitch);
    return this;
  }


  public LoginPage chooseEmailCredential() {
    clickCheckable(emailCredential);
    visible(emailInput);
    return this;
  }

  public LoginPage writeEmailAndSubmit(String email){
    type(emailInput, email);
    click(submitButton);
    skipIfCaptchaPresent();
    return this;
  }

  public LoginPage writeEmail(String email){
    type(emailInput, email);
    return this;
  }

  public LoginPage writePasswordAndSend(String password){
    click(passwordButton);
    skipIfCaptchaPresent();
    type(passwordInput, password);
    click(submitButton);
    skipIfCaptchaPresent();
    return this;
  }

  public LoginPage submitEmptyEmail() {
    click(submitButton);
    skipIfCaptchaPresent();
    visible(errorMessage);
    return this;
  }
  public String codeInfo() {
    return wait.until(driver -> {
      skipIfCaptchaPresent();
      String pageText = bodyText();
      return pageText.contains("Введите код") ? pageText : null;
    });
  }

  public String validationError() {
    return visible(errorMessage).getText();
  }
}
