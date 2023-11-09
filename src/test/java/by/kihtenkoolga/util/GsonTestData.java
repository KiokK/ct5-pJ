package by.kihtenkoolga.util;

import by.kihtenkoolga.parser.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class GsonTestData {

    public static final DateTimeFormatter formatterOffset = Constants.offsetDateTimeFormatter;
    public static final DateTimeFormatter formatterLocalDt = Constants.localDateFormatter;

    public static final Gson gson = new GsonBuilder().serializeNulls()
            .registerTypeAdapter(OffsetDateTime.class,
                    (JsonSerializer<OffsetDateTime>) (localDate, type, jsonSerializationContext) ->
                            new JsonPrimitive(formatterOffset.format(localDate))
            )
            .registerTypeAdapter(OffsetDateTime.class, (JsonDeserializer<OffsetDateTime>)
                    (json, type, context) -> OffsetDateTime.parse(json.getAsString())
            )
            .registerTypeAdapter(LocalDate.class,
                    (JsonSerializer<LocalDate>) (localDate, type, jsonSerializationContext) ->
                            new JsonPrimitive(formatterLocalDt.format(localDate))
            )
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                    (json, type, context) -> LocalDate.parse(json.getAsString())
            )
            .create();
}
