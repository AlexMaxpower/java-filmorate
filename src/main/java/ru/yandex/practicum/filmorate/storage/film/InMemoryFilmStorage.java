package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films;
    private Long currentId;

    public InMemoryFilmStorage() {
        currentId = 0L;
        films = new HashMap<>();
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        if (isValidFilm(film)) {
            film.setId(++currentId);
            films.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Фильм с ID=" + film.getId() + " не найден!");
        }
        if (isValidFilm(film)) {
            films.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм с ID=" + filmId + " не найден!");
        }
        return films.get(filmId);
    }

    @Override
    public Film delete(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм с ID=" + filmId + " не найден!");
        }
        return films.remove(filmId);
    }

    private boolean isValidFilm(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не должно быть пустым!");
        }
        if ((film.getDescription().length()) > 200 || (film.getDescription().isEmpty())) {
            throw new ValidationException("Описание фильма больше 200 символов или пустое: " + film.getDescription().length());
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Некорректная дата релиза фильма: " + film.getReleaseDate());
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность должна быть положительной: " + film.getDuration());
        }
        return true;
    }
}
