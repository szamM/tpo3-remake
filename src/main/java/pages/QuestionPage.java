package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class QuestionPage extends Page {

  public QuestionPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = "//div[2]/div/a/div")
  private WebElement likeButton;

  @FindBy(xpath = "//div[@id='inputBody']/div[2]/div")
  private WebElement answerTextArea;

  @FindBy(xpath = "//form/div[2]/a/div")
  private WebElement answerButton;

  @FindBy(xpath = "//div[2]/div[2]/a/div")
  private WebElement addCommentButton;

  @FindBy(xpath = "//div[@id='inputBody']/div[2]/div")
  private WebElement commentTextArea;

  @FindBy(xpath = "//div[2]/div/div[2]/div[2]/a/div")
  private WebElement commentButton;


  public void answerOnQuestion() {
    likeButton.click();
    answerTextArea.click();
    answerTextArea.sendKeys("Лучше всего прочитайте специальную литературу для этого");
    answerButton.click();
  }

  public void createComment() {
    addCommentButton.click();
    commentTextArea.click();
    commentTextArea.sendKeys("Да, это лучший вараинт");
    commentButton.click();
  }
}
