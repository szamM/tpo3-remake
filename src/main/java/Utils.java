import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Utils {

  public static final String BASE_URL = "https://hh.ru";
  private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(35);
  private static final Duration SCRIPT_TIMEOUT = Duration.ofSeconds(15);
  private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(25);

  public WebDriver driver;
  public WebDriverWait wait;

  public void setupDriver() {
    setupDriver(BrowserType.CHROME);
  }

  public void setupDriver(BrowserType browserType) {
    driver = createDriver(browserType);
    wait = new WebDriverWait(driver, WAIT_TIMEOUT);
  }

  public static WebDriver createDriver(BrowserType browserType) {
    WebDriver driver = switch (browserType) {
      case CHROME -> createChromeDriver();
      case FIREFOX -> createFirefoxDriver();
    };

    driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);
    driver.manage().timeouts().scriptTimeout(SCRIPT_TIMEOUT);
    driver.manage().window().setSize(new Dimension(1366, 900));
    return driver;
  }

  private static WebDriver createChromeDriver() {
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();
    options.setPageLoadStrategy(PageLoadStrategy.EAGER);
    options.addArguments("--lang=ru-RU");
    options.addArguments("--window-size=1366,900");
    options.addArguments("--disable-dev-shm-usage");
    if (isHeadless()) {
      options.addArguments("--headless=new");
    }
    return new ChromeDriver(options);
  }

  private static WebDriver createFirefoxDriver() {
    WebDriverManager.firefoxdriver().setup();
    FirefoxOptions options = new FirefoxOptions();
    options.setPageLoadStrategy(PageLoadStrategy.EAGER);
    options.addPreference("intl.accept_languages", "ru-RU,ru");
    if (isHeadless()) {
      options.addArguments("-headless");
    }
    return new FirefoxDriver(options);
  }

  private static boolean isHeadless() {
    return Boolean.parseBoolean(System.getProperty("headless", "false"));
  }

  public WebDriver getDriver() {
    return driver;
  }

}
