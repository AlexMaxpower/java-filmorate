package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private Map<Integer, User> users;
    private Integer currentId;

    public UserController() {
        currentId = 0;
        users = new HashMap<>();
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        // Обработка GET-запроса по пути "/users"
        return new ArrayList<>(users.values());
    }

    @ResponseBody
    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        log.info("Получен POST-запрос к эндпоинту: '/users' на добавление пользователя с ID={}", currentId + 1);
        if (isValidUser(user)) {
            user.setId(++currentId);
            users.put(user.getId(), user);
        }
        return user;
    }

    @ResponseBody
    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        log.info("Получен PUT-запрос к эндпоинту: '/users' на обновление пользователя с ID={}", user.getId());
        if (user.getId() == null) {
            user.setId(currentId + 1);
        }
        if (isValidUser(user)) {
            users.put(user.getId(), user);
            currentId++;
        }
        return user;
    }

    private boolean isValidUser(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный e-mail пользователя: " + user.getEmail());
        }
        if ((user.getLogin().isEmpty()) || (user.getLogin().contains(" "))) {
            throw new ValidationException("Некорректный логин пользователя: " + user.getLogin());
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Некорректная дата рождения пользователя: " + user.getBirthday());
        }
        return true;
    }
}