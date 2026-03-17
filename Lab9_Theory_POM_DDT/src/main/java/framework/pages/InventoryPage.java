package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class InventoryPage extends BasePage {

    @FindBy(css = ".inventory_list")
    private WebElement inventoryList;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = ".btn_inventory")
    private List<WebElement> addToCartButtons;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    // Kiểm tra trang đã load thành công
    public boolean isLoaded() {
        return isElementVisible(By.cssSelector(".inventory_list"));
    }

    // Thêm sản phẩm đầu tiên vào giỏ
    public InventoryPage addFirstItemToCart() {
        waitAndClick(addToCartButtons.get(0));
        return this;
    }

    // Lấy số lượng item trong badge giỏ hàng
    public int getCartItemCount() {
        try {
            return Integer.parseInt(cartBadge.getText().trim());
        } catch (Exception e) {
            return 0; // Badge không hiển thị -> giỏ hàng rỗng
        }
    }

    // Chuyển sang trang giỏ hàng
    public CartPage goToCart() {
        waitAndClick(cartLink);
        return new CartPage(driver);
    }
}