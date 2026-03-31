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
    public void setUp(@Optional("chrome") String xmlBrowser, @Optional("dev") String env) {
        System.setProperty("env", env);
        
        // Đọc biến browser từ GitHub Actions truyền vào (nếu không có thì mặc định dùng xmlBrowser)
        String browser = System.getProperty("browser") != null ? System.getProperty("browser") : xmlBrowser;
        boolean isCI = System.getenv("CI") != null; 
        
        WebDriver driver;

        // --- BẮT ĐẦU CẤU HÌNH ĐA TRÌNH DUYỆT ---
        if (browser.equalsIgnoreCase("firefox")) {
            io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
            org.openqa.selenium.firefox.FirefoxOptions firefoxOptions = new org.openqa.selenium.firefox.FirefoxOptions();
            if (isCI) {
                firefoxOptions.addArguments("-headless"); // Chạy ngầm cho Firefox [cite: 107, 165]
            }
            driver = new org.openqa.selenium.firefox.FirefoxDriver(firefoxOptions);
        } else {
            io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
            org.openqa.selenium.chrome.ChromeOptions chromeOptions = new org.openqa.selenium.chrome.ChromeOptions();
            if (isCI) {
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--window-size=1920,1080");
            } else {
                chromeOptions.addArguments("--start-maximized");
            }
            driver = new org.openqa.selenium.chrome.ChromeDriver(chromeOptions);
        }
        // --- KẾT THÚC CẤU HÌNH ĐA TRÌNH DUYỆT ---

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(framework.config.ConfigReader.getInstance().getBaseUrl());

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
