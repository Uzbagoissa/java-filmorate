package ru.yandex.practicum.filmorate.exceptions;

public class InvalidReleaseDateException extends Exception{
    public InvalidReleaseDateException() {
    }

    public InvalidReleaseDateException(final String message) {
        super(message);
    }
}
