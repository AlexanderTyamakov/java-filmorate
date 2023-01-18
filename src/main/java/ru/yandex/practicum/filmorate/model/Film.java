package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
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
    @JsonIgnore
    private Set<Integer> usersLikes = new HashSet<>();
    @NotNull
    private Rating rating;
    private Set<Integer> genres;

    public void addUserLike(int userId) {
        this.usersLikes.add(userId);
    }

    public void addGenre (Integer id) {
        this.genres.add(id);
    }
    public void deleteUserLike(int userId) {
        this.usersLikes.remove(userId);
    }
    public int getLikesCount() {
        return usersLikes.size();
    }
}
