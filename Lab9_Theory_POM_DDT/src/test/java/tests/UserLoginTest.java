package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.JsonReader;
import framework.utils.UserData;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class UserLoginTest extends BaseTest {

    @DataProvider(name = "jsonUsers")
    public Object[][] getUsersFromJson() throws IOException {
        List<UserData> users = JsonReader.readUsers("src/test/resources/testdata/users.json");
        return users.stream()
                .map(u -> new Object[]{u.username, u.password, u.expectSuccess, u.description})
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "jsonUsers", description = "Kiểm thử đăng nhập bằng dữ liệu từ JSON")
    public void testLoginFromJson(String username, String password, boolean expectSuccess, String description) {
        System.out.println("Đang chạy test case JSON: " + description);
        LoginPage loginPage = new LoginPage(getDriver());

        if (expectSuccess) {
            // Test case mong đợi thành công
            InventoryPage inventoryPage = loginPage.login(username, password);
            Assert.assertTrue(inventoryPage.isLoaded(), "Đăng nhập thành công nhưng không thấy trang Inventory");
        } else {
            // Test case mong đợi thất bại
            loginPage.loginExpectingFailure(username, password);
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Lỗi không hiển thị!");
        }
    }
}