package ru.yandex.practicum.filmorate.storage;

import java.util.HashMap;
import java.util.Map;

abstract class AbstractInMemoryStorage<T> {
    final Map<Integer, T> storage = new HashMap<>();
}
