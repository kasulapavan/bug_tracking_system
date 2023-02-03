package net.thrymr.task.custom.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.font.TextHitInfo;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Service
@NoArgsConstructor
public class ApiResponse {

    private int code;

    private String message;

    private Object payLoad;

    public ApiResponse(int value, String success) {
        this.code=value;
        this.message=success;

    }


    public ApiResponse(int code, String message,  Object payload) {
        this.code = code;
        this.message = message;
        this.payLoad=payload;
    }

    public ApiResponse(int code, Object payLoad) {
        this.code = code;
        this.payLoad = payLoad;
    }

    public ApiResponse(int value) {
        this.code=value;
    }
}
