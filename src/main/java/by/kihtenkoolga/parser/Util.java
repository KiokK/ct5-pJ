package by.kihtenkoolga.parser;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.UUID;

public class Util {

    public static String escape(String s) {
        if (s == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        escape(s, sb);

        return sb.toString();
    }

    public static void escape(String s, StringBuffer sb) {
        final int len = s.length();
        for (int i = 0; i < len; i++) {
            char currentChar = s.charAt(i);
            switch (currentChar) {
                case Constants.QUOTATION_MARK -> sb.append("\\\"");
                case Constants.ESCAPED_SLASH -> sb.append("\\\\");
                case Constants.BACKSPACE -> sb.append("\\b");
                case Constants.FORMFEED -> sb.append("\\f");
                case Constants.NEW_LINE -> sb.append("\\n");
                case Constants.CARRIAGE_RETURN -> sb.append("\\r");
                case Constants.TAB -> sb.append("\\t");
                default -> sb.append(currentChar);
            }
        }
    }

    /**
     * Приводит объект класса или наследника <i> Number, Boolean, String, примитивы, null, Arrays, Collection</i>, а так же
     * объекты классов <i> UUID, OffsetDateTime, LocalDate </i>
     *
     * @param obj объект, который будет приведён к строке
     * @return исходный объект, если он не подошел ни одному из перечисленных класов, или объект класса <b>String</b>
     * содержащий его представление в json
     */
    public static Object castToStringJsonOrReturnInitial(Object obj) {
        if (obj == null) {
            return Constants.NULL;
        }
        if (obj.getClass().isPrimitive()) {
            return obj.toString();
        }
        if (obj instanceof String str) {
            return Constants.QUOTATION_MARK + Util.escape(str) + Constants.QUOTATION_MARK;
        }
        if (obj instanceof Character) {
            return Constants.QUOTATION_MARK + obj.toString() + Constants.QUOTATION_MARK;
        }
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        if (obj instanceof UUID uuid) {
            return Constants.QUOTATION_MARK + uuid.toString() + Constants.QUOTATION_MARK;
        }
        if (obj instanceof OffsetDateTime dateTime) {
            return Constants.QUOTATION_MARK + Constants.offsetDateTimeFormatter.format(dateTime) + Constants.QUOTATION_MARK;
        }
        if (obj instanceof LocalDate localDate) {
            return Constants.QUOTATION_MARK + Constants.localDateFormatter.format(localDate) + Constants.QUOTATION_MARK;
        }
        if (obj.getClass().isArray()) {
            return ArrayParser.arrayToJson(obj);
        }
        if (obj instanceof Collection<?> collection) {
            return CollectionParser.collectionToJson(collection);
        }

        return obj;
    }

    public static <T> T castObjectToClassType(Class<T> clazz, Object object) throws ClassCastException {
        if (Constants.NULL.equals(object)) {
            return null;
        }
        if (Constants.TRUE.equals(object)) {
            return clazz.cast(Boolean.TRUE);
        }
        if (Constants.FALSE.equals(object)) {
            return clazz.cast(Boolean.FALSE);
        }
        if (object.getClass().equals(clazz))
            return clazz.cast(object);
        try {
            return clazz.cast(UUID.fromString(object.toString()));
        } catch (IllegalArgumentException ignored) {
        }
        try {
            return clazz.cast(Integer.parseInt(object.toString()));
        } catch (NumberFormatException ignored) {
        }
        try {
            return clazz.cast(Double.parseDouble(object.toString()));
        } catch (NumberFormatException ignored) {
        }
        try {
            return clazz.cast(OffsetDateTime.parse(object.toString()));
        } catch (DateTimeParseException ignored) {
        }
        try {
            return clazz.cast(LocalDate.parse(object.toString()));
        } catch (DateTimeParseException ignored) {
        }
        try {
            return clazz.cast(object.toString());
        } catch (ClassCastException ignored) {
        }

        return clazz.cast(object);
    }

}
