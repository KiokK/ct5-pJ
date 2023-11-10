package by.kihtenkoolga.parser;

import java.lang.reflect.Array;

public class ArrayParser {

    /**
     * Переводит любой массив (например {@code int[], Object[], Boolean[]}) в json, используя метод
     * {@link  Parser#parseObject(Object object)} для сериализации элемента массива
     *
     * @param array массив, который будет сериализован в json
     * @return json представление массива
     */
    protected static String arrayToJson(Object array) {
        if (array == null) {
            return Constants.NULL;
        }
        StringBuilder arrayJsonString = new StringBuilder();
        arrayJsonString.append(Constants.ARR_START);
        if (Array.getLength(array) > 0) {
            arrayJsonString.append(Parser.parseObject(Array.get(array, 0)));
        }
        for (int i = 1; i < Array.getLength(array); i++) {
            arrayJsonString.append(Constants.ARR_SEPARATOR)
                    .append(Parser.parseObject(Array.get(array, i)));
        }
        arrayJsonString.append(Constants.ARR_END);

        return String.valueOf(arrayJsonString);
    }

}
