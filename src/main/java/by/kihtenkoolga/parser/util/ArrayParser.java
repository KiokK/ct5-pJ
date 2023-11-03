package by.kihtenkoolga.parser.util;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import static by.kihtenkoolga.parser.util.Constants.ARR_END;
import static by.kihtenkoolga.parser.util.Constants.ARR_SEPARATOR;
import static by.kihtenkoolga.parser.util.Constants.ARR_START;
import static by.kihtenkoolga.parser.util.Constants.NULL;

class ArrayParser {


    protected static String collectionToJson(Collection<?> collection) throws IOException, NoSuchFieldException, IllegalAccessException {
        if (collection == null) return NULL;

        Iterator<?> iter = collection.iterator();

        StringBuilder jsonCollection = new StringBuilder(ARR_START);
        Object nextValue;
        if (iter.hasNext()) {
            nextValue = iter.next();
            jsonCollection.append(Parser.parseValue(nextValue));
        }

        while (iter.hasNext()) {
            jsonCollection.append(ARR_SEPARATOR);
            nextValue = iter.next();
            jsonCollection.append(Parser.parseValue(nextValue));
        }

        jsonCollection.append(ARR_END);
        return String.valueOf(jsonCollection);
    }

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
                ans.append(Parser.parseValue(Array.get(array, 0)));
            for (int i = 1; i < Array.getLength(array); i++)
                ans.append(ARR_SEPARATOR)
                   .append(Parser.parseValue(Array.get(array, i)));
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        ans.append(ARR_END);
        return String.valueOf(ans);
    }

}
