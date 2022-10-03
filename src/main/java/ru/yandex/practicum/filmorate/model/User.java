package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String email;
    private String name;
    private String login;
    private LocalDate birthday;
}
