package sideproject.petmeeting.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int status;
    private String message;
    private List<FieldError> errors;
    private String code;


    private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.errors = errors;
        this.code = code.getCode();
    }

    private ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.errors = new ArrayList<>();
    }


//    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
//        return new ErrorResponse(code, FieldError.of(bindingResult));
//    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors) {
        return new ErrorResponse(code, errors);
    }

//    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
////        final String rejectValue = e.getValue() == null ? "" : e.getValue().toString();
//        final List<ErrorResponse.FieldError> errors = ErrorResponse.FieldError.of(e.getName(), e.getErrorCode());
//        return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
//    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String objectName;
        private String code;
        private String defaultMessage;
        private String rejectValue;


        private FieldError(final String field, final String objectName, final String code, final String defaultMessage, final String rejectValue) {
            this.field = field;
            this.objectName = objectName;
            this.code = code;
            this.defaultMessage = defaultMessage;
            this.rejectValue = rejectValue;
        }

        public static List<FieldError> of(final String field, final String objectName, final String code, final String defaultMessage, final String rejectValue) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, objectName, code, defaultMessage, rejectValue));
            return fieldErrors;
        }

//        private static List<FieldError> of(final BindingResult bindingResult) {
//            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
//            return fieldErrors.stream()
//                    .map(error -> new FieldError(
//                            error.getField(),
//                            error.getObjectName(),
//                            error.getCode(),
//                            error.getDefaultMessage(),
//                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString())
//                    .collect(Collectors.toList());
//        }
    }


}