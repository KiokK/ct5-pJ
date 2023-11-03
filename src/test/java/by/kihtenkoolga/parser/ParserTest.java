package by.kihtenkoolga.parser;

import by.kihtenkoolga.parser.util.Parser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

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
        Gson gson = new GsonBuilder().serializeNulls().create();

        return Stream.of(
                Arguments.of(getApple(), gson.toJson(getApple()))
        );
    }

}
