package web.fiiit.dataservice.dto.data;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class DataUpdate {

    private Date startTime;

    private Date endTime;

    private String text;

}
