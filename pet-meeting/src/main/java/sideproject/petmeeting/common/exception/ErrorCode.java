package sideproject.petmeeting.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "INVALID_INPUT_VALUE", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "METHOD_NOT_ALLOWED", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "ENTITY_NOT_FOUND", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "Server Error"),
    INVALID_TYPE_VALUE(400, "INVALID_TYPE_VALUE", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "HANDLE_ACCESS_DENIED", "Access is Denied"),


    //file
    FILE_NO_EXIST(500, "F001", "File is no exixt(파일이 존재하지 않습니다.)"),

    // == PostService ==//
    POST_NOT_EXIST(400, "P001", "게시글이 존재하지 않습니다.");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
