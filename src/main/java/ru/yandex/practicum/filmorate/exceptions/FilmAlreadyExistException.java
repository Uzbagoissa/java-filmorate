package ru.yandex.practicum.filmorate.exceptions;

public class FilmAlreadyExistException extends Exception{
    public FilmAlreadyExistException() {
    }

    public FilmAlreadyExistException(final String message) {
        super(message);
    }
}
