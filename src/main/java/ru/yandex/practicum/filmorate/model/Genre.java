package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Genre {
    private Integer id;
    private String name;

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(Integer id) {
        this.id = id;
        this.name = "";
    }

    public Genre() {
        this.name = "";
    }

    public Integer getId() {
        return id;
    }
}
