import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public abstract class BaseBrowserTest {

  private static List<BrowserType> selectedBrowsers() {
    String browser = System.getProperty("browser", "all").trim().toUpperCase(Locale.ROOT);
    if ("ALL".equals(browser)) {
      return Arrays.asList(BrowserType.values());
    }
    return List.of(BrowserType.valueOf(browser));
  }

  protected void runInBrowser(BrowserType browserType, Consumer<WebDriver> scenario) {
    WebDriver driver = Utils.createDriver(browserType);
    try {
      scenario.accept(driver);
    } finally {
      driver.quit();
    }
  }

  protected void runInBrowsers(Consumer<WebDriver> scenario) {
    List<BrowserType> browserTypes = selectedBrowsers();
    if (browserTypes.size() == 1) {
      runInBrowser(browserTypes.get(0), scenario);
      return;
    }

    ExecutorService executor = Executors.newFixedThreadPool(browserTypes.size());
    try {
      List<Future<Void>> futures = browserTypes.stream()
          .map(browserType -> executor.submit(() -> {
            runInBrowser(browserType, scenario);
            return (Void) null;
          }))
          .toList();

      Throwable firstFailure = null;
      for (Future<Void> future : futures) {
        try {
          future.get();
        } catch (InterruptedException exception) {
          Thread.currentThread().interrupt();
          throw new RuntimeException("Browser test execution was interrupted", exception);
        } catch (ExecutionException exception) {
          if (firstFailure == null) {
            firstFailure = exception.getCause();
          } else {
            firstFailure.addSuppressed(exception.getCause());
          }
        }
      }

      if (firstFailure instanceof RuntimeException runtimeException) {
        throw runtimeException;
      }
      if (firstFailure instanceof Error error) {
        throw error;
      }
      if (firstFailure != null) {
        throw new RuntimeException(firstFailure);
      }
    } finally {
      executor.shutdownNow();
    }
  }
}
