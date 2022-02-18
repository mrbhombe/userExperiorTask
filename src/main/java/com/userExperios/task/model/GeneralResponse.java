package com.userExperios.task.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class GeneralResponse <T> {

    private T data;
    private Boolean success;
    private String msg;
    private Long timestamp;
    private Integer status;

    public GeneralResponse(T data, Boolean success, String msg, Long timestamp, HttpStatus status) {
        this.data = data;
        this.success = success;
        this.msg = msg;
        this.timestamp = timestamp;
        this.status = status.value();
    }
}
