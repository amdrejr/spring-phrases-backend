package com.amdrejr.phrases.exceptions.customExceptions;

public class UserInexistentOrInvalidPassword extends RuntimeException {
    public UserInexistentOrInvalidPassword(String msg) {
        super(msg);
    }
}
