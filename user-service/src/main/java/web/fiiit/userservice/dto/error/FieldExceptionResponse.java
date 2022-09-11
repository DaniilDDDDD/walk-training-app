package web.fiiit.userservice.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FieldExceptionResponse {

    private String fieldName;

    private String message;

}
