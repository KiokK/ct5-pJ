package by.kihtenkoolga.parser.util;

import by.kihtenkoolga.exception.JsonDeserializeException;
import by.kihtenkoolga.exception.JsonIncorrectDataParseException;
import by.kihtenkoolga.exception.JsonParseFieldNameException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
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

    public static int i = 0;

    /**
     * Глубина десереализации
     */
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
            return Util.castObject(clazz, o.substring(1, o.length() - 1));
        } catch (RuntimeException ignored) {
        }

        T object = clazz.newInstance();
        Map<Object, Object> fieldValue = fromJson(json, clazz);

        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            fields[i].set(object, Util.castObject(fields[i].getType(), (Object) fieldValue.get(fields[i].getName())));
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
                    throw new JsonIncorrectDataParseException(Double.TYPE.getName());
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

    public static <T> Map.Entry<Object, Object> fromJsonO(char[] json, Class<T> mainClass) throws IOException, NoSuchFieldException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (json[i] == '"') {
            String field = getFieldName(json, i);
            String fieldName = field.substring(1, field.length() - 1);
            i++;
            if (json[i] == ':') {
                i++;
                Object o = null;
                if (json[i] != '[') {
                    o = deserialize(json, Arrays.stream(mainClass.getDeclaredFields())
                            .filter(f -> f.getName().equals(fieldName))
                            .findAny().orElseThrow(() -> new JsonParseFieldNameException(fieldName)).getType());
                } else {
                    if (json[i] == '[')
                        o = ArrayParser.deserializeArray(json, Arrays.stream(mainClass.getDeclaredFields())
                                .filter(f -> f.getName().equals(fieldName))
                                .findAny().orElseThrow(() -> new JsonParseFieldNameException(fieldName))
                                .getGenericType()
                        );
                }
                return Map.entry(fieldName, o);
            }
        }
        throw new JsonDeserializeException(String.valueOf(i));
    }

    public static <T> Map<Object, Object> fromJson(char[] json, Class<T> mainClass) throws IOException, NoSuchFieldException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Map<Object, Object> ans = new HashMap<>();
        if (json[i] == '{') {
            i++;

            while (json.length > i && json[i] != '{' && json[i] != ']') {
                if (json[i] == '"') {
                    Map.Entry<Object, Object> pair = fromJsonO(json, mainClass);
                    ans.put(pair.getKey().toString(),
                            pair.getValue());
                }
                i++;
            }
        }
        if (json.length <= i || json[i] == '{' || json[i] == ']')
            return ans;
        if (json[i] == ',')
            i++;

        return fromJson(json, mainClass);
    }

}
