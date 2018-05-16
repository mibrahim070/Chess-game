package com.chess.exceptions;

public class NotValidMoveException extends Exception {
    public NotValidMoveException(String message) {
        super(message);
    }
}
