package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private Map<Integer, Film> films;
    private Integer currentId;

    public FilmController() {
        currentId = 0;
        films = new HashMap<>();
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @ResponseBody
    @PostMapping(value = "/films")
    public Object saveFilm(@RequestBody Film film) {

        try {
            log.info("Получен POST-запрос к эндпоинту: '/films' на добавление фильма с ID={}", currentId + 1);
            if (isValidFilm(film)) {
                film.setId(++currentId);
                films.put(film.getId(), film);
            }
        } catch (ValidationException exp) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        } catch (NullPointerException exp) {
            log.error("Передан пустой аргумент!");
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return film;
    }

    @ResponseBody
    @PutMapping(value = "/films")
    public Object updateFilm(@RequestBody Film film) {
        try {
            log.info("Получен PUT-запрос к эндпоинту: '/films' на обновление фильма с ID={}", film.getId());
            if (film.getId() == null) {
                film.setId(currentId + 1);
            }
            if (isValidFilm(film)) {
                films.put(film.getId(), film);
                currentId++;
            }
        } catch (ValidationException exp) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        } catch (NullPointerException exp) {
            log.error("Передан пустой аргумент!");
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return film;
    }

    private boolean isValidFilm(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не должно быть пустым!");
        }
        if ((film.getDescription().length()) > 200 || (film.getDescription().isEmpty()))  {
            throw new ValidationException("Описание фильма больше 200 символов: " + film.getDescription().length());
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new ValidationException("Некорректная дата релиза фильма: " + film.getReleaseDate());
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность должна быть положительной: " + film.getDuration());
        }
        return true;
    }
}