package sideproject.petmeeting.common.exception;

import lombok.*;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int statusCode;
    private String message;
    private String code;
    private List<FieldError> errors;



    private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.statusCode = code.getStatusCode();
        this.message = code.getMessage();
        this.code = code.getCode();
        this.errors = errors;

    }

    private ErrorResponse(final ErrorCode code) {
        this.statusCode = code.getStatusCode();
        this.message = code.getMessage();
        this.code = code.getCode();
        this.errors = new ArrayList<>();

    }

    public ErrorResponse(final ErrorCode code, final String message) {
        this.statusCode = code.getStatusCode();
        this.message = message;
        this.code = code.getCode();
        this.errors = new ArrayList<>();

    }

    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(ErrorCode code, String message) {
        return new ErrorResponse(code, message);

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String objectName;
        private String code;
        private String defaultMessage;
        private String rejectValue;


        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getObjectName(),
                            error.getCode(),
                            error.getDefaultMessage(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString()))
                    .collect(Collectors.toList());
        }
    }

}