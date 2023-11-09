package by.kihtenkoolga.util;

import by.kihtenkoolga.model.Order;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static by.kihtenkoolga.util.ProductTestData.getApple;
import static by.kihtenkoolga.util.ProductTestData.getProductsList;

public class OrderTestData {

    private static final UUID ORDER_UUID = UUID.fromString("915bc5f9-ec01-4303-bfa0-c9daec0500e3");

    private static final OffsetDateTime ORDER_DATE_TIME = OffsetDateTime.of(
            LocalDateTime.of(2023, Month.NOVEMBER, 11, 10, 2, 1),
            ZoneOffset.UTC
    );

    public static Order getOrderWithTwoProducts() {
        return Order.builder()
                .id(ORDER_UUID)
                .products(getProductsList())
                .createDate(ORDER_DATE_TIME)
                .build();
    }

    public static Order getOrderWithOneProduct() {
        return Order.builder()
                .id(ORDER_UUID)
                .products(List.of(getApple()))
                .createDate(ORDER_DATE_TIME)
                .build();
    }

}
