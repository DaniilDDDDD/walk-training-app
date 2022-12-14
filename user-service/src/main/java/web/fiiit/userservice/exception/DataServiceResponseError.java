package web.fiiit.userservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataServiceResponseError extends RuntimeException {

    private int statusCode;

    public DataServiceResponseError(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
