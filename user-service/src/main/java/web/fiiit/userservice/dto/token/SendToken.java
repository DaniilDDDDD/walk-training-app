package web.fiiit.userservice.dto.token;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class SendToken {

    private Long id;

    private String value;

    private Long ownerId;

    private Date expirationDate;

}
