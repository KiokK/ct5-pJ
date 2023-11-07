package by.kihtenkoolga.parser.util;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static by.kihtenkoolga.parser.util.Constants.ARR_END;
import static by.kihtenkoolga.parser.util.Constants.ARR_SEPARATOR;
import static by.kihtenkoolga.parser.util.Constants.ARR_START;
import static by.kihtenkoolga.parser.util.Constants.NULL;
import static by.kihtenkoolga.parser.util.Parser.deserialize;

class ArrayParser {

    /**
     * Парсит любой массив в json строку, например:
     * int[], Object[], Boolean[]
     * @param array массив, который будет переведен в json
     * @return json представление массива
     */
    protected static String arrayToJson(Object array)  {
        if (array == null)
            return NULL;
        StringBuilder ans = new StringBuilder(ARR_START);
        try {
            if (Array.getLength(array) > 0)
                ans.append(Parser.parseObject(Array.get(array, 0)));
            for (int i = 1; i < Array.getLength(array); i++)
                ans.append(ARR_SEPARATOR)
                   .append(Parser.parseObject(Array.get(array, i)));
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        ans.append(ARR_END);
        return String.valueOf(ans);
    }

    protected static <T> ArrayList<T> deserializeArray(char[] json, T clazz) throws IOException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Type t = ((ParameterizedType) clazz).getActualTypeArguments()[0];
        ArrayList<T> deserializeArr = new ArrayList<>();
        Parser.i++;
        while (json[Parser.i] != ']') {
            T arrElement = deserialize(json, (Class<T>) t);
            deserializeArr.add(arrElement);
        }
        return deserializeArr;
    }

}
