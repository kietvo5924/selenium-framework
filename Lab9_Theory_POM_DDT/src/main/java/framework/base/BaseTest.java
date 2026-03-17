package framework.base;

import framework.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.time.Duration;

public abstract class BaseTest {
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    protected WebDriver getDriver() { return tlDriver.get(); }

    @Parameters({"browser", "env"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser, @Optional("dev") String env) {
        // Đặt env làm System property để ConfigReader đọc đúng file [cite: 295, 296]
        System.setProperty("env", env);

        // Trong tài liệu ghi dùng DriverFactory, nhưng tôi tạm viết trực tiếp ChromeDriver
        // ở đây để code có thể chạy ngay lập tức mà không báo lỗi [cite: 298]
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(ConfigReader.getInstance().getBaseUrl());

        tlDriver.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // Chụp ảnh TRƯỚC khi quit - bắt buộc trong dự án thực để debug [cite: 306]
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = framework.utils.ScreenshotUtil.capture(getDriver(), result.getName());
        }

        if (getDriver() != null) {
            getDriver().quit();
            tlDriver.remove(); // Quan trọng: tránh memory leak khi chạy song song [cite: 316-317]
        }
    }
}
// Nguồn tham khảo [cite: 286-317]