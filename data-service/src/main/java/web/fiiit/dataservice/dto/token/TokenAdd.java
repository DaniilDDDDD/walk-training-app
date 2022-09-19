package web.fiiit.dataservice.dto.token;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TokenAdd {

    private Long rootId;

    private String value;

    private Long ownerId;

    private Date expirationTime;

}
