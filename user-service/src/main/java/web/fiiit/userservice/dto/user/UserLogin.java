package web.fiiit.userservice.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import web.fiiit.userservice.config.DtoConfiguration;

import javax.validation.constraints.NotNull;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLogin {
    @NotNull(
            groups = {DtoConfiguration.OnRequest.class, DtoConfiguration.OnResponse.class},
            message = "login field is not provided!"
    )
    private String login;  // username or email as login

    @NotNull(
            groups = DtoConfiguration.OnRequest.class,
            message = "password field is not provided!"
    )
    private String password;

    @NotNull(
            groups = DtoConfiguration.OnResponse.class
    )
    private String token;

}
