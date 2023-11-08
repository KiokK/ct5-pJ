package by.kihtenkoolga.parser;

import by.kihtenkoolga.exception.JsonDeserializeException;
import by.kihtenkoolga.exception.JsonIncorrectDataParseException;
import by.kihtenkoolga.model.Customer;
import by.kihtenkoolga.model.Order;
import by.kihtenkoolga.model.Product;
import by.kihtenkoolga.parser.util.Parser;
import by.kihtenkoolga.util.MultiClassTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import static by.kihtenkoolga.util.CustomerTestData.getCustomerAnn;
import static by.kihtenkoolga.util.CustomerTestData.getCustomerNullOrEmptyFields;
import static by.kihtenkoolga.util.GsonTestData.gson;
import static by.kihtenkoolga.util.JsonTestData.getJsonCustomer;
import static by.kihtenkoolga.util.JsonTestData.getJsonOrder;
import static by.kihtenkoolga.util.JsonTestData.getMultiClass;
import static by.kihtenkoolga.util.JsonTestData.getProductWithExceptionFalse;
import static by.kihtenkoolga.util.JsonTestData.getProductWithExceptionNull;
import static by.kihtenkoolga.util.JsonTestData.getProductWithExceptionPrice;
import static by.kihtenkoolga.util.JsonTestData.getProductWithExceptionTrue;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                    Arguments.of(getMultiClass(), gson.fromJson(getMultiClass(), MultiClassTestData.class)),
                    Arguments.of(getJsonProduct(), gson.fromJson(getJsonProduct(), Product.class)),
                    Arguments.of(getJsonOrder(), gson.fromJson(getJsonOrder(), Order.class)),
                    Arguments.of(getJsonCustomer(), gson.fromJson(getJsonCustomer(), Customer.class))
            );
        }

        @ParameterizedTest
        @MethodSource("argsForDeserializeIncorrectDataParseExceptionTests")
        void deserializeShouldThrowJsonIncorrectDataParseException(String argJson, Class<?> clazz) throws IOException, NoSuchFieldException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
            assertThrows(JsonIncorrectDataParseException.class, () ->
                    Parser.deserialize(argJson.toCharArray(), clazz));
        }


        static Stream<Arguments> argsForDeserializeIncorrectDataParseExceptionTests() throws IOException {
            return Stream.of(
                    Arguments.of(getProductWithExceptionNull(), Product.class),
                    Arguments.of(getProductWithExceptionTrue(), MultiClassTestData.class),
                    Arguments.of(getProductWithExceptionFalse(), MultiClassTestData.class),
                    Arguments.of(getProductWithExceptionPrice(), Product.class)
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
