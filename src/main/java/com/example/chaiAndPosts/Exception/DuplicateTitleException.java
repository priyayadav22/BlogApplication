package com.example.chaiAndPosts.Exception;

public class DuplicateTitleException extends RuntimeException{
    public DuplicateTitleException(String message){
        super(message);
    }
}
