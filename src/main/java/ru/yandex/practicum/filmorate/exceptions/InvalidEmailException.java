package ru.yandex.practicum.filmorate.exceptions;

public class InvalidEmailException extends Exception{
    public InvalidEmailException() {
    }

    public InvalidEmailException(final String message) {
        super(message);
    }
}
