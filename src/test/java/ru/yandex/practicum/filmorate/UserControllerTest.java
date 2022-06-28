package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    private User user;
    private UserController userController;
    private UserStorage userStorage;

    @BeforeEach
    public void beforeEach() {
        userStorage = new InMemoryUserStorage();
        userController = new UserController(userStorage, new UserService(userStorage, null));
        user = User.builder()
                .name("MyName")
                .login("MaxPower")
                .email("1@ya.ru")
                .birthday(LocalDate.of(1980, 12, 23))
                .build();
    }

    // проверка контроллера при корректных атрибутах пользователя
    @Test
    public void shouldAddUserWhenAllAttributeCorrect() {
        User user1 = userController.create(user);
        assertEquals(user, user1, "Переданный и полученный пользователь должны совпадать");
        assertEquals(1, userController.getUsers().size(), "В списке должен быть один пользователь");
    }

    // проверка контроллера при "пустой" электронной почте пользователя
    @Test
    public void shouldNoAddUserWhenUserEmailIsEmpty() {
        user.setEmail("");
        assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }

    // проверка контроллера, когда электронная почта не содержит символа @
    @Test
    public void shouldNoAddUserWhenUserEmailIsNotContainsCommercialAt() {
        user.setEmail("notemail.ru");
        assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }

    // проверка контроллера, когда у пользователя пустой логин
    @Test
    public void shouldNoAddUserWhenUserLoginIsEmpty() {
        user.setLogin("");
        assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }

    // проверка контроллера, когда логин пользователя содержит пробелы
    @Test
    public void shouldNoAddUserWhenUserLoginIsContainsSpaces() {
        user.setLogin("Max Power");
        assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }

    // проверка контроллера, когда имя пользователя пустое
    @Test
    public void shouldAddUserWhenUserNameIsEmpty() {
        user.setName("");
        User user1 = userController.create(user);
        assertTrue(user1.getName().equals(user.getLogin()),
                "Имя и логин пользователя должны совпадать");
        assertEquals(1, userController.getUsers().size(), "В списке должен быть один пользователь");
    }

    // проверка контроллера, когда дата рождения пользователя в будущем
    @Test
    public void shouldAddUserWhenUserBirthdayInFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }
}