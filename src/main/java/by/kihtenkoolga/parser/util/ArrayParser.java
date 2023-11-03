package by.kihtenkoolga.parser.util;

import java.io.IOException;
import java.lang.reflect.Array;

class ArrayParser {

    /**
     * Парсит любой массив в json строку, например:
     * int[], Object[], Boolean[]
     * @param array массив, который будет переведен в json
     * @return json представление массива
     */
    protected static String arrayToJson(Object array)  {
        if (array == null)
            return "null";
        StringBuilder ans = new StringBuilder("[");
        try {
            if (Array.getLength(array) > 0)
                ans.append(Parser.parseValue(Array.get(array, 0)));
            for (int i = 1; i < Array.getLength(array); i++)
                ans.append(",")
                   .append(Parser.parseValue(Array.get(array, i)));
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        ans.append("]");
        return String.valueOf(ans);
    }

}
