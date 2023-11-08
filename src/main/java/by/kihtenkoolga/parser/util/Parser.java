package by.kihtenkoolga.parser.util;

import by.kihtenkoolga.exception.JsonDeserializeException;
import by.kihtenkoolga.exception.JsonIncorrectDataParseException;
import by.kihtenkoolga.exception.JsonParseFieldNameException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static by.kihtenkoolga.parser.util.CollectionParser.deserializeList;
import static by.kihtenkoolga.parser.util.Constants.ARR_END;
import static by.kihtenkoolga.parser.util.Constants.ARR_SEPARATOR;
import static by.kihtenkoolga.parser.util.Constants.ARR_START;
import static by.kihtenkoolga.parser.util.Constants.ESCAPED_SLASH;
import static by.kihtenkoolga.parser.util.Constants.FALSE;
import static by.kihtenkoolga.parser.util.Constants.FALSE_IN_QUOTES;
import static by.kihtenkoolga.parser.util.Constants.FIELD_VALUE_SEPARATOR;
import static by.kihtenkoolga.parser.util.Constants.NULL;
import static by.kihtenkoolga.parser.util.Constants.NULL_IN_QUOTES;
import static by.kihtenkoolga.parser.util.Constants.OBJECT_END;
import static by.kihtenkoolga.parser.util.Constants.OBJECT_START;
import static by.kihtenkoolga.parser.util.Constants.POINT;
import static by.kihtenkoolga.parser.util.Constants.QUOTATION_MARK;
import static by.kihtenkoolga.parser.util.Constants.TRUE;
import static by.kihtenkoolga.parser.util.Constants.TRUE_IN_QUOTES;
import static by.kihtenkoolga.parser.util.Constants.localDateFormatter;
import static by.kihtenkoolga.parser.util.Constants.offsetDateTimeFormatter;

public class Parser {

    public static int i = 0;
    private static int exceptionPosition = 0;

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
     */
    protected static String parseObject(Object obj) {
        if (parseValue(obj) instanceof String json)
            return json;
//      TODO:  Map serialization
        return serialize(obj);
    }

    /**
     * Парсит объект класса или наследника <i> Number, Boolean, String, примитивы, null, Arrays, Collection</i>, а так же
     * объекты классов <i> UUID, OffsetDateTime, LocalDate </i>
     *
     * @param obj объект, который будет приведён к строке
     * @return исходный объект, если он не подошел ни одному из перечисленных класов, или объект класса <b>String</b>
     * содержащий его представление в json
     */
    private static Object parseValue(Object obj) {
        if (obj == null)
            return NULL;

        if (obj.getClass().isPrimitive())
            return obj.toString();

        if (obj instanceof String str)
            return QUOTATION_MARK + Util.escape(str) + QUOTATION_MARK;

        if (obj instanceof Character)
            return QUOTATION_MARK + obj.toString() + QUOTATION_MARK;

        if (obj instanceof Number || obj instanceof Boolean)
            return obj.toString();

        if (obj instanceof UUID uuid)
            return QUOTATION_MARK + uuid.toString() + QUOTATION_MARK;

        if (obj instanceof OffsetDateTime dateTime)
            return QUOTATION_MARK + offsetDateTimeFormatter.format(dateTime) + QUOTATION_MARK;

        if (obj instanceof LocalDate localDate)
            return QUOTATION_MARK + localDateFormatter.format(localDate) + QUOTATION_MARK;

        if (obj.getClass().isArray())
            return ArrayParser.arrayToJson(obj);

        if (obj instanceof Collection<?> collection)
            return CollectionParser.collectionToJson(collection);
        return obj;
    }


    /**
     * Cериализует любой объект в последовательность, соответствующуу json формату. Метод {@link #parseObject(Object object)}
     * используется для сериализации значения конкретного поля объекта
     *
     * @param obj объект, который надо представить в json формате
     * @return json представление объекта
     */
    public static String serialize(Object obj) {
        if (parseValue(obj) instanceof String s)
            return s;
        StringBuilder jsonString = new StringBuilder();
        jsonString.append(OBJECT_START);
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            jsonString.append(QUOTATION_MARK)
                    .append(fields[i].getName())
                    .append(QUOTATION_MARK)
                    .append(FIELD_VALUE_SEPARATOR);
            fields[i].setAccessible(true);
            try {
                jsonString.append(parseObject(fields[i].get(obj)));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (fields.length > i + 1)
                jsonString.append(ARR_SEPARATOR);
        }
        jsonString.append(OBJECT_END);
        return String.valueOf(jsonString);
    }

    /**
     * Десериализует объект из заданного json представления и класса в
     *
     * @param json  последовательность символов, соответствующая json представлению объекта
     * @param clazz класс ожидаемого обекта
     * @param <T>   тип десериализуемого объекта
     * @return десериализованный объект класса {@code Class<T>}
     * @throws JsonDeserializeException при ошибочном представлении объекта в формате json
     */
    public static <T> T deserialize(char[] json, Class<T> clazz) throws JsonDeserializeException {
        if (json == null) {
            return null;
        }
        deserializeDepthIn();
        try {
            String nextSimpleJsonValue = getNextSimpleValueFromJson(json, i);
            deserializeDepthBack();
            return Util.castObject(clazz, nextSimpleJsonValue.substring(1, nextSimpleJsonValue.length() - 1));
        } catch (ClassCastException | JsonDeserializeException ignored) {
        }

        T object;
        Map<Object, Object> fieldValue;
        try {
            object = clazz.getDeclaredConstructor().newInstance();
            fieldValue = fromJson(json, clazz);

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(object, Util.castObject(field.getType(), fieldValue.get(field.getName())));
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            flush();
            throw new JsonDeserializeException(exceptionPosition);
        }

        deserializeDepthBack();
        return object;
    }

    /**
     * Выделяет в строку последовательность символов <i>примитива, String или класса обертки</i> при проходе начиная с
     * заданной позиции
     *
     * @param json массив json - источник поиска значения
     * @param pos  позиция начала поиска
     * @return строковое представление значения из json источника или исключение
     * @throws JsonDeserializeException        если значение оказалось не подходящим для обработки по условию
     * @throws JsonIncorrectDataParseException если значение было не верного типа, например некорректное строковое
     *                                         представление числа с плавающей точкой, значения boolean или null
     */
    public static String getNextSimpleValueFromJson(char[] json, int pos) throws JsonDeserializeException, JsonIncorrectDataParseException {
        StringBuilder val = new StringBuilder();
        if (json[pos] == QUOTATION_MARK) {
            val.append(json[pos++]);
            while ((pos - 1 >= 0 && json[pos] == QUOTATION_MARK && json[pos - 1] == ESCAPED_SLASH) || json[pos] != QUOTATION_MARK) {
                if (pos - 1 >= 0 && json[pos] == QUOTATION_MARK && json[pos - 1] == ESCAPED_SLASH)
                    val.deleteCharAt(val.length() - 1);
                val.append(json[pos++]);
            }
            val.append(json[pos]);
            i = ++pos;
            return val.toString();
        }
        if (json[pos] == NULL.charAt(0)) {
            if (pos + NULL.length() < json.length && NULL.equals(String.valueOf(Arrays.copyOfRange(json, pos, pos + NULL.length())))) {
                i += NULL.length();
                return NULL_IN_QUOTES;
            } else {
                flush();
                throw new JsonIncorrectDataParseException(null);
            }
        }
        if (json[pos] == TRUE.charAt(0)) {
            if (pos + TRUE.length() < json.length && TRUE.equals(String.valueOf(Arrays.copyOfRange(json, pos, pos + TRUE.length())))) {
                i += TRUE.length();
                return TRUE_IN_QUOTES;
            } else {
                flush();
                throw new JsonIncorrectDataParseException(Boolean.TYPE.getName());
            }
        }
        if (json[pos] == FALSE.charAt(0)) {
            if (pos + FALSE.length() < json.length && FALSE.equals(String.valueOf(Arrays.copyOfRange(json, pos, pos + FALSE.length())))) {
                i += FALSE.length();
                return FALSE_IN_QUOTES;
            } else {
                flush();
                throw new JsonIncorrectDataParseException(Boolean.TYPE.getName());
            }
        }
        if (Character.isDigit(json[pos])) {
            boolean isMetPoint = false;
            val.append(QUOTATION_MARK)
                    .append(json[pos++]);
            while (json.length > pos && (Character.isDigit(json[pos]) || json[pos] == POINT)) {
                if (json[pos] == POINT && isMetPoint) {
                    flush();
                    throw new JsonIncorrectDataParseException(Double.TYPE.getName());
                }
                if (json[pos] == POINT && !isMetPoint)
                    isMetPoint = true;
                val.append(json[pos++]);
            }
            i = pos;
            val.append(QUOTATION_MARK);
            return val.toString();
        }
        throw new JsonDeserializeException(i);
    }

    /**
     * Выделяет подстроку, являющуюся имененм поля начиная с символа " до следующего такого же
     *
     * @param json массив, содержащий структуру json - источник поиска значения
     * @param pos  позиция начала поиска
     * @return выделенное имя поля
     */
    private static String getFieldName(char[] json, int pos) {
        StringBuilder val = new StringBuilder();
        do {
            val.append(json[pos++]);
        } while (json[pos] != QUOTATION_MARK);
        val.append(json[pos]);
        i = pos;
        return val.toString();
    }

    /**
     * Выделяет поле объекта и его значение в {@code Map.Entry}
     *
     * @param json            последовательность символов, соответствующая json представлению объекта
     * @param objectCastClass класс десериализуемого обекта
     * @param <T>             тип десериализуемого объекта
     * @return {@code Map.Entry}, где key - поле объекта, а value - значение объекта
     */
    public static <T> Map.Entry<Object, Object> fromJsonFieldAndValue(char[] json, Class<T> objectCastClass) {
        if (json[i] == QUOTATION_MARK) {
            String field = getFieldName(json, i);
            final String FIELD_NAME = field.substring(1, field.length() - 1);
            System.out.println(FIELD_NAME);
            Field currentField;
            try {
                currentField = Arrays.stream(objectCastClass.getDeclaredFields())
                        .filter(f -> f.getName().equals(FIELD_NAME))
                        .findAny().orElseThrow(() -> new JsonParseFieldNameException(FIELD_NAME));
            } catch (JsonParseFieldNameException e) {
                flush();
                throw new JsonParseFieldNameException(FIELD_NAME);
            }
            i++;
            if (json[i] == FIELD_VALUE_SEPARATOR) {
                i++;
                Object deserializeObject;
                if (json[i] != ARR_START) {
                    deserializeObject = deserialize(json, currentField.getType());
                } else {
                    deserializeObject = deserializeList(json, currentField.getGenericType());
                }
                return Map.entry(FIELD_NAME, deserializeObject == null ? NULL : deserializeObject);
            }
        }
        flush();
        throw new JsonDeserializeException(exceptionPosition);

    }

    /**
     * Парсит json представление объекта на {@code Map<Object, Object>} содержащую наименования полей и значения этих полей в объекте
     * класса {@code Class<T>}
     *
     * @param json            последовательность символов, соответствующая json представлению объекта
     * @param objectCastClass класс десериализуемого обекта
     * @param <T>             тип десериализуемого объекта
     * @return Map содержащая поля и значения объекта класса Class<T>
     */
    public static <T> Map<Object, Object> fromJson(char[] json, Class<T> objectCastClass) {
        Map<Object, Object> parseFieldsWithValues = new HashMap<>();
        if (json[i] == OBJECT_START) {
            i++;

            while (json.length > i && json[i] != OBJECT_START && json[i] != ARR_END) {
                if (json[i] == QUOTATION_MARK) {
                    Map.Entry<Object, Object> pair = fromJsonFieldAndValue(json, objectCastClass);
                    parseFieldsWithValues.put(pair.getKey().toString(),
                            pair.getValue());
                }
                i++;
            }
        }
        if (json.length <= i || json[i] == OBJECT_START || json[i] == ARR_END) {
            return parseFieldsWithValues;
        }
        return fromJson(json, objectCastClass);
    }

    private static void flush() {
        exceptionPosition = i;
        i = 0;
        DEPTH = 0;
    }

    private static void deserializeDepthBack() {
        if (--DEPTH == 0) {
            i = 0;
        }
    }

    private static void deserializeDepthIn() {
        DEPTH++;
    }

}
