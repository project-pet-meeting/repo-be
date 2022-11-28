package sideproject.petmeeting.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;

@RestControllerAdvice // Global Level using, 에러 전역 처리를 위한 어노테이션(Controller, Service 에서 throw 한 예외를 일괄 처리
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 비즈니스 요구사항에 따른 Exception
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.error("handleEntityNotFoundException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse errorResponse = ErrorResponse.of(errorCode);

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

//    /**
//     * @Valid 유효성 체크에 통과하지 못하면 MethodArgumentNotValidException 발생
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        log.error("handleMethodArgumentNotValidException", e);
//        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
//        // Of 메서드:Null 값을 입력 받을 시 NullPointerException
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * Handler 에서 예외처리 되지 않은 Exception 처리
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(Exception.class)
//    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
//        log.error("handleEntityNotFoundException", e);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
//
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(IOException.class)
//    protected ResponseEntity<ErrorResponse> handleIOException(IOException e) {
//        log.error("handleIOException", e);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * RequestPart 데이터가 없을 시 발생하는 에러 예외 처리
//     */
//    @ExceptionHandler(MissingServletRequestPartException.class)
//    protected ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
//        log.error("handleMissingServletRequestPartException", e);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getMessage());
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * request parameter 가 없을 시 발생하는 에러 예외 처리
//     */
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
//        log.error("handleMissingServletRequestParameterException", e);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * 이미지 파일 업로드 용량 초과 시 발생하는 에러 예외 처리
//     */
//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    protected ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
//        log.error("handleMaxUploadSizeExceededException", e);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.FILE_SIZE_EXCEED);
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * 지원하지 않은 HTTP method 호출 시 발생하는 에러 예외 처리
//     */
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
//        log.error("handleHttpRequestMethodNotSupportedException", e);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
//
//        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
//    }
//
//    /**
//     * 지원하지 않은 Content Type 으로 호출 시 발생하는 에러 예외 처리
//     */
//    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
//    protected ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
//        log.error("handleHttpRequestMethodNotSupportedException", e);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_TYPE_VALUE, e.getMessage());
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }


//    /**
//     * @ModelAttribute 으로 binding error 발생시 BindException 발생한다.
//     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
//     */
//    @ExceptionHandler(BindException.class)
//    protected ResponseEntity<ExceptionResponse> handleBindException(org.springframework.validation.BindException e ) {
//        log.error("handleBindException", e);
//        final ExceptionResponse response = ExceptionResponse.of(ErrorCode.INVALID_INPUT_VALUE,e.getBindingResult());
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * enum type 일치하지 않아 binding 못할 경우 발생
//     * 주로 @RequestParam enum 으로 binding 못했을 경우 발생
//     */
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    protected ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
//        log.error("handleMethodArgumentTypeMismatchException", e);
//        final ExceptionResponse response = ExceptionResponse.of(e);
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//

//
//    /**
//    * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
//     */
//    @ExceptionHandler(AccessDeniedException.class)
//    protected ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException e) {
//        log.error("handleAccessDeniedException", e);
//        final ExceptionResponse response = ExceptionResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
//
//        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.HANDLE_ACCESS_DENIED.getStatus()));
//    }
//


}
