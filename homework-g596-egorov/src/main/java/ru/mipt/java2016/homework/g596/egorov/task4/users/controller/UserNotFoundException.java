package ru.mipt.java2016.homework.g596.egorov.task4.users.controller;

public class UserNotFoundException extends RuntimeException {
    private long userId;

    public UserNotFoundException(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
