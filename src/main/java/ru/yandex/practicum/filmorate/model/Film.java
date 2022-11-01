package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private List<Integer> likes = new ArrayList<>();
    private List<Integer> genres = new ArrayList<>();
    private int rating;

    @Builder
    public Film(int id, String name, String description, LocalDate releaseDate, int duration, int rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rating = rating;
    }

    public Film() {
    }
}
