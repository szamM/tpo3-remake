package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.opentest4j.TestAbortedException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public abstract class Page {

  protected static final String BASE_URL = "https://hh.ru";
  protected static final String CAPTCHA_TEXT = "//*[contains(normalize-space(), 'Пройдите капчу')]";

  protected static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(25);
  protected static final Duration SHORT_TIMEOUT = Duration.ofSeconds(5);

  protected final WebDriver driver;
  protected final WebDriverWait wait;
  protected final JavascriptExecutor js;

  protected Page(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    this.js = (JavascriptExecutor) driver;
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
    }
//    acceptCookiesIfPresent();
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
    driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
  }

  protected WebElement visible(String xpath) {
    return wait.until(driver -> {
      skipIfCaptchaPresent();
      try {
        WebElement element = driver.findElement(By.xpath(xpath));
        return element.isDisplayed() ? element : null;
      } catch (NoSuchElementException | StaleElementReferenceException exception) {
        return null;
      }
    });
  }

  protected WebElement visible(WebElement element) {
    return wait.until(driver -> {
      skipIfCaptchaPresent();
      try {
        return element.isDisplayed() ? element : null;
      } catch (StaleElementReferenceException exception) {
        return null;
      }
    });
  }

  protected WebElement present(String xpath) {
    return wait.until(driver -> {
      skipIfCaptchaPresent();
      try {
        return driver.findElement(By.xpath(xpath));
      } catch (NoSuchElementException | StaleElementReferenceException exception) {
        return null;
      }
    });
  }

  protected boolean isCaptchaPresent() {
    try {
      List<WebElement> captchaElements = driver.findElements(By.xpath(CAPTCHA_TEXT));
      return captchaElements.stream().anyMatch(WebElement::isDisplayed);
    } catch (StaleElementReferenceException exception) {
      return false;
    }
  }

  protected void skipIfCaptchaPresent() {
    if (isCaptchaPresent()) {
      throw new TestAbortedException("Появилась капча, тест пропущен");
    }
  }

  protected List<WebElement> elements(String xpath) {
    skipIfCaptchaPresent();
    return driver.findElements(By.xpath(xpath));
  }

  protected boolean exists(String xpath) {
    return !elements(xpath).isEmpty();
  }

  protected void type(String xpath, String text) {
    WebElement element = visible(xpath);
    element.clear();
    element.sendKeys(text);
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

  protected void click(String xpath) {
    WebElement element = visible(xpath);
    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    try {
      element.click();
    } catch (RuntimeException exception) {
      js.executeScript("arguments[0].click();", element);
    }
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

  protected void clickCheckable(String inputXpath) {
    WebElement input = present(inputXpath);
    try {
      input.click();
    } catch (RuntimeException exception) {
      List<WebElement> labels = driver.findElements(By.xpath(inputXpath + "/ancestor::label"));
      if (!labels.isEmpty()) {
        js.executeScript("arguments[0].click();", labels.get(0));
      } else {
        js.executeScript("arguments[0].click();", input);
      }
    }
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

  protected boolean waitShortForVisible(String xpath) {
    try {
      new WebDriverWait(driver, SHORT_TIMEOUT).until(driver -> {
        skipIfCaptchaPresent();
        try {
          return driver.findElement(By.xpath(xpath)).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException exception) {
          return false;
        }
      });
      return true;
    } catch (TimeoutException exception) {
      return false;
    }
  }

  protected void acceptCookiesIfPresent() {
    List<WebElement> buttons = driver.findElements(By.xpath("//button[normalize-space()='Понятно']"));
    if (!buttons.isEmpty()) {
      js.executeScript("arguments[0].click();", buttons.get(0));
    }
  }
}
