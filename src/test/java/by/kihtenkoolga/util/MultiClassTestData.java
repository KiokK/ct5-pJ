package by.kihtenkoolga.util;

import by.kihtenkoolga.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiClassTestData {

    private String title = "This class is only for testing different objects";
    private List<Product> products;
    private Boolean trueVal;
    private Boolean falseVal;
    private String nullVal;
}
