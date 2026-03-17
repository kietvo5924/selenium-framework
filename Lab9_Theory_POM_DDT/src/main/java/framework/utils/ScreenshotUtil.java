package framework.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {
    public static String capture(WebDriver driver, String testName) {
        // Tạo tên file có chứa timestamp để không bị ghi đè [cite: 570-571]
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String destPath = "target/screenshots/" + testName + "_" + timestamp + ".png";

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File(destPath));
            System.out.println("[Screenshot] Đã chụp ảnh màn hình lỗi tại: " + destPath);
        } catch (IOException e) {
            System.out.println("Không thể lưu ảnh màn hình: " + e.getMessage());
        }
        return destPath;
    }
}