package web.fiiit.dataservice.dto.token;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TokenResponse {

    private Long id;

    private Long rootId;

    private Long ownerId;

    private String value;

    private Date expirationTime;

}
