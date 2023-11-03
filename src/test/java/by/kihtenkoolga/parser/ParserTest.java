package by.kihtenkoolga.parser;

import by.kihtenkoolga.util.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ParserTest {

    @ParameterizedTest
    @MethodSource("argsForParseTest")
    void serialize(Object argument, String expected) throws IOException, NoSuchFieldException, IllegalAccessException {
        assertThat(Parser.serialize(argument))
                .isEqualTo(expected);
    }

    static Stream<Arguments> argsForParseTest() {
        Gson gson = new GsonBuilder().serializeNulls().create();

        Product product = new Product(UUID.randomUUID(), "Apple", 10.01);

        return Stream.of(
                Arguments.of(product, gson.toJson(product))
        );
    }

}
