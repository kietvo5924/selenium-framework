package tests;

import framework.base.BaseTest;
import framework.pages.CartPage;
import framework.pages.CheckoutPage;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.TestDataFactory;
import org.testng.annotations.Test;
import java.util.Map;

public class CheckoutTest extends BaseTest {

    @Test(description = "Kiểm tra điền form checkout bằng dữ liệu ngẫu nhiên từ Java Faker")
    public void testCheckoutWithFaker() {
        // 1. Sinh dữ liệu ngẫu nhiên từ Factory [cite: 621]
        Map<String, String> checkoutInfo = TestDataFactory.randomCheckoutData();
        String fName = checkoutInfo.get("firstName");
        String lName = checkoutInfo.get("lastName");
        String zip = checkoutInfo.get("postalCode");

        // In ra Console để theo dõi [cite: 622]
        System.out.println("=== Dữ liệu Faker sinh ra ===");
        System.out.println("First Name: " + fName);
        System.out.println("Last Name:  " + lName);
        System.out.println("Zip Code:   " + zip);
        System.out.println("=============================");

        // 2. Kịch bản thao tác trên web
        String user = System.getenv("APP_USERNAME") != null ? System.getenv("APP_USERNAME") : "standard_user";
        String pass = System.getenv("APP_PASSWORD") != null ? System.getenv("APP_PASSWORD") : "secret_sauce";

        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login(user, pass);

        inventoryPage.addFirstItemToCart();
        CartPage cartPage = inventoryPage.goToCart();
        CheckoutPage checkoutPage = cartPage.goToCheckout();

        // 3. Điền thông tin checkout bằng dữ liệu ảo
        checkoutPage.enterCheckoutInfo(fName, lName, zip);

        // Bạn có thể thấy code test đọc cực kỳ giống kịch bản tự nhiên!
    }
}
