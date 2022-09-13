package web.fiiit.userservice.dto.token;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendToken {

    private Long id;

    private String value;

    private Long ownerId;

}
