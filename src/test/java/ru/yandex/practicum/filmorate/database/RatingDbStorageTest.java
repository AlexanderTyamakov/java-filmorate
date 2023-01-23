package ru.yandex.practicum.filmorate.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.database.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.database.interfaces.RatingStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FilmorateApplication.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class RatingDbStorageTest {
    private final RatingStorage gatingStorage;
    private int countRec;   //Так как в таблице уже есть начальные данные

    @BeforeEach
    void setUp() {
        countRec = gatingStorage.getValues().size();
    }

    @Test
    void add() {
        Integer id1 = countRec++;
        Rating expRating = getExpRating1((id1));
        gatingStorage.add(expRating);
        Rating actRating = gatingStorage.getById(expRating.getId());
        assertEquals(expRating.getId(),actRating.getId());
        assertEquals(expRating.getName(),actRating.getName());
    }

    @Test
    void replace() {
        Integer id1 = countRec++;
        Rating expRating = getExpRating1(id1);
        gatingStorage.add(expRating);
        expRating.setName("action");

        gatingStorage.replace(expRating);
        Rating actRating = gatingStorage.getById(expRating.getId());

        assertEquals(expRating.getId(), actRating.getId());
        assertEquals(expRating.getName(), actRating.getName());
    }

    @Test
    void getById() {
        Integer id1 = countRec++;
        Rating expRating = getExpRating1(id1);
        gatingStorage.add(expRating);
        Rating actRating = gatingStorage.getById(expRating.getId());
        assertEquals(expRating.getId(), actRating.getId());
        assertEquals(expRating.getName(), actRating.getName());
    }

    @Test
    void getValues() {
        Integer id1 = countRec++;
        Rating expRating1 = getExpRating1(id1);
        gatingStorage.add(expRating1);
        Integer id2 = countRec++;
        Rating expRating2 = getExpRating2(id2);
        gatingStorage.add(expRating2);

        List<Rating> actRatings = new ArrayList<>(gatingStorage.getValues());
        int i1 = actRatings.size() - 2;
        int i2 = actRatings.size() - 1;
        assertEquals(countRec, actRatings.size());
    }

    private Rating getExpRating1(Integer id) {
        Rating gating = new Rating();
        gating.setId(id);
        gating.setName("Rating1");
        return gating;
    }

    private Rating getExpRating2(Integer id) {
        Rating gating = new Rating();
        gating.setId(id);
        gating.setName("Rating2");
        return gating;
    }
}