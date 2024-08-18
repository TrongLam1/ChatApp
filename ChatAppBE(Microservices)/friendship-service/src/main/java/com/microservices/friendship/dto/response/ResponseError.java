package com.microservices.friendship.dto.response;

public class ResponseError<T> extends ResponseData<Object> {

    public ResponseError(int status, String message) {
        super(status, message);
    }
}
