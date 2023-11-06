package by.kihtenkoolga.parser.util;

import java.util.UUID;

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
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(ch);
            }
        }
    }

    protected static <T> T castObject(Class<T> clazz, String object) {
        try {
            return clazz.cast(Integer.parseInt(object));
        } catch (NumberFormatException ignored) {
        }
        try {
            return clazz.cast(Double.parseDouble(object));
        } catch (NumberFormatException ignored) {
        }
        try {
            return clazz.cast(UUID.fromString(object));
        } catch (IllegalArgumentException ignored) {
        }
        return clazz.cast(object);
    }

}
