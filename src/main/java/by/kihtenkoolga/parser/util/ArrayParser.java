package by.kihtenkoolga.parser.util;

import java.lang.reflect.Array;

import static by.kihtenkoolga.parser.util.Constants.ARR_END;
import static by.kihtenkoolga.parser.util.Constants.ARR_SEPARATOR;
import static by.kihtenkoolga.parser.util.Constants.ARR_START;
import static by.kihtenkoolga.parser.util.Constants.NULL;

class ArrayParser {

    /**
     * Переводит любой массив (например {@code int[], Object[], Boolean[]}) в json, используя метод
     * {@link  Parser#parseObject(Object object)} для сериализации элемента массива
     *
     * @param array массив, который будет сериализован в json
     * @return json представление массива
     */
    protected static String arrayToJson(Object array) {
        if (array == null)
            return NULL;
        StringBuilder arrayJsonString = new StringBuilder();
        arrayJsonString.append(ARR_START);
        int i = 1;
        if (Array.getLength(array) > 0)
            arrayJsonString.append(Parser.parseObject(Array.get(array, 0)));
        for (i = 1; i < Array.getLength(array); i++)
            arrayJsonString.append(ARR_SEPARATOR)
                    .append(Parser.parseObject(Array.get(array, i)));
        arrayJsonString.append(ARR_END);
        return String.valueOf(arrayJsonString);
    }


}
