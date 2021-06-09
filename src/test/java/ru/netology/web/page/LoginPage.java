package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;

import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private SelenideElement loginField = $("[data-test-id=login] input");
  private SelenideElement passwordField = $("[data-test-id=password] input");
  private SelenideElement loginButton = $("[data-test-id=action-login]");
  private SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");

  public VerificationPage validLogin(DataHelper.AuthInfo info) {
    loginField.setValue(info.getLogin());
    passwordField.setValue(info.getPassword());
    loginButton.click();
    return new VerificationPage();
  }
  public void invalidLogin(DataHelper.AuthInfo info) {
    loginField.setValue(info.getLogin());
    passwordField.setValue(info.getPassword());
    loginButton.click();

  }


  public String errorNotification() {
    errorNotification.shouldBe(visible);
    String text = errorNotification.getText();
    return text;
  }
}
