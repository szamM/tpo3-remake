import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.HomePage;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
public class LoginValidationTest extends BaseBrowserTest {

  private static final String CORRECT_PHONE = "9211924046";
  private static final String INCORRECT_PHONE = "123";
  private static final String INCORRECT_EMAIL = "pupupu@mailru";
  private static final String CORRECT_EMAIL = "pupupu@mail.ru";
  private static final String PASSWORD = "pupupu";
  private static final String EMPTY = "";

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void submitPhone(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      LoginPage loginPage = new HomePage(driver)
        .open()
        .openLogin()
        .continueAsApplicant()
        .continueByPhoneNumber(CORRECT_PHONE);
      assertTrue(loginPage.codeInfo().contains("Введите код из смс"));
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void submitEmptyPhone(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      LoginPage loginPage = new HomePage(driver)
        .open()
        .openLogin()
        .continueAsApplicant()
        .continueByPhoneNumber(EMPTY);
      assertTrue(loginPage.validationError().contains("Обязательное поле"));
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void submitIncorrectPhone(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      LoginPage loginPage = new HomePage(driver)
        .open()
        .openLogin()
        .continueAsApplicant()
        .continueByPhoneNumber(INCORRECT_PHONE);
      assertTrue(loginPage.validationError().contains("Указан некорректный номер телефона"));
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void emptyEmailShowsValidationError(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      LoginPage loginPage = new HomePage(driver)
        .open()
        .openLogin()
        .continueAsApplicant()
        .chooseEmailCredential()
        .submitEmptyEmail();

      assertTrue(loginPage.validationError().contains("Обязательное поле"));
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void IncorrectEmailShowsValidationError(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      LoginPage loginPage = new HomePage(driver)
        .open()
        .openLogin()
        .continueAsApplicant()
        .chooseEmailCredential()
        .writeEmailAndSubmit(INCORRECT_EMAIL);

      assertTrue(loginPage.validationError().contains("Указан некорректный e-mail"));
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void CorrectEmailShows(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      LoginPage loginPage = new HomePage(driver)
        .open()
        .openLogin()
        .continueAsApplicant()
        .chooseEmailCredential()
        .writeEmailAndSubmit(CORRECT_EMAIL);
      assertTrue(loginPage.codeInfo().contains("Введите код из письма"));
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void CorrectEmailAndPassword(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      LoginPage loginPage = new HomePage(driver)
        .open()
        .openLogin()
        .continueAsApplicant()
        .chooseEmailCredential()
        .writeEmail(CORRECT_EMAIL)
        .writePasswordAndSend(PASSWORD);
      assertTrue(loginPage.validationError().contains("Неправильные данные для входа. Пожалуйста, попробуйте снова."));
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void correctEmailAndEmptyPassword(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      LoginPage loginPage = new HomePage(driver)
        .open()
        .openLogin()
        .continueAsApplicant()
        .chooseEmailCredential()
        .writeEmail(CORRECT_EMAIL)
        .writePasswordAndSend(EMPTY);
      assertTrue(loginPage.validationError().contains("Обязательное поле"));
    });
  }
}
