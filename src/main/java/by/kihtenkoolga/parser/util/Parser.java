package by.kihtenkoolga.parser.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

import static by.kihtenkoolga.parser.util.Constants.NULL;
import static by.kihtenkoolga.parser.util.Constants.formatter;


public class Parser {

    /**
     * Обрабатывает null
     * @param value
     * @return
     * @throws IOException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    protected static String parseValue(Object value) throws IOException, NoSuchFieldException, IllegalAccessException {
        if (value == null)
            return NULL;

        if (value.getClass().isPrimitive())
            return value.toString();

        if (value instanceof String str)
            return '\"' + Util.escape(str) + '\"';

        if (value instanceof Character)
            return '\"' + value.toString() + '\"';

        if (value instanceof Number || value instanceof Boolean)
            return value.toString();

        if (value instanceof UUID uuid)
            return '\"' + uuid.toString() + '\"';

        if (value instanceof OffsetDateTime dateTime)
            return '\"' + formatter.format(dateTime) + '\"';

        if (value.getClass().isArray())
            return ArrayParser.arrayToJson(value);

        if (value instanceof Collection<?> collection)
            return ArrayParser.collectionToJson(collection);
//        maps

        return serialize(value);
    }



    public static String serialize(Object val)  throws NoSuchFieldException, IllegalAccessException, IOException {
        // null - array - map - char - string - primitive - number - b(B)oolean
        StringBuilder jsonString = new StringBuilder("{");
        Field[] fields = val.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            jsonString.append("\"").append(fields[i].getName()).append("\":");
            fields[i].setAccessible(true);
            jsonString.append(parseValue(fields[i].get(val)));
            if (fields.length > i + 1)
                jsonString.append(",");
        }
        jsonString.append("}");
        return String.valueOf(jsonString);
    }

}
