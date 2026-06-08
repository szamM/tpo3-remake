import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class BaseBrowserTest {

  protected static Stream<BrowserType> browsers() {
    String browser = System.getProperty("browser", "all").trim().toUpperCase(Locale.ROOT);
    if ("ALL".equals(browser)) {
      return Arrays.stream(BrowserType.values());
    }
    return Stream.of(BrowserType.valueOf(browser));
  }

  protected void runInBrowser(BrowserType browserType, Consumer<WebDriver> scenario) {
    WebDriver driver = Utils.createDriver(browserType);
    try {
      scenario.accept(driver);
    } finally {
      driver.quit();
    }
  }
}
