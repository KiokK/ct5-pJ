package by.kihtenkoolga.parser;

import by.kihtenkoolga.model.Product;
import by.kihtenkoolga.parser.util.Parser;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import static by.kihtenkoolga.util.CustomerTestData.getCustomerAnn;
import static by.kihtenkoolga.util.CustomerTestData.getCustomerNullOrEmptyFields;
import static by.kihtenkoolga.util.GsonTestData.gson;
import static by.kihtenkoolga.util.OrderTestData.getOrderWithTwoProducts;
import static by.kihtenkoolga.util.PrimitiveTestData.getBoolean;
import static by.kihtenkoolga.util.PrimitiveTestData.getDouble;
import static by.kihtenkoolga.util.PrimitiveTestData.getInt;
import static by.kihtenkoolga.util.PrimitiveTestData.getNull;
import static by.kihtenkoolga.util.PrimitiveTestData.getString;
import static by.kihtenkoolga.util.ProductTestData.getApple;
import static by.kihtenkoolga.util.JsonTestData.getJsonInt;
import static by.kihtenkoolga.util.JsonTestData.getJsonProduct;
import static by.kihtenkoolga.util.JsonTestData.getJsonString;
import static org.assertj.core.api.Assertions.assertThat;

class ParserTest {

    @Nested
    class Deserialize {

        @ParameterizedTest
        @MethodSource("argsForDeserializeTest")
        void deserialize(String argumentJson, Object expected) throws IOException, NoSuchFieldException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
            assertThat(Parser.deserialize(argumentJson.toCharArray(), expected.getClass()))
                    .isEqualTo(expected);
        }

        static Stream<Arguments> argsForDeserializeTest() throws IOException {
            return Stream.of(
                    Arguments.of(getJsonString(), gson.fromJson(getJsonString(), String.class)),
                    Arguments.of(getJsonInt(), gson.fromJson(getJsonInt(), Integer.class)),
                    Arguments.of(getJsonProduct(), gson.fromJson(getJsonProduct(), Product.class))
            );
        }
    }

    @Nested
    class Serialize {

        @ParameterizedTest
        @MethodSource("argsForParseTest")
        void serialize(Object argument, String expected) throws IOException, NoSuchFieldException, IllegalAccessException {
            assertThat(Parser.serialize(argument))
                    .isEqualTo(expected);
        }

        static Stream<Arguments> argsForParseTest() {
            return Stream.of(
                    Arguments.of(getInt(), gson.toJson(getInt())),
                    Arguments.of(getBoolean(), gson.toJson(getBoolean())),
                    Arguments.of(getNull(), gson.toJson(getNull())),
                    Arguments.of(getDouble(), gson.toJson(getDouble())),
                    Arguments.of(getString(), gson.toJson(getString())),
                    Arguments.of(getApple(), gson.toJson(getApple())),
                    Arguments.of(getCustomerAnn(), gson.toJson(getCustomerAnn())),
                    Arguments.of(getCustomerNullOrEmptyFields(), gson.toJson(getCustomerNullOrEmptyFields())),
                    Arguments.of(getOrderWithTwoProducts(), gson.toJson(getOrderWithTwoProducts()))
            );
        }
    }

}
