package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();
    Film create(Film film);
    Film update(Film film);
    Film getFilmById(Long filmId);
    Film delete(Long filmId);
}
