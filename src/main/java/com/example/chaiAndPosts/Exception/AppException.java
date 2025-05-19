package com.example.chaiAndPosts.Exception;

public class AppException extends  RuntimeException{
    private final String type;

    public AppException(String type, String errorMessage){
        super(errorMessage);
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
