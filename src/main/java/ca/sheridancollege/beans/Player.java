package ca.sheridancollege.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Player {

    private String name;
    private int age;
    private String gender;
    private String phone;
    private String email;

}
