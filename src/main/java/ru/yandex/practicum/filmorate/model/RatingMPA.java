package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingMPA {
    private int id;
    private String name;

    public RatingMPA(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
