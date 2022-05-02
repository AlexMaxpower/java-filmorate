package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTest {

    private User user;
    private UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
        user = new User();
        user.setName("MyName");
        user.setLogin("MaxPower");
        user.setEmail("1@ya.ru");
        user.setBirthday(LocalDate.of(1980,12,23));
    }

    // проверка контроллера при корректных атрибутах пользователя
    @Test
    public void shouldAddUserWhenAllAttributeCorrect() {
        Object user1 = userController.saveUser(user);
        assertEquals(user, user1, "Переданный и полученный пользователь должны совпадать");
        assertEquals(1, userController.getUsers().size(), "В списке должен быть один пользователь");
    }

    // проверка контроллера при "пустом" пользователе
    @Test
    public void shouldNoAddUserWhenUserIsNull() {
        user = null;
        Object object = userController.saveUser(user);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }

    // проверка контроллера, когда у пользователя нет электронной почты
    @Test
    public void shouldNoAddUserWhenUserEmailIsNull() {
        user.setEmail(null);
        Object object = userController.saveUser(user);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }

    // проверка контроллера при "пустой" электронной почте пользователя
    @Test
    public void shouldNoAddUserWhenUserEmailIsEmpty() {
        user.setEmail("");
        Object object = userController.saveUser(user);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }

    // проверка контроллера, когда электронная почта не содержит символа @
    @Test
    public void shouldNoAddUserWhenUserEmailIsNotContainsCommercialAt() {
        user.setEmail("notemail.ru");
        Object object = userController.saveUser(user);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }

    // проверка контроллера, когда у пользователя нет логина
    @Test
    public void shouldNoAddUserWhenUserLoginIsNull() {
        user.setLogin(null);
        Object object = userController.saveUser(user);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }

    // проверка контроллера, когда у пользователя пустой логин
    @Test
    public void shouldNoAddUserWhenUserLoginIsEmpty() {
        user.setLogin("");
        Object object = userController.saveUser(user);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }

    // проверка контроллера, когда логин пользователя содержит пробелы
    @Test
    public void shouldNoAddUserWhenUserLoginIsContainsSpaces() {
        user.setLogin("Max Power");
        Object object = userController.saveUser(user);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }

    // проверка контроллера, когда имя пользователя пустое
    @Test
    public void shouldAddUserWhenUserNameIsEmpty() {
        user.setName("");
        Object object = userController.saveUser(user);
        assertTrue(((User)object).getName().equals(user.getLogin()),
                "Имя и логин пользователя должны совпадать");
        assertEquals(1, userController.getUsers().size(), "В списке должен быть один пользователь");
    }

    // проверка контроллера, когда нет имени пользователя
    @Test
    public void shouldAddUserWhenUserNameIsNull() {
        user.setName(null);
        Object object = userController.saveUser(user);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }

    // проверка контроллера, когда дата рождения пользователя в будущем
    @Test
    public void shouldAddUserWhenUserBirthdayInFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));
        Object object = userController.saveUser(user);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }

    // проверка контроллера, когда даты рождения пользователя не существует
    @Test
    public void shouldAddUserWhenUserBirthdayIsNull() {
        user.setBirthday(null);
        Object object = userController.saveUser(user);
        assertTrue(object.toString().contains("BAD_REQUEST"), "Должен быть получен 400 ответ от сервера");
        assertEquals(0, userController.getUsers().size(), "Список пользователей должен быть пустым");
    }
}