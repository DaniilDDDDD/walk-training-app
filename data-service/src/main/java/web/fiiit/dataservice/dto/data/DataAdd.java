package web.fiiit.dataservice.dto.data;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class DataAdd {

    private Date startTime;

    private Date endTime;

    // TODO: add real user's data
    private String text;

}
