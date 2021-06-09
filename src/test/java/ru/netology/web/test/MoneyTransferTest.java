package ru.netology.web.test;

import lombok.val;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.CardListPage;
import ru.netology.web.page.LoginPage;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.getAuthInfo;
import static ru.netology.web.data.DataHelper.getOtherAuthInfo;

class MoneyTransferTest {

    @BeforeEach
    void setUpAll() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTransferMoneyFromCard2ToCard1() {
        String sum = "5000";

        val loginPage = new LoginPage();
        val authInfo = getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val cardPage = verificationPage.validVerify(verificationCode);
        int balanceBeginCard1 = cardPage.getCard1Balance();
        int balanceBeginCard2 = cardPage.getCard2Balance();
        val cardPageReplenish = cardPage.card1Replenish();
        val infoFrom1To2 = DataHelper.getCardInfoFromCard2();
        cardPageReplenish.replenishCardToCard(infoFrom1To2, sum);
        assertEquals(balanceBeginCard1 + Integer.parseInt(sum), cardPage.getCard1Balance());
        assertEquals(balanceBeginCard2 - Integer.parseInt(sum), cardPage.getCard2Balance());
    }

    @Test
    void shouldTransferMoneyFromCard1ToCard2() {
        String sum = "2000";

        val loginPage = new LoginPage();
        val authInfo = getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val cardPage = verificationPage.validVerify(verificationCode);
        int balanceBeginCard1 = cardPage.getCard1Balance();
        int balanceBeginCard2 = cardPage.getCard2Balance();
        val cardPageReplenish = cardPage.card2Replenish();
        val infoFrom2To1 = DataHelper.getCardInfoFromCard1();
        cardPageReplenish.replenishCardToCard(infoFrom2To1, sum);
        val cardPageAfter = new CardListPage();
        assertEquals(balanceBeginCard2 + Integer.parseInt(sum), cardPageAfter.getCard2Balance());
        assertEquals(balanceBeginCard1 - Integer.parseInt(sum), cardPage.getCard1Balance());
    }

    @Test
    void shouldErrorMessageAppearIfBalanceBelowZero() {
        String sum = "20000";

        val loginPage = new LoginPage();
        val authInfo = getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val cardPage = verificationPage.validVerify(verificationCode);
        val cardPageReplenish = cardPage.card1Replenish();
        val infoFrom1To2 = DataHelper.getCardInfoFromCard2();
        cardPageReplenish.replenishCardToCard(infoFrom1To2, sum);
        assertEquals("Недостаточно средств на карте.", cardPageReplenish.errorNotificationAppear());
    }

    @Test
    void shouldErrorMessageAppearIfUseWrongCard() {
        String sum = "2000";

        val loginPage = new LoginPage();
        val authInfo = getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val cardPage = verificationPage.validVerify(verificationCode);
        val cardPageReplenish = cardPage.card2Replenish();
        val infoFrom3To1 = DataHelper.getCardInfoFromCard3();
        cardPageReplenish.replenishCardToCard(infoFrom3To1, sum);
        assertEquals("Проверьте правильность ввода номера карты.", cardPageReplenish.errorNotificationAppear());
    }

    @Test
    void shouldLoginWithInvalidUser() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        loginPage.invalidLogin(getOtherAuthInfo(getAuthInfo()));
        assertEquals("Ошибка! Неверно указан логин или пароль", loginPage.errorNotification());
        //$("[data-test-id='error-notification'] .notification__content").shouldBe(visible)
        //       .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldLoginWithInvalidCode() {

        val loginPage = new LoginPage();
        val authInfo = getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        verificationPage.invalidVerify(DataHelper.getVerificationCodeBag(getAuthInfo()));
        assertEquals("Ошибка! Неверно указан код! Попробуйте ещё раз.", verificationPage.errorNotification());
    }
}



