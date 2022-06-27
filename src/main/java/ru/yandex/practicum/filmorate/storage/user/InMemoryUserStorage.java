package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    public Map<Long, User> users;

    private Long currentId;

    public InMemoryUserStorage() {
        currentId = 0L;
        users = new HashMap<>();
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        if (isValidUser(user)) {
            user.setId(++currentId);
            users.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователь с ID=" + user.getId() + " не найден!");
        }
        if (isValidUser(user)) {
            users.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с ID=" + userId + " не найден!");
        }
        return users.get(userId);
    }

    @Override
    public User delete(Long userId) {
        if (userId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с ID=" + userId + " не найден!");
        }
        // удаляем из списка друзей пользователя у других пользователей
        for (User user : users.values()) {
            user.getFriends().remove(userId);
        }
        return users.remove(userId);
    }

    private boolean isValidUser(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный e-mail пользователя: " + user.getEmail());
        }
        if ((user.getLogin().isEmpty()) || (user.getLogin().contains(" "))) {
            throw new ValidationException("Некорректный логин пользователя: " + user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Некорректная дата рождения пользователя: " + user.getBirthday());
        }
        return true;
    }
}
