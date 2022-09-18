package web.fiiit.dataservice.document;

import lombok.Builder;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@lombok.Data
@Builder
public class Data {

    @Id
    private Long id;

    private Long ownerId;

    private Date startTime;

    private Date endTime;

    private String text;

}
