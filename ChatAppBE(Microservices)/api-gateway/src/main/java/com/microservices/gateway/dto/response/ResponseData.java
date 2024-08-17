package com.microservice.gateway.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ResponseData<T> implements Serializable {

    private int status;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ResponseData(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    //    Response data when the API executes successfully or getting error. For PUT, PATCH, DELETE
//    @param status
//    @param message
    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }
}