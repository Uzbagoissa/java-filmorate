package ru.yandex.practicum.filmorate.exceptions;

public class InvalidUserException extends Exception{
    public InvalidUserException() {
    }

    public InvalidUserException(final String message) {
        super(message);
    }
}
