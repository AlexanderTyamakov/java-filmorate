package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
public class Rating {
    private Integer id;
    private String name;

    public Rating(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Rating(Integer id) {
        this.id = id;
        this.name = "";
    }

    public Rating() {
        this.name = "";
    }

    public Integer getId() {
        return id;
    }
}
