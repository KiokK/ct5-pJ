package by.kihtenkoolga.util;

import by.kihtenkoolga.model.Product;

import java.util.List;
import java.util.UUID;

public class ProductTestData {

    private static final UUID APPLE_UUID = UUID.fromString("521fb222-bdf2-4c14-8f73-7c496e3b1bb7");
    private static final String APPLE_NAME = "Apple";
    private static final Double APPLE_PRISE = 10.01;

    private static final UUID MELON_UUID = UUID.fromString("ae82d1d1-f9cf-4d28-9151-e67c508f043a");
    private static final String MELON_NAME = "Melon";
    private static final Double MELON_PRISE = 10.0;

    public static Product getApple() {
        return Product.builder()
                .id(APPLE_UUID)
                .name(APPLE_NAME)
                .price(APPLE_PRISE)
                .build();
    }

    public static Product getMelon() {
        return Product.builder()
                .id(MELON_UUID)
                .name(MELON_NAME)
                .price(MELON_PRISE)
                .build();
    }

    public static Product[] getProductsArray() {
        return new Product[]{getApple(), getMelon()};
    }

    public static List<Product> getProductsList() {
        return List.of(getApple(), getMelon());
    }

}
