package sideproject.petmeeting.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Response {

    private StatusEnum status;
    private String message;
    private Object data;

    // Initial Setting
    public Response() {
        this.status = StatusEnum.BAD_REQUEST;
        this.message = null;
        this.data = null;
    }
}
