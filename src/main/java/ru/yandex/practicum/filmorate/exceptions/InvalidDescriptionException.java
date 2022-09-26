package ru.yandex.practicum.filmorate.exceptions;

public class InvalidDescriptionException extends Exception{
    public InvalidDescriptionException() {
    }

    public InvalidDescriptionException(final String message) {
        super(message);
    }
}
