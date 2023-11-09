package by.kihtenkoolga.parser;

import by.kihtenkoolga.exception.JsonDeserializeException;
import by.kihtenkoolga.exception.JsonIncorrectDataParseException;
import by.kihtenkoolga.exception.JsonParseFieldNameException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static by.kihtenkoolga.parser.Util.castToStringJsonOrReturnInitial;

public class Parser {

    protected static int i = 0;

    private static int exceptionPosition = 0;

    /**
     * Глубина десереализации
     */
    private static int DEPTH = 0;

    /**
     * Парсит переданный объект любой степени вложенности в json, использует метод
     * {@link Parser#serialize(Object obj)}
     *
     * @param obj объект, который будет переведён в json
     * @return строка представляющая json
     */
    protected static String parseObject(Object obj) {
        if (castToStringJsonOrReturnInitial(obj) instanceof String json) {
            return json;
        }
        return serialize(obj);
    }

    /**
     * Cериализует любой объект в последовательность, соответствующуу json формату. Метод {@link #parseObject(Object object)}
     * используется для сериализации значения конкретного поля объекта
     *
     * @param obj объект, который надо представить в json формате
     * @return json представление объекта
     */
    public static String serialize(Object obj) {
        if (castToStringJsonOrReturnInitial(obj) instanceof String s) {
            return s;
        }
        StringBuilder jsonString = new StringBuilder();
        jsonString.append(Constants.OBJECT_START);
        Map<String, Field> allFields = getAllFields(obj.getClass());
        for (var field : allFields.values()) {
            jsonString.append(Constants.QUOTATION_MARK)
                    .append(field.getName())
                    .append(Constants.QUOTATION_MARK)
                    .append(Constants.FIELD_VALUE_SEPARATOR);
            try {
                field.setAccessible(true);
                jsonString.append(parseObject(field.get(obj)));
            } catch (IllegalAccessException ignore) {
            }
            jsonString.append(Constants.ARR_SEPARATOR);
        }
        if (allFields.values().size() > 0) {
            jsonString.deleteCharAt(jsonString.length() - 1);
        }
        jsonString.append(Constants.OBJECT_END);
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
            return Util.castObjectToClassType(clazz, nextSimpleJsonValue.substring(1, nextSimpleJsonValue.length() - 1));
        } catch (ClassCastException | JsonDeserializeException ignored) {
        }

        T object;
        Map<Object, Object> fieldValue;
        try {
            object = clazz.getDeclaredConstructor().newInstance();
            Map<String, Field> allFields = getAllFields(clazz);
            fieldValue = fromJson(json, allFields, clazz);

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(object, Util.castObjectToClassType(field.getType(), fieldValue.get(field.getName())));
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
    private static String getNextSimpleValueFromJson(char[] json, int pos) throws JsonDeserializeException, JsonIncorrectDataParseException {
        StringBuilder val = new StringBuilder();
        if (json[pos] == Constants.QUOTATION_MARK) {
            val.append(json[pos++]);
            while ((pos - 1 >= 0 && json[pos] == Constants.QUOTATION_MARK && json[pos - 1] == Constants.ESCAPED_SLASH) || json[pos] != Constants.QUOTATION_MARK) {
                if (pos - 1 >= 0 && json[pos] == Constants.QUOTATION_MARK && json[pos - 1] == Constants.ESCAPED_SLASH) {
                    val.deleteCharAt(val.length() - 1);
                }
                val.append(json[pos++]);
            }
            val.append(json[pos]);
            i = ++pos;
            return val.toString();
        }
        if (json[pos] == Constants.NULL.charAt(0)) {
            if (pos + Constants.NULL.length() < json.length && Constants.NULL.equals(String.valueOf(Arrays.copyOfRange(json, pos, pos + Constants.NULL.length())))) {
                i += Constants.NULL.length();
                return Constants.NULL_IN_QUOTES;
            } else {
                flush();
                throw new JsonIncorrectDataParseException(null);
            }
        }
        if (json[pos] == Constants.TRUE.charAt(0)) {
            if (pos + Constants.TRUE.length() < json.length && Constants.TRUE.equals(String.valueOf(Arrays.copyOfRange(json, pos, pos + Constants.TRUE.length())))) {
                i += Constants.TRUE.length();
                return Constants.TRUE_IN_QUOTES;
            } else {
                flush();
                throw new JsonIncorrectDataParseException(Boolean.TYPE.getName());
            }
        }
        if (json[pos] == Constants.FALSE.charAt(0)) {
            if (pos + Constants.FALSE.length() < json.length && Constants.FALSE.equals(String.valueOf(Arrays.copyOfRange(json, pos, pos + Constants.FALSE.length())))) {
                i += Constants.FALSE.length();
                return Constants.FALSE_IN_QUOTES;
            } else {
                flush();
                throw new JsonIncorrectDataParseException(Boolean.TYPE.getName());
            }
        }
        if (Character.isDigit(json[pos])) {
            boolean isMetPoint = false;
            val.append(Constants.QUOTATION_MARK)
                    .append(json[pos++]);
            while (json.length > pos && (Character.isDigit(json[pos]) || json[pos] == Constants.POINT)) {
                if (json[pos] == Constants.POINT && isMetPoint) {
                    flush();
                    throw new JsonIncorrectDataParseException(Double.TYPE.getName());
                }
                if (json[pos] == Constants.POINT && !isMetPoint)
                    isMetPoint = true;
                val.append(json[pos++]);
            }
            i = pos;
            val.append(Constants.QUOTATION_MARK);
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
        } while (json[pos] != Constants.QUOTATION_MARK);
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
    private static <T> Map.Entry<Object, Object> fromJsonFieldAndValue(char[] json, Map<String, Field> allFields, Class<T> objectCastClass) {
        if (json[i] == Constants.QUOTATION_MARK) {
            String field = getFieldName(json, i);
            final String FIELD_NAME = field.substring(1, field.length() - 1);
            Field currentField = allFields.get(FIELD_NAME);
            if (currentField == null) {
                flush();
                throw new JsonParseFieldNameException(FIELD_NAME);
            }
            i++;
            if (json[i] == Constants.FIELD_VALUE_SEPARATOR) {
                i++;
                Object deserializeObject;
                if (json[i] != Constants.ARR_START) {
                    deserializeObject = deserialize(json, currentField.getType());
                } else {
                    deserializeObject = CollectionParser.deserializeList(json, currentField.getGenericType());
                }
                return Map.entry(FIELD_NAME, deserializeObject == null ? Constants.NULL : deserializeObject);
            }
        }
        flush();
        throw new JsonDeserializeException(exceptionPosition);
    }

    /**
     * Выделяет все поля класса, в том числе родительские по иерархии до класса Object
     *
     * @param clazz класс, с которого надо получить все поля
     * @return Map, где ключ - это имя поля String, а значение - само поле Field
     */
    private static Map<String, Field> getAllFields(Class<?> clazz) {
        Map<String, Field> allFields = new LinkedHashMap<>();
        if (clazz == Object.class) {
            return allFields;
        }
        do {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                allFields.put(field.getName(), field);
            }
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class);
        return allFields;
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
    private static <T> Map<Object, Object> fromJson(char[] json, Map<String, Field> allFields, Class<T> objectCastClass) {
        Map<Object, Object> parseFieldsWithValues = new HashMap<>();
        if (json[i] == Constants.OBJECT_START) {
            i++;

            while (json.length > i && json[i] != Constants.OBJECT_START && json[i] != Constants.ARR_END) {
                if (json[i] == Constants.QUOTATION_MARK) {
                    Map.Entry<Object, Object> pair = fromJsonFieldAndValue(json, allFields, objectCastClass);
                    parseFieldsWithValues.put(pair.getKey().toString(),
                            pair.getValue());
                }
                i++;
            }
        }
        if (json.length <= i || json[i] == Constants.OBJECT_START || json[i] == Constants.ARR_END) {
            return parseFieldsWithValues;
        }
        return fromJson(json, allFields, objectCastClass);
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
