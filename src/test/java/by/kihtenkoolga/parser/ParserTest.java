package by.kihtenkoolga.parser;

import by.kihtenkoolga.parser.util.Parser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static by.kihtenkoolga.util.CustomerTestData.getCustomerAnn;
import static by.kihtenkoolga.util.CustomerTestData.getCustomerNullOrEmptyFields;
import static by.kihtenkoolga.util.GsonTestData.gson;
import static by.kihtenkoolga.util.OrderTestData.getOrderWithTwoProducts;
import static by.kihtenkoolga.util.ProductTestData.getApple;
import static org.assertj.core.api.Assertions.assertThat;

class ParserTest {

    @ParameterizedTest
    @MethodSource("argsForParseTest")
    void serialize(Object argument, String expected) throws IOException, NoSuchFieldException, IllegalAccessException {
        assertThat(Parser.serialize(argument))
                .isEqualTo(expected);
    }

    static Stream<Arguments> argsForParseTest() {
        return Stream.of(
                Arguments.of(1, gson.toJson(1)),
                Arguments.of(true, gson.toJson(true)),
                Arguments.of(null, gson.toJson(null)),
                Arguments.of(1.234, gson.toJson(1.234)),
                Arguments.of(getApple(), gson.toJson(getApple())),
                Arguments.of(getCustomerAnn(), gson.toJson(getCustomerAnn())),
                Arguments.of(getCustomerNullOrEmptyFields(), gson.toJson(getCustomerNullOrEmptyFields())),
                Arguments.of(getOrderWithTwoProducts(), gson.toJson(getOrderWithTwoProducts()))
        );
    }

}
