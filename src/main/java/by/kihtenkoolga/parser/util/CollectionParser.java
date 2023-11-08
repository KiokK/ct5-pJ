package by.kihtenkoolga.parser.util;

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

    /**
     * Сериализует коллекцию, реализующую интерфейс {@code Collection} , по средствам обхода каждого элемента с использованиме
     * @param collection коллекция, реализующую интерфейс {@code Collection}
     * @return сериализованый объект в формате json
     */
    protected static String collectionToJson(Collection<?> collection) {
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

    /**
     * Находит в {@code json[]} начиная с позиции {@link Parser#i} последовательность, ограниченную '[' и ']'
     * @param json последовательность символов json формата
     * @param classParameterizedType generic тип класса, реализующего Collection<E>
     * @return {@code List} элементов из json последовательности
     * @param <T> generic тип коллекции, например: {@code java.util.List< model.Product>}
     * @param <E> тип элемента в коллекции, например: {@code model.Product}
     */
    protected static <T, E> List<E> deserializeList(char[] json, T classParameterizedType) {
        Type listElementType = ((ParameterizedType) classParameterizedType).getActualTypeArguments()[0];
        List<E> deserializeArr = new ArrayList<>();
        Parser.i++;
        while (json[Parser.i] != ARR_END) {
            E arrElement = deserialize(json, (Class<E>) listElementType);
            deserializeArr.add(arrElement);
        }
        return deserializeArr;
    }

}
