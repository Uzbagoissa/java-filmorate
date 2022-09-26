package ru.yandex.practicum.filmorate.exceptions;

public class InvalidDurationException extends Exception{
    public InvalidDurationException() {
    }

    public InvalidDurationException(final String message) {
        super(message);
    }
}
