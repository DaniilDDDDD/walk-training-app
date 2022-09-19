package web.fiiit.dataservice.dto.data;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AddData {

    private Long ownerId;

    private Date startTime;

    private Date endTime;

    private String text;

}
