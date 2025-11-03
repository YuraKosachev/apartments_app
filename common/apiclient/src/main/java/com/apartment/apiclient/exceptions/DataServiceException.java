package com.apartment.apiclient.exceptions;

public class DataServiceException extends RuntimeException{
    private int code;
    public DataServiceException(int code, String message){
        super(message);
        this.code = code;

    }
}

