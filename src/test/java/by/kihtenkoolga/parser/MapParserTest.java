package by.kihtenkoolga.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static by.kihtenkoolga.util.GsonTestData.gson;
import static org.assertj.core.api.Assertions.assertThat;

class MapParserTest {

    @ParameterizedTest
    @MethodSource("argsForMapTests")
    void serializeMap(Map<?, ?> argument, String expected) {
        assertThat(MapParser.serializeMap(argument))
                .isEqualTo(expected);
    }

    static Stream<Arguments> argsForMapTests() {
        Map<Integer, String> map = Map.of(1, "", 2, "3sd");

        return Stream.of(
                Arguments.of(null, gson.toJson(null)),
                Arguments.of(map, gson.toJson(map))
        );
    }

}
