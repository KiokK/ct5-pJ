package by.kihtenkoolga.parser;

import java.util.Iterator;
import java.util.Map;

import static by.kihtenkoolga.parser.Constants.ARR_SEPARATOR;
import static by.kihtenkoolga.parser.Constants.FIELD_VALUE_SEPARATOR;
import static by.kihtenkoolga.parser.Constants.NULL;
import static by.kihtenkoolga.parser.Constants.OBJECT_END;
import static by.kihtenkoolga.parser.Constants.OBJECT_START;
import static by.kihtenkoolga.parser.Constants.QUOTATION_MARK;
import static by.kihtenkoolga.parser.Parser.serialize;

public class MapParser {

    public static String serializeMap(Map<?, ?> map) {
        if (map == null)
            return NULL;

        Iterator<?> iter = map.entrySet().iterator();

        StringBuilder json = new StringBuilder();
        json.append(OBJECT_START);
        while (iter.hasNext()) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();

            json.append(QUOTATION_MARK)
                    .append(entry.getKey())
                    .append(QUOTATION_MARK)
                    .append(FIELD_VALUE_SEPARATOR);
            if (entry.getValue() == null) {
                json.append(NULL);
            } else {
                json.append(serialize(entry.getValue()));
            }
            json.append(ARR_SEPARATOR);
        }
        if (json.length() > 1) {
            json.deleteCharAt(json.length() - 1);
        }
        json.append(OBJECT_END);

        return json.toString();
    }
    
}
