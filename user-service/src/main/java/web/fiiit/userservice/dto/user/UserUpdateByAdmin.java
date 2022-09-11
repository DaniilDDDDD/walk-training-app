package web.fiiit.userservice.dto.user;

import lombok.Builder;
import lombok.Data;
import web.fiiit.userservice.model.Role;
import web.fiiit.userservice.model.Status;

import java.util.List;

@Data
@Builder
public class UserUpdateByAdmin {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private Status status;

    private List<Role> roles;

}
