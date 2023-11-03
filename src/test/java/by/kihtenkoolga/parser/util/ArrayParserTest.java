package by.kihtenkoolga.parser.util;

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

import static by.kihtenkoolga.util.GsonTestData.gson;
import static by.kihtenkoolga.util.OrderTestData.getOrderWithOneProduct;
import static by.kihtenkoolga.util.OrderTestData.getOrderWithTwoProducts;
import static by.kihtenkoolga.util.ProductTestData.getProductsArray;
import static by.kihtenkoolga.util.ProductTestData.getProductsList;


class ArrayParserTest {

    @Nested
    class CollectionToJson {
        @ParameterizedTest
        @MethodSource("argsForCollectionToJsonTest")
        void collectionToJson(Collection<?> argument, String expected) throws IOException, NoSuchFieldException, IllegalAccessException {
            Assertions.assertThat(ArrayParser.collectionToJson(argument))
                    .isEqualTo(expected);
        }

        static Stream<Arguments> argsForCollectionToJsonTest() {
            List<?> empty = new ArrayList<>();
            Set<Order> setOrders = Set.of(getOrderWithTwoProducts(), getOrderWithOneProduct());

            return Stream.of(
                    Arguments.of(getProductsList(), gson.toJson(getProductsList())),
                    Arguments.of(empty, gson.toJson(empty)),
                    Arguments.of(setOrders, gson.toJson(setOrders)),
                    Arguments.of(null, gson.toJson(null))
            );
        }

    }

    @Nested
    class ArrayToJson {
        @ParameterizedTest
        @MethodSource("argsForArrayToJsonTest")
        void arrayToJson(Object argument, String expected) {
            Assertions.assertThat(ArrayParser.arrayToJson(argument))
                    .isEqualTo(expected);
        }

        static Stream<Arguments> argsForArrayToJsonTest() {
            boolean[] bools = new boolean[]{true, false};
            int[] ints = new int[]{1, 2};
            Double[] doubles = new Double[]{1.21};
            long[] empty = new long[]{};
            Order[] orders = new Order[]{getOrderWithOneProduct(), getOrderWithTwoProducts()};

            return Stream.of(
                    Arguments.of(bools, gson.toJson(bools)),
                    Arguments.of(ints, gson.toJson(ints)),
                    Arguments.of(doubles, gson.toJson(doubles)),
                    Arguments.of(empty, gson.toJson(empty)),
                    Arguments.of(orders, gson.toJson(orders)),
                    Arguments.of(getProductsArray(), gson.toJson(getProductsArray())),
                    Arguments.of(null, gson.toJson(null))
            );
        }
    }
}
