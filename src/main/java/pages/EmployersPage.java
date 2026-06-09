package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Locale;

public class EmployersPage extends Page {

  @FindBy(xpath = "//*[@data-qa='employers-company__title']")
  private WebElement catalogTitle;

  @FindBy(xpath = "//*[@data-qa='search-input' and @name='query']")
  private WebElement companySearchInput;

  @FindBy(xpath = "//*[@data-qa='search-button']")
  private WebElement companySearchButton;

  @FindBy(xpath = "//*[@data-qa='employer-name']")
  private List<WebElement> employerNames;

  @FindBy(xpath = "//h1 | //*[@data-qa='title']")
  private WebElement profileTitle;

  public EmployersPage(WebDriver driver) {
    super(driver);
  }

  public EmployersPage openCatalog() {
    openPath("/employers_company");
    return waitUntilCatalogOpened();
  }

  public EmployersPage waitUntilCatalogOpened() {
    visible(catalogTitle);
    return this;
  }

  public EmployersPage openList() {
    openPath("/employers_list?areaId=113");
    visible(companySearchInput);
    return this;
  }

  public EmployersPage searchCompany(String query) {
    type(companySearchInput, query);
    click(companySearchButton);
    if (!waitShortForUrlContains("query=")) {
      openPath("/employers_list?areaId=113&query=" + urlEncode(query));
    }
    visible(employerNames);
    return this;
  }

  public int resultCount() {
    return elements(employerNames).size();
  }

  public boolean hasEmployerContaining(String expectedText) {
    String expected = expectedText.toLowerCase(Locale.ROOT);
    return elements(employerNames).stream()
        .map(WebElement::getText)
        .map(text -> text.toLowerCase(Locale.ROOT))
        .anyMatch(text -> text.contains(expected));
  }

  public EmployersPage openFirstEmployerProfile() {
    WebElement firstEmployer = visible(employerNames);
    String href = firstEmployer.getAttribute("href");
    click(firstEmployer);
    if (!waitShortForUrlContains("/employer/")) {
      driver.get(toAbsoluteUrl(href));
    }
    visible(profileTitle);
    return this;
  }
}
