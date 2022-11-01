package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private int id;
    private String email;
    private String name;
    private String login;
    private LocalDate birthday;
    private List<Integer> friends = new ArrayList<>();


    @Builder
    public User(int id, String email, String name, String login, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.login = login;
        this.birthday = birthday;
    }

    public User() {
    }
}
