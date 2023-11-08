package by.kihtenkoolga.parser.util;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import static by.kihtenkoolga.parser.util.Constants.ESCAPED_SLASH;
import static by.kihtenkoolga.parser.util.Constants.FALSE;
import static by.kihtenkoolga.parser.util.Constants.NULL;
import static by.kihtenkoolga.parser.util.Constants.QUOTATION_MARK;
import static by.kihtenkoolga.parser.util.Constants.TRUE;

class Util {

    protected static String escape(String s) {
        if (s == null)
            return null;
        StringBuffer sb = new StringBuffer();
        escape(s, sb);
        return sb.toString();
    }

    protected static void escape(String s, StringBuffer sb) {
        final int len = s.length();
        for (int i = 0; i < len; i++) {
            char currentChar = s.charAt(i);
            switch (currentChar) {
                case QUOTATION_MARK -> sb.append("\\\"");
                case ESCAPED_SLASH -> sb.append("\\\\");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> sb.append(currentChar);
            }
        }
    }

    protected static <T> T castObject(Class<T> clazz, Object object) {
        if (NULL.equals(object)) {
            return null;
        }
        if (TRUE.equals(object)) {
            return clazz.cast(Boolean.TRUE);
        }
        if (FALSE.equals(object)) {
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
