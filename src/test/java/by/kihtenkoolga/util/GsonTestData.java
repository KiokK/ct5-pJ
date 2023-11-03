package by.kihtenkoolga.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class GsonTestData {
    public static final String dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
    public static final Gson gson = new GsonBuilder().serializeNulls()
            .registerTypeAdapter(OffsetDateTime.class,
                    (JsonSerializer<OffsetDateTime>) (localDate, type, jsonSerializationContext) -> {
                        System.out.println(new JsonPrimitive(formatter.format(localDate)));
                        return new JsonPrimitive(formatter.format(localDate));
                    }
            )
            .create();
}
