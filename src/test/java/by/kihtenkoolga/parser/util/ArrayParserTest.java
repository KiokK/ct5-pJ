package by.kihtenkoolga.parser.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static by.kihtenkoolga.util.ProductTestData.getProductsArray;


class ArrayParserTest {

    @ParameterizedTest
    @MethodSource("argsForArrayToJsonTest")
    void arrayToJson(Object argument, String expected) {
        Assertions.assertThat(ArrayParser.arrayToJson(argument))
                .isEqualTo(expected);
    }

    static Stream<Arguments> argsForArrayToJsonTest() {
        Gson gson = new GsonBuilder().serializeNulls().create();

        boolean[] bools = new boolean[]{true, false};
        int[] ints = new int[]{1, 2};
        Double[] doubles = new Double[]{1.21};

        return Stream.of(
                Arguments.of(bools, gson.toJson(bools)),
                Arguments.of(ints, gson.toJson(ints)),
                Arguments.of(doubles, gson.toJson(doubles)),
                Arguments.of(getProductsArray(), gson.toJson(getProductsArray())),
                Arguments.of(null, gson.toJson(null))
        );
    }
}
