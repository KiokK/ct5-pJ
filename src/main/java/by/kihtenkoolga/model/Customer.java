package by.kihtenkoolga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends Base {

    private String firstName;
    private String lastName;
    private LocalDate dateBirth;
    private List<Order> orders;

}
