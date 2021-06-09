package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
  private SelenideElement codeField = $("[data-test-id=code] input");
  private SelenideElement verifyButton = $("[data-test-id=action-verify]");
  private SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");
  public VerificationPage() {
    codeField.shouldBe(visible);
  }

  public CardListPage validVerify(DataHelper.VerificationCode verificationCode) {
    codeField.setValue(verificationCode.getCode());
    verifyButton.click();
    return new CardListPage();
  }
  public void invalidVerify(DataHelper.VerificationCode verificationCode) {
    codeField.setValue(verificationCode.getCode());
    verifyButton.click();
  }
  public String errorNotification() {
    errorNotification.shouldBe(visible);
    String text = errorNotification.getText();
    return text;
  }
}
