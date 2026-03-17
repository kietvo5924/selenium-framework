package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginDDTTest extends BaseTest {

    String excelPath = "src/test/resources/testdata/login_data.xlsx";

    // 1. Data cho nhóm Smoke (Đăng nhập thành công)
    @DataProvider(name = "smokeData")
    public Object[][] getSmokeData() {
        return ExcelReader.getData(excelPath, "SmokeCases");
    }

    // 2. Data cho nhóm Negative (Đăng nhập thất bại)
    @DataProvider(name = "negativeData")
    public Object[][] getNegativeData() {
        return ExcelReader.getData(excelPath, "NegativeCases");
    }

    // 3. Data cho nhóm Boundary (Kiểm thử biên/ký tự đặc biệt)
    @DataProvider(name = "boundaryData")
    public Object[][] getBoundaryData() {
        return ExcelReader.getData(excelPath, "BoundaryCases");
    }

    // Test Case 1: Chạy Happy Path (Kỳ vọng check URL)
    @Test(dataProvider = "smokeData", groups = {"smoke", "regression"}, description = "Test Happy Path")
    public void testSmokeLogin(String username, String password, String expectedUrl, String description) {
        System.out.println("Đang chạy: " + description);
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login(username, password);

        Assert.assertTrue(inventoryPage.isLoaded(), "Trang Inventory chưa load thành công!");
        Assert.assertEquals(getDriver().getCurrentUrl(), expectedUrl, "URL không khớp sau khi đăng nhập!");
    }

    // Test Case 2: Chạy Negative (Kỳ vọng check Error Message)
    @Test(dataProvider = "negativeData", groups = {"regression"}, description = "Test Negative Cases")
    public void testNegativeLogin(String username, String password, String expectedError, String description) {
        System.out.println("Đang chạy: " + description);
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginExpectingFailure(username, password);

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Không hiển thị khung báo lỗi!");
        Assert.assertTrue(loginPage.getErrorMessage().contains(expectedError),
                "Lỗi thực tế: " + loginPage.getErrorMessage() + " | Mong đợi: " + expectedError);
    }

    // Test Case 3: Chạy Boundary (Kỳ vọng check Error Message)
    @Test(dataProvider = "boundaryData", groups = {"regression"}, description = "Test Boundary Cases")
    public void testBoundaryLogin(String username, String password, String expectedError, String description) {
        System.out.println("Đang chạy: " + description);
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginExpectingFailure(username, password);

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Không hiển thị khung báo lỗi!");
        Assert.assertTrue(loginPage.getErrorMessage().contains(expectedError),
                "Lỗi thực tế: " + loginPage.getErrorMessage() + " | Mong đợi: " + expectedError);
    }
}