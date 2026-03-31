package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

   @Test(description = "Kiểm tra đăng nhập thành công với tài khoản chuẩn")
    public void testLoginSuccess() {
        LoginPage loginPage = new LoginPage(getDriver());

        // --- ĐỌC TỪ KÉT SẮT ---
        String user = System.getenv("APP_USERNAME") != null ? System.getenv("APP_USERNAME") : "standard_user";
        String pass = System.getenv("APP_PASSWORD") != null ? System.getenv("APP_PASSWORD") : "secret_sauce";
        // ----------------------

        // Gọi hàm login với biến vừa đọc
        InventoryPage inventoryPage = loginPage.login(user, pass);

        Assert.assertTrue(inventoryPage.isLoaded(), "Trang inventory chưa load, đăng nhập thất bại!");
    }

    @Test(description = "Kiểm tra đăng nhập thất bại khi sai mật khẩu")
    public void testLoginFailure() {
        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.loginExpectingFailure("standard_user", "wrong_password");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Lỗi không hiển thị!");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
                "Câu thông báo lỗi không khớp!");
    }
}
