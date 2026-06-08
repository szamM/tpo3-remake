package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Locale;

public class EmployersPage extends Page {

  private static final String CATALOG_TITLE = "//*[@data-qa='employers-company__title']";
  private static final String COMPANY_SEARCH_INPUT = "//*[@data-qa='search-input' and @name='query']";
  private static final String COMPANY_SEARCH_BUTTON = "//*[@data-qa='search-button']";
  private static final String EMPLOYER_NAMES = "//*[@data-qa='employer-name']";
  private static final String PROFILE_TITLE = "//h1 | //*[@data-qa='title']";

  public EmployersPage(WebDriver driver) {
    super(driver);
  }

  public EmployersPage openCatalog() {
    openPath("/employers_company");
    return waitUntilCatalogOpened();
  }

  public EmployersPage waitUntilCatalogOpened() {
    visible(CATALOG_TITLE);
    return this;
  }

  public EmployersPage openList() {
    openPath("/employers_list?areaId=113");
    visible(COMPANY_SEARCH_INPUT);
    return this;
  }

  public EmployersPage searchCompany(String query) {
    type(COMPANY_SEARCH_INPUT, query);
    click(COMPANY_SEARCH_BUTTON);
    if (!waitShortForUrlContains("query=")) {
      openPath("/employers_list?areaId=113&query=" + urlEncode(query));
    }
    visible(EMPLOYER_NAMES);
    return this;
  }

  public int resultCount() {
    return elements(EMPLOYER_NAMES).size();
  }

  public boolean hasEmployerContaining(String expectedText) {
    String expected = expectedText.toLowerCase(Locale.ROOT);
    return elements(EMPLOYER_NAMES).stream()
        .map(WebElement::getText)
        .map(text -> text.toLowerCase(Locale.ROOT))
        .anyMatch(text -> text.contains(expected));
  }

  public EmployersPage openFirstEmployerProfile() {
    WebElement firstEmployer = visible(EMPLOYER_NAMES);
    String href = firstEmployer.getAttribute("href");
    click(EMPLOYER_NAMES);
    if (!waitShortForUrlContains("/employer/")) {
      driver.get(toAbsoluteUrl(href));
    }
    visible(PROFILE_TITLE);
    return this;
  }
}
