package by.kihtenkoolga.parser.util;

import java.time.format.DateTimeFormatter;

class Constants {
    public static final String dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

    protected static final String NULL = "null";
    protected static final String ARR_START = "[";
    protected static final String ARR_END = "]";
    protected static final String ARR_SEPARATOR = ",";

}
