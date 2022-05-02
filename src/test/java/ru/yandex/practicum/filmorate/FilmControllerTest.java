package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmControllerTest {
    private Film film;
    private FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
        film = new Film();
        film.setName("Breakfast at Tiffany's");
        film.setDescription("American romantic comedy film directed by Blake Edwards, written by George Axelrod," +
                " adapted from Truman Capote's 1958 novella of the same name.");
        film.setDuration(114);
        film.setReleaseDate(LocalDate.of(1961,10,5));
    }

    // проверка контроллера при корректных атрибутах фильма
    @Test
    public void shouldAddFilmWhenAllAttributeCorrect() {
        Object film1 = filmController.saveFilm(film);
        assertEquals(film, film1, "Переданный и полученный фильмы должны совпадать");
        assertEquals(1, filmController.getFilms().size(), "В списке должен быть один фильм");
    }

    // проверка контроллера при "пустом" фильме
    @Test
    public void shouldNoAddFilmWhenFilmIsNull() {
        film = null;
        Object object = filmController.saveFilm(film);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, filmController.getFilms().size(), "Список фильмов должен быть пустым");
    }

    // проверка контроллера, когда у фильма нет названия
    @Test
    public void shouldNoAddFilmWhenFilmNameIsNull() {
        film.setName(null);
        Object object = filmController.saveFilm(film);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, filmController.getFilms().size(), "Список фильмов должен быть пустым");
    }

    // проверка контроллера при "пустом" названии у фильма
    @Test
    public void shouldNoAddFilmWhenFilmNameIsEmpty() {
        film.setName("");
        Object object = filmController.saveFilm(film);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, filmController.getFilms().size(), "Список фильмов должен быть пустым");
    }

    // проверка контроллера, когда максимальная длина описания больше 200 символов
    @Test
    public void shouldNoAddFilmWhenFilmDescriptionMoreThan200Symbols() {
        film.setDescription(film.getDescription() + film.getDescription()); // длина описания 286 символов
        Object object = filmController.saveFilm(film);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, filmController.getFilms().size(), "Список фильмов должен быть пустым");
    }

    // проверка контроллера, когда у фильма нет описания
    @Test
    public void shouldNoAddFilmWhenFilmDescriptionIsNull() {
        film.setDescription(null);
        Object object = filmController.saveFilm(film);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, filmController.getFilms().size(), "Список фильмов должен быть пустым");
    }

    // проверка контроллера, когда дата релиза фильма раньше 28-12-1895
    @Test
    public void shouldNoAddFilmWhenFilmReleaseDateIsBefore28121895() {
        film.setReleaseDate(LocalDate.of(1895,12,27));
        Object object = filmController.saveFilm(film);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, filmController.getFilms().size(), "Список фильмов должен быть пустым");
    }

    // проверка контроллера, когда нет даты релиза фильма
    @Test
    public void shouldNoAddFilmWhenFilmReleaseDateIsNull() {
        film.setReleaseDate(null);
        Object object = filmController.saveFilm(film);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, filmController.getFilms().size(), "Список фильмов должен быть пустым");
    }

    // проверка контроллера, когда продолжительность фильма равна нулю
    @Test
    public void shouldNoAddFilmWhenFilmDurationIsZero() {
        film.setDuration(0);
        Object object = filmController.saveFilm(film);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, filmController.getFilms().size(), "Список фильмов должен быть пустым");
    }

    // проверка контроллера, когда продолжительность фильма отрицательная
    @Test
    public void shouldNoAddFilmWhenFilmDurationIsNegative() {
        film.setDuration(-1);
        Object object = filmController.saveFilm(film);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, filmController.getFilms().size(), "Список фильмов должен быть пустым");
    }

    // проверка контроллера, когда у фильма нет продолжительности
    @Test
    public void shouldNoAddFilmWhenFilmDurationIsNull() {
        film.setDuration(null);
        Object object = filmController.saveFilm(film);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, filmController.getFilms().size(), "Список фильмов должен быть пустым");
    }
}