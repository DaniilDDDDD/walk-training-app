package web.fiiit.userservice.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdate {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

}
