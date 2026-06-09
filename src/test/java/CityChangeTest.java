import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.HomePage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
public class CityChangeTest extends BaseBrowserTest {

  private static final String EXISTING_CITY = "Санкт-Петербург";
  private static final String NON_EXISTING_CITY = "ГородКоторогоНет123124";

  @ParameterizedTest(name = "{0}")
  @MethodSource("browsers")
  void existingRegionCanBeSelectedAndUnknownRegionIsIgnored(BrowserType browserType) {
    runInBrowser(browserType, driver -> {
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
