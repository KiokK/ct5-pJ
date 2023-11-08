package by.kihtenkoolga.parser.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static by.kihtenkoolga.parser.util.Constants.ARR_END;
import static by.kihtenkoolga.parser.util.Constants.ARR_SEPARATOR;
import static by.kihtenkoolga.parser.util.Constants.ARR_START;
import static by.kihtenkoolga.parser.util.Constants.NULL;
import static by.kihtenkoolga.parser.util.Parser.deserialize;

public class CollectionParser {

    protected static String collectionToJson(Collection<?> collection) throws IOException, NoSuchFieldException, IllegalAccessException {
        if (collection == null)
            return NULL;

        Iterator<?> iter = collection.iterator();

        StringBuilder jsonCollection = new StringBuilder();
        jsonCollection.append(ARR_START);
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

    protected static <T> List<T> deserializeList(char[] json, T clazz) throws IOException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        Type listElementType = ((ParameterizedType) clazz).getActualTypeArguments()[0];
        List<T> deserializeArr = new ArrayList<>();
        Parser.i++;
        while (json[Parser.i] != ARR_END) {
            T arrElement = deserialize(json, (Class<T>) listElementType);
            deserializeArr.add(arrElement);
        }
        return deserializeArr;
    }

}
