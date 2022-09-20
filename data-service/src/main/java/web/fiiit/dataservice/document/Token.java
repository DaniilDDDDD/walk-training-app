package web.fiiit.dataservice.document;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("store-#{@environment.getProperty('spring.data.mongodb.default-collection')}")
@Data
@Builder
public class Token {

    @Id
    private Long id;

    private Long rootId;

    private String value;

    private Long ownerId;

    private Date expirationTime;

}
