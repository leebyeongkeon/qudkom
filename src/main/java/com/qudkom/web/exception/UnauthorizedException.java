package com.qudkom.web.exception;

public class UnauthorizedException extends Exception{
    //redirect, refresh, alert, prompt
    private String action="";
    private String redirectUri="";
    public UnauthorizedException(){}
    public UnauthorizedException(String message, String action, String redirectUri){

    }
}
