package by.kihtenkoolga.parser.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

import static by.kihtenkoolga.parser.util.Constants.ARR_SEPARATOR;
import static by.kihtenkoolga.parser.util.Constants.NULL;
import static by.kihtenkoolga.parser.util.Constants.OBJECT_END;
import static by.kihtenkoolga.parser.util.Constants.OBJECT_START;
import static by.kihtenkoolga.parser.util.Constants.localDateFormatter;
import static by.kihtenkoolga.parser.util.Constants.offsetDateTimeFormatter;

public class Parser {

    /**
     * Парсит переданный объект любой степени вложенности в json, использует методы
     * {@link by.kihtenkoolga.parser.util.Parser#parseValue(Object obj)} и
     * {@link by.kihtenkoolga.parser.util.Parser#serialize(Object obj)}
     *
     * @param obj объект, который будет переведён в json
     * @return строка представляющая json
     * @throws IOException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    protected static String parseObject(Object obj) throws IOException, NoSuchFieldException, IllegalAccessException {
        if (parseValue(obj) instanceof String json)
            return json;
//      TODO:  Map serialization
        return serialize(obj);
    }

    /**
     * Парсит объект класса или наследника <i> Number, Boolean, String, примитивы, null, Arrays, Collection</i>, а так же
     * объекты классов <i> UUID, OffsetDateTime, LocalDate </i>
     *
     * @param obj
     * @return исходный объект, если он не подошел ни одному из перечисленных класов, или объект класса <b>String</b>
     * содержащий его представление в json
     * @throws IOException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object parseValue(Object obj) throws IOException, NoSuchFieldException, IllegalAccessException {
        if (obj == null)
            return NULL;

        if (obj.getClass().isPrimitive())
            return obj.toString();

        if (obj instanceof String str)
            return '\"' + Util.escape(str) + '\"';

        if (obj instanceof Character)
            return '\"' + obj.toString() + '\"';

        if (obj instanceof Number || obj instanceof Boolean)
            return obj.toString();

        if (obj instanceof UUID uuid)
            return '\"' + uuid.toString() + '\"';

        if (obj instanceof OffsetDateTime dateTime)
            return '\"' + offsetDateTimeFormatter.format(dateTime) + '\"';

        if (obj instanceof LocalDate localDate)
            return '\"' + localDateFormatter.format(localDate) + '\"';

        if (obj.getClass().isArray())
            return ArrayParser.arrayToJson(obj);

        if (obj instanceof Collection<?> collection)
            return CollectionParser.collectionToJson(collection);
        return obj;
    }


    public static String serialize(Object obj) throws NoSuchFieldException, IllegalAccessException, IOException {
        if (parseValue(obj) instanceof String s)
            return s;
        StringBuilder jsonString = new StringBuilder(OBJECT_START);
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            jsonString.append("\"").append(fields[i].getName()).append("\":");
            fields[i].setAccessible(true);
            jsonString.append(parseObject(fields[i].get(obj)));
            if (fields.length > i + 1)
                jsonString.append(ARR_SEPARATOR);
        }
        jsonString.append(OBJECT_END);
        return String.valueOf(jsonString);
    }

}
