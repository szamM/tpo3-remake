import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import pages.HomePage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(2)
public class CityChangeTest extends BaseBrowserTest {

  private static final String EXISTING_CITY = "Санкт-Петербург";
  private static final String NON_EXISTING_CITY = "ГородКоторогоНет123124";

  @Order(1)
  @Test
  void existingRegionCanBeSelectedAndUnknownRegionIsIgnored() {
    runInBrowsers(driver -> {
      HomePage homePage = new HomePage(driver)
          .open()
          .changeRegionTo(EXISTING_CITY);

      assertTrue(homePage.currentRegionContains(EXISTING_CITY));
      String selectedRegion = homePage.currentRegion();

      homePage.tryChangeRegionTo(NON_EXISTING_CITY);

      assertEquals(selectedRegion, homePage.currentRegion());
    });
  }
}
