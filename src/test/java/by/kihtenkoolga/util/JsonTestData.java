package by.kihtenkoolga.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonTestData {

    public static final String PATH = "src/test/java/by/kihtenkoolga/json/";
    public static final String PATH_FOR_EXCEPTIONS = PATH + "withexceptions/";

    public static String getJsonString() throws IOException {
        return FileUtils.readFileToString(new File(PATH + "test-string.json"), StandardCharsets.UTF_8);
    }

    public static String getJsonInt() throws IOException {
        return FileUtils.readFileToString(new File(PATH + "test-int.json"), StandardCharsets.UTF_8);
    }

    public static String getJsonProduct() throws IOException {
        return FileUtils.readFileToString(new File(PATH + "test-product.json"), StandardCharsets.UTF_8);
    }

    public static String getJsonOrder() throws IOException {
        return FileUtils.readFileToString(new File(PATH + "test-order.json"), StandardCharsets.UTF_8);
    }

    public static String getJsonCustomer() throws IOException {
        return FileUtils.readFileToString(new File(PATH + "test-customer.json"), StandardCharsets.UTF_8);
    }

    public static String getMultiClass() throws IOException {
        return FileUtils.readFileToString(new File(PATH + "test-multi-class.json"), StandardCharsets.UTF_8);
    }

    public static String getProductWithExceptionNull() throws IOException {
        return FileUtils.readFileToString(new File(PATH_FOR_EXCEPTIONS + "exception-test-null-product.json"), StandardCharsets.UTF_8);
    }

    public static String getProductWithExceptionTrue() throws IOException {
        return FileUtils.readFileToString(new File(PATH_FOR_EXCEPTIONS + "exception-test-true-multi-class.json"), StandardCharsets.UTF_8);
    }

    public static String getProductWithExceptionFalse() throws IOException {
        return FileUtils.readFileToString(new File(PATH_FOR_EXCEPTIONS + "exception-test-false-multi-class.json"), StandardCharsets.UTF_8);
    }

    public static String getProductWithExceptionPrice() throws IOException {
        return FileUtils.readFileToString(new File(PATH_FOR_EXCEPTIONS + "exception-test-incorrect-price-product.json"), StandardCharsets.UTF_8);
    }

}
