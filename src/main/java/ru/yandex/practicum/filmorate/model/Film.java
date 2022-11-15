package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private List<Integer> likes = new ArrayList<>();
    private Mpa mpa;
    private int rate;
    private List<Genre> genres = new ArrayList<>();

    @Builder
    public Film(int id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa, int rate, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.rate = rate;
        this.genres = genres;
    }
}
