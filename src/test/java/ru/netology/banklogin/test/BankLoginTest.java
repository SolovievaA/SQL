package ru.netology.banklogin.test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.junit.jupiter.api.*;
import ru.netology.banklogin.page.LoginPage;
import ru.netology.banklogin.data.DataHelper;
import ru.netology.banklogin.data.SQLHelper;

import static ru.netology.banklogin.data.SQLHelper.cleanDatabase;
import static ru.netology.banklogin.data.SQLHelper.cleanAutoCodes;
import static com.codeborne.selenide.Selenide.open;

public class BankLoginTest {
    LoginPage LoginPage;

    @AfterEach
    void tearDown() {
        cleanAutoCodes();
    }

    @AfterAll
    static void tearDownAll()  {
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        LoginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Should successfully login to dashboard with exist login and password from sut test data")
    void shouldSuccessfulLogin()  {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = LoginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisiblity();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Should get error notification if user is not exist in base")
    void shouldGetErrorNotificationIfLoginWithRandomUserWithoutAddingToBase() {
        var authInfo = DataHelper.generateRandomUser();
        LoginPage.validLogin(authInfo);
        LoginPage.verifyErrorNotification();
    }

    @Test
    @DisplayName("Should get error notification if login with exist in base and active user and random verification code")
    void shouldGetErrorNotificationIfLoginWithExistUserAndRandomVerificationCode() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = LoginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisiblity();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification();

    }
}