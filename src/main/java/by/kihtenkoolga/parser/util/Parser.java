package by.kihtenkoolga.parser.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static by.kihtenkoolga.parser.util.Constants.ARR_SEPARATOR;
import static by.kihtenkoolga.parser.util.Constants.NULL;
import static by.kihtenkoolga.parser.util.Constants.OBJECT_END;
import static by.kihtenkoolga.parser.util.Constants.OBJECT_START;
import static by.kihtenkoolga.parser.util.Constants.localDateFormatter;
import static by.kihtenkoolga.parser.util.Constants.offsetDateTimeFormatter;

public class Parser {

    private static int i = 0;

    /** Глубина десереализации */
    private static int DEPTH = 0;

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

    public static <T> T deserialize(char[] json, Class<T> clazz) throws NoSuchFieldException, IllegalAccessException, IOException, InstantiationException, ClassNotFoundException {
        if (json == null) {
            return null;
        }
        DEPTH++;
        try {
            String o = simpleVal(json, i);

            if (--DEPTH == 0)
                i = 0;
            if (DEPTH == 0)
                return Util.castObject(clazz,
                        o.substring(1, o.length() - 1));
            return Util.castObject(clazz, o);
        } catch (RuntimeException ignored) {
        }

        T object = clazz.newInstance();
        Map<Object, Object> fieldValue = fromJson(json);

        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            fields[i].set(object, Util.castObject(fields[i].getType(), (String) fieldValue.get(fields[i].getName())));
        }

        if (--DEPTH == 0)
            i = 0;
        return object;
    }

    public static String simpleVal(char[] json, int pos) {
        StringBuilder val = new StringBuilder("");

        if (json[pos] == '"') {
            val.append(json[pos++]);
            while (json[pos] != '"') {
                val.append(json[pos++]);
            }
            val.append(json[pos]);
            i = ++pos;
            return val.toString();
        }
        if (Character.isDigit(json[pos])) {
            boolean point = false;
            val.append(json[pos++]);
            while (json.length > pos && (Character.isDigit(json[pos]) || json[pos] == '.')) {
                if (json[pos] == '.' && point)
                    throw new RuntimeException();
                if (json[pos] == '.' && !point)
                    point = true;
                val.append(json[pos++]);
            }
            i = pos;
            return "\"" + val + "\"";
        }
        throw new RuntimeException();
    }

    private static String getFieldName(char[] json, int pos) {
        StringBuilder val = new StringBuilder("");
        do {
            val.append(json[pos++]);
        } while (json[pos] != '"');
        val.append(json[pos]);
        i = pos;
        return val.toString();
    }

    public static Map.Entry<Object, Object> fromJsonO(char[] json) throws IOException, NoSuchFieldException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (json[i] == '"') {
            String fieldName = getFieldName(json, i);
            i++;
            if (i < json.length)
                if (json[i] == ':') {
                    i++;
                    Object o = deserialize(json, Object.class);
                    return Map.entry(fieldName, o);
                }
        }
        return null;
    }

    public static Map<Object, Object> fromJson(char[] json) throws IOException, NoSuchFieldException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Map<Object, Object> ans = new HashMap<>();
        if (json[i] == '{') {
            i++;
            while (json.length > i && json[i] != '}') {
                Object o = fromJsonO(json);
                if (o instanceof Map.Entry<?, ?> pair) {
                    ans.put(pair.getKey().toString().substring(1, pair.getKey().toString().length() - 1),
                            pair.getValue().toString().substring(1, pair.getValue().toString().length() - 1));
                }
                i++;
            }
        }

        if (json.length <= i)
            return ans;
        return fromJson(json);
    }

}
