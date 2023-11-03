package by.kihtenkoolga.parser;

import by.kihtenkoolga.parser.util.Util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;


public class Parser {

    private static String parseValue(Object value) throws IOException, NoSuchFieldException, IllegalAccessException {
        if (value == null)
            return "null";

        if (value.getClass().isPrimitive())
            return value.toString();

        if (value instanceof String)
            return '\"' + Util.escape((String) value) + '\"';

        if (value instanceof Character)
            return '\"' + value.toString() + '\"';

        if (value instanceof Number || value instanceof Boolean)
            return value.toString();

        if (value instanceof UUID)
            return '\"' + ((UUID)value).toString() + '\"';

//        maps - collections - arrays

        return serialize(value);
    }



    public static String serialize(Object val)  throws NoSuchFieldException, IllegalAccessException, IOException {
        // null - array - map - char - string - primitive - number - b(B)oolean
        StringBuilder jsonString = new StringBuilder("{");
        Class<?> cls = val.getClass();
        Field[] fields = cls.getDeclaredFields();
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
