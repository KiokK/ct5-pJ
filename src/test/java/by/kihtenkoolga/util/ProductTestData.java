package by.kihtenkoolga.util;

import java.util.UUID;

public class ProductTestData {

    private static final UUID APPLE_UUID = UUID.randomUUID();
    private static final String APPLE_NAME = "Apple";
    private static final Double APPLE_PRISE = 10.01;

    private static final UUID MELON_UUID = UUID.randomUUID();
    private static final String MELON_NAME = "Melon";
    private static final Double MELON_PRISE = 10.0;

    public static Product getApple() {
        return new Product(APPLE_UUID, APPLE_NAME, APPLE_PRISE);
    }

    public static Product getMelon() {
        return new Product(MELON_UUID, MELON_NAME, MELON_PRISE);
    }

    public static Product[] getProductsArray() {
        return new Product[]{getApple(), getMelon()};
    }
}
