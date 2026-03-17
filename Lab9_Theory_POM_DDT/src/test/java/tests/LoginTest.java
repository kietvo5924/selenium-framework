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

        // Gọi hàm login và tự động chuyển trang nhờ Fluent Interface
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");

        // Chỉ dùng Assert trong Test class, không để Assert trong Page Object
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