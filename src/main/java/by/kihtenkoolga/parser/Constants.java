package by.kihtenkoolga.parser;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final String offsetDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final DateTimeFormatter offsetDateTimeFormatter = DateTimeFormatter.ofPattern(offsetDateTimeFormat);
    public static final String localDateFormat = "yyyy-MM-dd";
    public static final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern(localDateFormat);
    protected static final String NULL = "null";
    protected static final String TRUE = "true";
    protected static final String FALSE = "false";
    protected static final char ARR_START = '[';
    protected static final char ARR_END = ']';
    protected static final char ARR_SEPARATOR = ',';
    protected static final char OBJECT_START = '{';
    protected static final char OBJECT_END = '}';
    protected static final char POINT = '.';
    protected static final char FIELD_VALUE_SEPARATOR = ':';
    protected static final char QUOTATION_MARK = '"';
    protected static final String NULL_IN_QUOTES = "\"null\"";
    protected static final String TRUE_IN_QUOTES = "\"true\"";
    protected static final String FALSE_IN_QUOTES = "\"false\"";
    protected static final char ESCAPED_SLASH = '\\';
    protected static final char TAB = '\t';
    protected static final char NEW_LINE = '\n';
    protected static final char CARRIAGE_RETURN = '\r';
    protected static final char FORMFEED = '\f';
    protected static final char BACKSPACE = '\b';

}
