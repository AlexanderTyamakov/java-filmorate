package ru.yandex.practicum.filmorate.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Data
public class Film {

    private int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;

}
