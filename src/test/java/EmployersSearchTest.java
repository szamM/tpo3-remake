import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.EmployersPage;
import pages.HomePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
public class EmployersSearchTest extends BaseBrowserTest {

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void searchEmployers(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
      EmployersPage employersPage = new HomePage(driver).openEmployerCatalog().openList().searchCompany("HeadHunter");
      assertTrue(employersPage.resultCount() > 0);
      assertTrue(employersPage.hasEmployerContaining("HeadHunter"));
    });
  }
}
