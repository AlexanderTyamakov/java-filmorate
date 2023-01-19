package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.RatingNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.database.interfaces.RatingStorage;

import java.util.Collection;
import java.util.stream.Collectors;
@Slf4j
@Service
public class RatingService {

    private final RatingStorage ratingStorage;

    @Autowired
    public RatingService(@Qualifier("RatingDbStorage")RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public Collection<Rating> getRatings() {
        return ratingStorage.getValues();
    }

    public Rating getRatingById(Integer id) {
        if (!getIds().contains(id)) {
            log.error("Рейтинг в коллекции не найден");
            throw new RatingNotFoundException("Ошибка при поиске: рейтинг id = " + id + " не найден");
        }
        else {
            return ratingStorage.getById(id);
        }
    }

    private Collection<Integer> getIds() {
        return ratingStorage.getValues().stream()
                .map(Rating::getId)
                .collect(Collectors.toList());
    }
}
