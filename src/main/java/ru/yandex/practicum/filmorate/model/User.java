package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;

@Data
public class User {

    private int id;
    @NotNull(message = "Email can not be null")
    @NotBlank(message = "Email can not be blank")
    @Email(message = "Email is not valid")
    private String email;
    @NotNull(message = "Login can not be null")
    @NotBlank(message = "Login can not be blank")
    private String login;
    private String name;
    @PastOrPresent(message = "Birthday can not be in the future")
    private LocalDate birthday;
    @JsonIgnore
    private Set<Integer> friends;

    public void addFriends(int id) {
        this.friends.add(id);
    }
    public void deleteFriends(int id) {
        this.friends.remove(id);
    }
    public List<Integer> getFiends() {
        return new ArrayList<>(friends);
    }
    public boolean containsFriend(Integer id){
        return friends.contains(id);
    }
}
