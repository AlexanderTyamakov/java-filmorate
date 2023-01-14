package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.*;

import javax.validation.constraints.*;

@Data
public class User {

    private int id;
    @NotNull(message = "Email can not be null")
    @NotBlank(message = "Email can not be blank")
    @Email(message = "Email is not valid")
    private final String email;
    @NotNull(message = "Login can not be null")
    @NotBlank(message = "Login can not be blank")
    private final String login;
    private String name;
    @PastOrPresent(message = "Birthday can not be in the future")
    private final LocalDate birthday;

}
