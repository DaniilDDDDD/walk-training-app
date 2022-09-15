package web.fiiit.userservice.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import web.fiiit.userservice.dto.error.FieldExceptionResponse;
import web.fiiit.userservice.validation.ValidationErrorResponse;
import web.fiiit.userservice.validation.Violation;

import javax.validation.ConstraintViolationException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlerControllerAdvice {

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onConstraintValidationException(
            MethodArgumentNotValidException e
    ) {
        e.printStackTrace();
        final Violation violation = new Violation(
                e.getParameter().getParameterName(),
                e.getMessage()
        );
        return new ValidationErrorResponse(violation);
    }

    @ExceptionHandler({
            ConstraintViolationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e
    ) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler({
            JwtException.class,
            AuthenticationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public FieldExceptionResponse onUsernameNotFoundException(
            Exception e
    ) {
        return new FieldExceptionResponse(
                "login",
                e.getMessage()
        );
    }

    @ExceptionHandler({
            JwtAuthenticationException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public FieldExceptionResponse onJwtAuthenticationException(
            JwtAuthenticationException e
    ) {
        return new FieldExceptionResponse(
                e.getField(),
                e.getMessage()
        );
    }

    @ExceptionHandler({
            AccessDeniedException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public FieldExceptionResponse onAccessDeniedException(
            Exception e
    ) {
        return new FieldExceptionResponse(
                "Authorization",
                e.getMessage()
        );
    }

    @ExceptionHandler({
            NoSuchAlgorithmException.class,
            InvalidKeySpecException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public FieldExceptionResponse onAuthenticationAndAccessDeniedException(
            Exception e
    ) {
        return new FieldExceptionResponse(
                "Authorization",
                e.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public FieldExceptionResponse onException(
            Exception e
    ) {
        return new FieldExceptionResponse(
                "Internal server error!",
                e.getMessage()
        );
    }

}
