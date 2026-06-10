import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import pages.EmployersPage;
import pages.HomePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(3)
public class EmployersSearchTest extends BaseBrowserTest {

  @Order(1)
  @Test
  void searchEmployers() {
    runInBrowsers(driver -> {
      EmployersPage employersPage = new HomePage(driver).openEmployerCatalog().openList().searchCompany("HeadHunter");
      assertTrue(employersPage.resultCount() > 0);
      assertTrue(employersPage.hasEmployerContaining("HeadHunter"));
    });
  }
}
