package events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserEvent {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<String> roles;
    private Boolean deleted;

}
