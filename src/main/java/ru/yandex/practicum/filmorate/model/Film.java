package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
public class Film {

    private Integer id;
    @NotNull(message = "Name of the film can not be null")
    @NotBlank(message = "Name of the film can not be blank")
    private String name;
    @Size(message = "Description size of the film must be up 200 chars", min = 1, max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Duration of the film can not be negative or zero")
    private int duration;
    private Rating mpa;
    private Set<Genre> genres = new HashSet<>();

    @Setter(AccessLevel.NONE)
    private Set<Integer> usersLikes = new HashSet<>();
    public void addUserLike(int userId) {
        usersLikes.add(userId);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }
    public void clearGenre() {
        genres.clear();
    }
    public void deleteUserLike(int userId) {
        usersLikes.remove(userId);
    }
    public int getLikesCount() {
        return usersLikes.size();
    }

    private int compare(Genre g0, Genre g1) {
        return Integer.compare(g0.getId(), g1.getId());
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                ", mpa=" + mpa +
                ", genres=" + genres.stream().sorted(this::compare).collect(Collectors.toList()) +
                ", usersLikes=" + usersLikes +
                '}';
    }
}
