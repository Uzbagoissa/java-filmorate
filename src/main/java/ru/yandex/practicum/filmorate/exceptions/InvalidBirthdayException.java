package ru.yandex.practicum.filmorate.exceptions;

public class InvalidBirthdayException extends Exception{
    public InvalidBirthdayException() {
    }

    public InvalidBirthdayException(final String message) {
        super(message);
    }
}
