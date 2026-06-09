package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.opentest4j.TestAbortedException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Locale;

public abstract class Page {

  protected static final String BASE_URL = "https://hh.ru";

  protected static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(25);
  protected static final Duration SHORT_TIMEOUT = Duration.ofSeconds(5);

  protected final WebDriver driver;
  protected final WebDriverWait wait;
  protected final JavascriptExecutor js;

  @FindBy(xpath = "//*[contains(normalize-space(), 'Пройдите капчу') or contains(normalize-space(), 'капч') or contains(normalize-space(), 'Капч') or contains(normalize-space(), 'робот') or contains(normalize-space(), 'Робот') or contains(normalize-space(), 'VPN мешает') or contains(translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'captcha')]")
  private List<WebElement> captchaElements;

  @FindBy(xpath = "//body")
  private List<WebElement> bodyElements;

  @FindBy(xpath = "//button[normalize-space()='Понятно']")
  private List<WebElement> cookieAcceptButtons;

  protected Page(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    this.js = (JavascriptExecutor) driver;
    PageFactory.initElements(driver, this);
  }

  public WebDriver getDriver() {
    return driver;
  }

  public String currentUrl() {
    return driver.getCurrentUrl();
  }

  protected void openPath(String path) {
    try {
      driver.get(toAbsoluteUrl(path));
    } catch (TimeoutException exception) {
      js.executeScript("window.stop();");
    } catch (NoSuchWindowException exception) {
      throw new TestAbortedException("Окно браузера закрыто проверкой HH, тест пропущен");
    } catch (WebDriverException exception) {
      abortIfBrowserWindowClosed(exception);
      throw exception;
    }
    acceptCookiesIfPresent();
    skipIfCaptchaPresent();
  }

  protected String toAbsoluteUrl(String pathOrUrl) {
    if (pathOrUrl.startsWith("http://") || pathOrUrl.startsWith("https://")) {
      return pathOrUrl;
    }
    return BASE_URL + pathOrUrl;
  }

  protected String urlEncode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

  protected String normalizeText(String text) {
    return text
        .replace('\u00A0', ' ')
        .replace('\u202F', ' ')
        .replace('\u2009', ' ')
        .replaceAll("\\s+", " ")
        .trim();
  }

  protected void pressEscape() {
    try {
      driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
    } catch (NoSuchWindowException exception) {
      throw new TestAbortedException("Окно браузера закрыто проверкой HH, тест пропущен");
    } catch (WebDriverException exception) {
      abortIfBrowserWindowClosed(exception);
    }
  }

  protected WebElement visible(WebElement element) {
    return wait.until(driver -> {
      skipIfCaptchaPresent();
      try {
        return element.isDisplayed() ? element : null;
      } catch (NoSuchElementException | StaleElementReferenceException exception) {
        return null;
      }
    });
  }

  protected WebElement visible(List<WebElement> elements) {
    return wait.until(driver -> {
      skipIfCaptchaPresent();
      return firstDisplayed(elements);
    });
  }

  protected WebElement present(WebElement element) {
    return wait.until(driver -> {
      skipIfCaptchaPresent();
      try {
        element.getTagName();
        return element;
      } catch (NoSuchElementException | StaleElementReferenceException exception) {
        return null;
      }
    });
  }

  protected WebElement present(By locator) {
    return wait.until(driver -> {
      skipIfCaptchaPresent();
      try {
        return driver.findElement(locator);
      } catch (NoSuchElementException | StaleElementReferenceException exception) {
        return null;
      }
    });
  }

  protected boolean isCaptchaPresent() {
    try {
      String currentUrl = driver.getCurrentUrl().toLowerCase(Locale.ROOT);
      if (currentUrl.contains("captcha")
          || currentUrl.contains("vpncheck")
          || currentUrl.contains("vpncheeck")) {
        return true;
      }

      if (captchaElements.stream().anyMatch(WebElement::isDisplayed)) {
        return true;
      }

      if (bodyElements.isEmpty()) {
        return false;
      }
      String bodyText = normalizeText(bodyElements.get(0).getText()).toLowerCase(Locale.ROOT);
      return bodyText.contains("капч")
          || bodyText.contains("captcha")
          || bodyText.contains("робот")
          || bodyText.contains("vpn мешает");
    } catch (NoSuchWindowException exception) {
      throw new TestAbortedException("Окно браузера закрыто проверкой HH, тест пропущен");
    } catch (NoSuchElementException | StaleElementReferenceException exception) {
      return false;
    } catch (WebDriverException exception) {
      abortIfBrowserWindowClosed(exception);
      return false;
    }
  }

  protected void skipIfCaptchaPresent() {
    if (isCaptchaPresent()) {
      throw new TestAbortedException("Появилась капча или проверка HH, тест пропущен");
    }
  }

  private void abortIfBrowserWindowClosed(WebDriverException exception) {
    String message = exception.getMessage();
    if (message != null
        && (message.contains("no such window")
        || message.contains("web view not found")
        || message.contains("target window already closed"))) {
      throw new TestAbortedException("Окно браузера закрыто проверкой HH, тест пропущен");
    }
  }

  protected List<WebElement> elements(List<WebElement> locatedElements) {
    skipIfCaptchaPresent();
    return locatedElements.stream().toList();
  }

  protected boolean exists(WebElement element) {
    try {
      element.getTagName();
      return true;
    } catch (NoSuchElementException | StaleElementReferenceException exception) {
      return false;
    }
  }

  protected boolean exists(List<WebElement> elements) {
    skipIfCaptchaPresent();
    return !elements.isEmpty();
  }

  protected boolean exists(By locator) {
    skipIfCaptchaPresent();
    return !driver.findElements(locator).isEmpty();
  }

  protected void type(WebElement element, String text) {
    WebElement visibleElement = visible(element);
    js.executeScript("arguments[0].focus();", visibleElement);
    visibleElement.clear();
    visibleElement.sendKeys(text);
    wait.until(driver -> {
      skipIfCaptchaPresent();
      return text.equals(visibleElement.getAttribute("value"));
    });
  }

  protected void typeMasked(WebElement element, String text) {
    WebElement visibleElement = visible(element);
    js.executeScript("arguments[0].focus();", visibleElement);
    visibleElement.clear();
    visibleElement.sendKeys(text);
    if (text.isBlank()) {
      return;
    }
    wait.until(driver -> {
      skipIfCaptchaPresent();
      String value = visibleElement.getAttribute("value");
      return onlyDigits(value).contains(onlyDigits(text));
    });
  }

  protected void click(WebElement element) {
    WebElement visibleElement = visible(element);
    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", visibleElement);
    try {
      visibleElement.click();
    } catch (RuntimeException exception) {
      js.executeScript("arguments[0].click();", visibleElement);
    }
  }

  protected void clickCheckable(WebElement element) {
    WebElement input = present(element);
    try {
      input.click();
    } catch (RuntimeException exception) {
      Object label = js.executeScript("return arguments[0].closest('label');", input);
      if (label instanceof WebElement labelElement) {
        js.executeScript("arguments[0].click();", labelElement);
      } else {
        js.executeScript("arguments[0].click();", input);
      }
    }
  }

  protected void clickCheckable(By locator) {
    clickCheckable(present(locator));
  }

  protected void waitForUrlContains(String urlPart) {
    wait.until(driver -> {
      skipIfCaptchaPresent();
      return driver.getCurrentUrl().contains(urlPart);
    });
  }

  protected boolean waitShortForUrlContains(String urlPart) {
    try {
      new WebDriverWait(driver, SHORT_TIMEOUT).until(driver -> {
        skipIfCaptchaPresent();
        return driver.getCurrentUrl().contains(urlPart);
      });
      return true;
    } catch (TimeoutException exception) {
      return false;
    }
  }

  protected boolean waitShortForVisible(WebElement element) {
    try {
      new WebDriverWait(driver, SHORT_TIMEOUT).until(driver -> {
        skipIfCaptchaPresent();
        try {
          return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException exception) {
          return false;
        }
      });
      return true;
    } catch (TimeoutException exception) {
      return false;
    }
  }

  protected boolean waitShortForVisible(List<WebElement> elements) {
    try {
      new WebDriverWait(driver, SHORT_TIMEOUT).until(driver -> {
        skipIfCaptchaPresent();
        return firstDisplayed(elements) != null;
      });
      return true;
    } catch (TimeoutException exception) {
      return false;
    }
  }

  protected void acceptCookiesIfPresent() {
    if (!cookieAcceptButtons.isEmpty()) {
      js.executeScript("arguments[0].click();", cookieAcceptButtons.get(0));
    }
  }

  protected String bodyText() {
    return normalizeText(visible(bodyElements).getText());
  }

  private String onlyDigits(String text) {
    return text == null ? "" : text.replaceAll("\\D", "");
  }

  private WebElement firstDisplayed(List<WebElement> elements) {
    try {
      return elements.stream()
          .filter(WebElement::isDisplayed)
          .findFirst()
          .orElse(null);
    } catch (NoSuchElementException | StaleElementReferenceException exception) {
      return null;
    }
  }
}
