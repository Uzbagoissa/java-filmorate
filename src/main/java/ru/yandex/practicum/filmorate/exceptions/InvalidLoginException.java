package ru.yandex.practicum.filmorate.exceptions;

public class InvalidLoginException extends Exception{
    public InvalidLoginException() {
    }

    public InvalidLoginException(final String message) {
        super(message);
    }
}
