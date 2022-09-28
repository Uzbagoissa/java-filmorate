package ru.yandex.practicum.filmorate.exceptions;

public class InvalidFilmException extends Exception{
    public InvalidFilmException() {
    }

    public InvalidFilmException(final String message) {
        super(message);
    }
}
