package ru.mipt.java2016.homework.g596.egorov.task4.users.controller;

public class Error {
    private int code;
    private String message;
    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
