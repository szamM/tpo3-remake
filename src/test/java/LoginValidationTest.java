import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import pages.HomePage;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(7)
public class LoginValidationTest extends BaseBrowserTest {

  private static final String CORRECT_PHONE = "9211924046";
  private static final String INCORRECT_PHONE = "123";
  private static final String INCORRECT_EMAIL = "pupupu@mailru";
  private static final String CORRECT_EMAIL = "pupupu@mail.ru";
  private static final String PASSWORD = "pupupu";
  private static final String EMPTY = "";

  @Order(1)
  @Test
  void submitPhone() {
    runInBrowsers(driver -> {
      LoginPage loginPage = new HomePage(driver)
        .open()
        .openLogin()
        .continueAsApplicant()
        .continueByPhoneNumber(CORRECT_PHONE);
      assertTrue(loginPage.codeInfo().contains("Введите код из смс"));
    });
  }

  @Order(2)
  @Test
  void submitEmptyPhone() {
    runInBrowsers(driver -> {
      LoginPage loginPage = new HomePage(driver)
        .open()
        .openLogin()
        .continueAsApplicant()
        .continueByPhoneNumber(EMPTY);
      assertTrue(loginPage.validationError().contains("Обязательное поле"));
    });
  }

  @Order(3)
  @Test
  void submitIncorrectPhone() {
    runInBrowsers(driver -> {
      LoginPage loginPage = new HomePage(driver)
        .open()
        .openLogin()
        .continueAsApplicant()
        .continueByPhoneNumber(INCORRECT_PHONE);
      assertTrue(loginPage.validationError().contains("Указан некорректный номер телефона"));
    });
  }

  @Order(4)
  @Test
  void emptyEmailShowsValidationError() {
    runInBrowsers(driver -> {
      LoginPage loginPage = new HomePage(driver)
        .open()
        .openLogin()
        .continueAsApplicant()
        .chooseEmailCredential()
        .submitEmptyEmail();

      assertTrue(loginPage.validationError().contains("Обязательное поле"));
    });
  }

  @Order(5)
  @Test
  void IncorrectEmailShowsValidationError() {
    runInBrowsers(driver -> {
      LoginPage loginPage = new HomePage(driver)
        .open()
        .openLogin()
        .continueAsApplicant()
        .chooseEmailCredential()
        .writeEmailAndSubmit(INCORRECT_EMAIL);

      assertTrue(loginPage.validationError().contains("Указан некорректный e-mail"));
    });
  }

  @Order(6)
  @Test
  void CorrectEmailShows() {
    runInBrowsers(driver -> {
      LoginPage loginPage = new HomePage(driver)
        .open()
        .openLogin()
        .continueAsApplicant()
        .chooseEmailCredential()
        .writeEmailAndSubmit(CORRECT_EMAIL);
      assertTrue(loginPage.codeInfo().contains("Введите код из письма"));
    });
  }

  @Order(7)
  @Test
  void CorrectEmailAndPassword() {
    runInBrowsers(driver -> {
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

  @Order(8)
  @Test
  void correctEmailAndEmptyPassword() {
    runInBrowsers(driver -> {
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
