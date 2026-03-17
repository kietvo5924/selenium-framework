package framework.utils;

import com.github.javafaker.Faker;
import java.util.Locale;
import java.util.Map;

public class TestDataFactory {
    // Có thể truyền "vi" để sinh tên tiếng Việt, ở đây dùng mặc định tiếng Anh cho hợp với web
    private static final Faker faker = new Faker(new Locale("en"));

    public static String randomFirstName() {
        return faker.name().firstName();
    }

    public static String randomLastName() {
        return faker.name().lastName();
    }

    public static String randomPostalCode() {
        return faker.number().digits(5);
    }

    // Sinh một bộ dữ liệu checkout hoàn chỉnh gom vào Map [cite: 636-637]
    public static Map<String, String> randomCheckoutData() {
        return Map.of(
                "firstName", randomFirstName(),
                "lastName", randomLastName(),
                "postalCode", randomPostalCode()
        );
    }
}