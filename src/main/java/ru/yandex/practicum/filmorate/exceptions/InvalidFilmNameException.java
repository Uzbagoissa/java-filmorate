package ru.yandex.practicum.filmorate.exceptions;

public class InvalidFilmNameException extends Exception{
    public InvalidFilmNameException() {
    }

    public InvalidFilmNameException(final String message) {
        super(message);
    }
}
