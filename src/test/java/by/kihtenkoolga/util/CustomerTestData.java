package by.kihtenkoolga.util;

import by.kihtenkoolga.model.Customer;
import by.kihtenkoolga.model.Order;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerTestData {

    private static final UUID CUSTOMER_ANN_ID = UUID.fromString("f9f0f520-8797-4b1e-9677-a27a22ed8e4f");
    private static final String CUSTOMER_ANN_FIRST_NAME = "Ann";
    private static final String CUSTOMER_ANN_LAST_NAME = "Ananas";
    private static final LocalDate CUSTOMER_ANN_DATE_BIRTH = LocalDate.of(1998, Month.OCTOBER, 10);

    private static final UUID CUSTOMER_NULL_ID = null;
    private static final String CUSTOMER_NULL_FIRST_NAME = null;
    private static final String CUSTOMER_EMPTY_LAST_NAME = "";
    private static final LocalDate CUSTOMER_NULL_DATE_BIRTH = null;
    private static final List<Order> CUSTOMER_EMPTY_ORDERS = new ArrayList<>();

    public static Customer getCustomerAnn() {
        return Customer.builder()
                .id(CUSTOMER_ANN_ID)
                .firstName(CUSTOMER_ANN_FIRST_NAME)
                .lastName(CUSTOMER_ANN_LAST_NAME)
                .dateBirth(CUSTOMER_ANN_DATE_BIRTH)
                .orders(List.of(OrderTestData.getOrderWithOneProduct(), OrderTestData.getOrderWithTwoProducts()))
                .build();
    }

    public static Customer getCustomerNullOrEmptyFields() {
        return Customer.builder()
                .id(CUSTOMER_NULL_ID)
                .firstName(CUSTOMER_NULL_FIRST_NAME)
                .lastName(CUSTOMER_EMPTY_LAST_NAME)
                .dateBirth(CUSTOMER_NULL_DATE_BIRTH)
                .orders(CUSTOMER_EMPTY_ORDERS)
                .build();
    }

}
