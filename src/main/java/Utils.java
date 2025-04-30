import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Utils {

  public WebDriver driver;
  public JavascriptExecutor js;
  public WebDriverWait wait;

  public void setupDriver() {
    WebDriverManager.chromedriver().driverVersion("135.0.7049.114").setup();
    driver = new ChromeDriver();
    wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));

    driver.manage().window().setSize(new Dimension(1050, 716));
    js = (JavascriptExecutor) driver;
    driver.get("https://otvet.mail.ru/");
  }

  public WebDriver getDriver() {
    return driver;
  }

  public WebDriverWait getWaitTime() {
    return wait;
  }

  public JavascriptExecutor getJsExecutor() {
    return js;
  }

  public void quitDriver() {
    if (driver != null) {
      driver.quit();
      driver = null;
    }
  }
}
