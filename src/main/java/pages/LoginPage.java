package pages;

import org.openqa.selenium.WebDriver;

public class LoginPage extends Page {

  private static final String ACCOUNT_TYPE_CARD =
      "//*[@data-qa='applicant-login-card' or contains(@data-qa, 'account-type-card-APPLICANT')]";
  private static final String SUBMIT_BUTTON = "//*[@data-qa='submit-button']";
  private static final String CREDENTIAL_SWITCH = "//*[@data-qa='credential-type-switch']";
  private static final String EMAIL_CREDENTIAL = "//*[contains(@data-qa, 'credential-type-EMAIL')]";
  private static final String EMAIL_INPUT = "//*[@data-qa='applicant-login-input-email']";
  private static final String PHONE_INPUT = "//input[@inputmode='tel']";
  private static final String PASSWORD_INPUT = "//input[@type='password']";

  private static final String ERROR_MESSAGE = "//*[@data-qa='form-helper-error']";
  private static final String WAITING_FOR_SMS = "//input[@inputmode='numeric']";
  private static final String PASSWORD_BUTTON = "//*[@id=\"HH-React-Root\"]/div/div[1]/div/div/div[1]/div/div/div/div/div/div[1]/div/div/form/div/div/div[5]/button[2]";

  public LoginPage(WebDriver driver) {
    super(driver);
  }

  public LoginPage open() {
    openPath("/account/login?role=applicant");
    return waitUntilOpened();
  }

  public LoginPage waitUntilOpened() {
    visible(ACCOUNT_TYPE_CARD);
    return this;
  }

  public LoginPage continueByPhoneNumber(String query) {
    type(PHONE_INPUT, query);
    click(SUBMIT_BUTTON);
    skipIfCaptchaPresent();
    return this;
  }

  public LoginPage continueAsApplicant() {
    if (!exists(CREDENTIAL_SWITCH)) {
      click(SUBMIT_BUTTON);
    }
    visible(CREDENTIAL_SWITCH);
    return this;
  }


  public LoginPage chooseEmailCredential() {
    clickCheckable(EMAIL_CREDENTIAL);
    visible(EMAIL_INPUT);
    return this;
  }

  public LoginPage writeEmailAndSubmit(String email){
    type(EMAIL_INPUT, email);
    click(SUBMIT_BUTTON);
    skipIfCaptchaPresent();
    return this;
  }

  public LoginPage writeEmail(String email){
    type(EMAIL_INPUT, email);
    return this;
  }

  public LoginPage writePasswordAndSend(String password){
    click(PASSWORD_BUTTON);
    skipIfCaptchaPresent();
    type(PASSWORD_INPUT, password);
    click(SUBMIT_BUTTON);
    skipIfCaptchaPresent();
    return this;
  }

  public LoginPage submitEmptyEmail() {
    click(SUBMIT_BUTTON);
    skipIfCaptchaPresent();
    visible(ERROR_MESSAGE);
    return this;
  }
  public String codeInfo(){
    return visible(WAITING_FOR_SMS).getText();
  }

  public String validationError() {
    return visible(ERROR_MESSAGE).getText();
  }
}
