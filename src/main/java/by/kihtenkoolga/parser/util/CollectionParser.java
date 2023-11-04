package by.kihtenkoolga.parser.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import static by.kihtenkoolga.parser.util.Constants.ARR_END;
import static by.kihtenkoolga.parser.util.Constants.ARR_SEPARATOR;
import static by.kihtenkoolga.parser.util.Constants.ARR_START;
import static by.kihtenkoolga.parser.util.Constants.NULL;

public class CollectionParser {

    protected static String collectionToJson(Collection<?> collection) throws IOException, NoSuchFieldException, IllegalAccessException {
        if (collection == null)
            return NULL;

        Iterator<?> iter = collection.iterator();

        StringBuilder jsonCollection = new StringBuilder(ARR_START);
        Object nextValue;
        if (iter.hasNext()) {
            nextValue = iter.next();
            jsonCollection.append(Parser.parseObject(nextValue));
        }

        while (iter.hasNext()) {
            jsonCollection.append(ARR_SEPARATOR);
            nextValue = iter.next();
            jsonCollection.append(Parser.parseObject(nextValue));
        }

        jsonCollection.append(ARR_END);
        return String.valueOf(jsonCollection);
    }

}
