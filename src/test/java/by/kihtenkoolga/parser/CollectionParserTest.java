package by.kihtenkoolga.parser;

import by.kihtenkoolga.model.Customer;
import by.kihtenkoolga.model.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static by.kihtenkoolga.util.CustomerTestData.getCustomerAnn;
import static by.kihtenkoolga.util.CustomerTestData.getCustomerNullOrEmptyFields;
import static by.kihtenkoolga.util.GsonTestData.gson;
import static by.kihtenkoolga.util.OrderTestData.getOrderWithOneProduct;
import static by.kihtenkoolga.util.OrderTestData.getOrderWithTwoProducts;
import static by.kihtenkoolga.util.ProductTestData.getProductsList;

class CollectionParserTest {

    @Nested
    class CollectionToJson {

        @ParameterizedTest
        @MethodSource("argsForCollectionToJsonTest")
        void collectionToJson(Collection<?> argument, String expected) throws IOException, NoSuchFieldException, IllegalAccessException {
            Assertions.assertThat(CollectionParser.collectionToJson(argument))
                    .isEqualTo(expected);
        }

        static Stream<Arguments> argsForCollectionToJsonTest() {
            List<?> emptyList = new ArrayList<>();
            Set<Order> setOrders = Set.of(getOrderWithTwoProducts(), getOrderWithOneProduct());
            List<Customer> customersList = List.of(getCustomerNullOrEmptyFields(), getCustomerAnn());

            return Stream.of(
                    Arguments.of(getProductsList(), gson.toJson(getProductsList())),
                    Arguments.of(emptyList, gson.toJson(emptyList)),
                    Arguments.of(setOrders, gson.toJson(setOrders)),
                    Arguments.of(customersList, gson.toJson(customersList)),
                    Arguments.of(null, gson.toJson(null))
            );
        }

    }

}
