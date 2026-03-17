package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginDDTTest extends BaseTest {

    // Khai báo nguồn dữ liệu đọc từ Excel [cite: 410-412]
    @DataProvider(name = "excelLoginData")
    public Object[][] getLoginDataFromExcel() {
        String path = "src/test/resources/testdata/login_data.xlsx";
        // Nhớ tên sheet "LoginCases" phải khớp đúng với tên sheet trong file Excel của bạn
        return ExcelReader.getData(path, "LoginCases");
    }

    // Gắn dataProvider vào test [cite: 413]
    // Tham số truyền vào hàm phải TƯƠNG ỨNG với số lượng cột trong Excel
    @Test(dataProvider = "excelLoginData", description = "Kiểm thử đăng nhập bằng dữ liệu từ Excel")
    public void testLoginFromExcel(String username, String password, String expected, String description) {
        System.out.println("Đang chạy test case: " + description);

        LoginPage loginPage = new LoginPage(getDriver());

        if (expected.equals("SUCCESS")) {
            // Trường hợp đăng nhập thành công
            InventoryPage inventoryPage = loginPage.login(username, password);
            Assert.assertTrue(inventoryPage.isLoaded(), "Đăng nhập thành công nhưng không thấy trang Inventory");
        } else {
            // Trường hợp đăng nhập thất bại (sai pass, bị khóa...)
            loginPage.loginExpectingFailure(username, password);
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Lỗi không hiển thị!");
            Assert.assertTrue(loginPage.getErrorMessage().contains(expected),
                    "Câu báo lỗi không khớp! Mong đợi: " + expected + ", Thực tế: " + loginPage.getErrorMessage());
        }
    }
}