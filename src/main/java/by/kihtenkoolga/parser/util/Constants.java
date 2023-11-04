package by.kihtenkoolga.parser.util;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final String offsetDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final DateTimeFormatter offsetDateTimeFormatter = DateTimeFormatter.ofPattern(offsetDateTimeFormat);
    public static final String localDateFormat = "yyyy-MM-dd";
    public static final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern(localDateFormat);

    protected static final String NULL = "null";
    protected static final String ARR_START = "[";
    protected static final String ARR_END = "]";
    protected static final String ARR_SEPARATOR = ",";
    protected static final String OBJECT_START = "{";
    protected static final String OBJECT_END = "}";

}
