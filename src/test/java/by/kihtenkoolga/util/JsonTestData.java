package by.kihtenkoolga.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonTestData {

    public static final String PATH = "src/test/java/by/kihtenkoolga/json/";

    public static String getJsonString() throws IOException {
        return FileUtils.readFileToString(new File(PATH + "test-string.json"), StandardCharsets.UTF_8);
    }

    public static String getJsonInt() throws IOException {
        return FileUtils.readFileToString(new File(PATH + "test-int.json"), StandardCharsets.UTF_8);
    }

    public static String getJsonProduct() throws IOException {
        return FileUtils.readFileToString(new File(PATH + "test-product.json"), StandardCharsets.UTF_8);
    }

}
