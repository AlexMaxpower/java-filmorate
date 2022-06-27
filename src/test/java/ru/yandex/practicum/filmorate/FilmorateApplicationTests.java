package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final FilmService filmService;
    private final UserService userService;
    private User firstUser;
    private User secondUser;
    private User thirdUser;
    private Film firstFilm;
    private Film secondFilm;
    private Film thirdFilm;

    @BeforeEach
    public void beforeEach() {
        firstUser = User.builder()
                .name("MisterFirst")
                .login("First")
                .email("1@ya.ru")
                .birthday(LocalDate.of(1980, 12, 23))
                .build();

        secondUser = User.builder()
                .name("MisterSecond")
                .login("Second")
                .email("2@ya.ru")
                .birthday(LocalDate.of(1980, 12, 24))
                .build();

        thirdUser = User.builder()
                .name("MisterThird")
                .login("Third")
                .email("3@ya.ru")
                .birthday(LocalDate.of(1980, 12, 25))
                .build();

        firstFilm = Film.builder()
                .name("Breakfast at Tiffany's")
                .description("American romantic comedy film directed by Blake Edwards, written by George Axelrod," +
                        " adapted from Truman Capote's 1958 novella of the same name.")
                .releaseDate(LocalDate.of(1961, 10, 5))
                .duration(114)
                .build();
        firstFilm.setMpa(new Mpa(1, "G"));
        firstFilm.setLikes(new HashSet<>());
        firstFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Драма"),
                new Genre(1, "Комедия"))));

        secondFilm = Film.builder()
                .name("Avatar")
                .description("American epic science fiction film directed, written, produced, and co-edited" +
                        " by James Cameron. It is set in the mid-22nd century when humans are colonizing Pandora...")
                .releaseDate(LocalDate.of(2009, 12, 10))
                .duration(162)
                .build();
        secondFilm.setMpa(new Mpa(3, "PG-13"));
        secondFilm.setLikes(new HashSet<>());
        secondFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(6, "Боевик"))));

        thirdFilm = Film.builder()
                .name("One Flew Over the Cuckoo's Nest")
                .description("American psychological comedy drama film directed by Milos Forman, based on" +
                        " the 1962 novel of the same name by Ken Kesey. The film stars Jack Nicholson...")
                .releaseDate(LocalDate.of(1975, 11, 19))
                .duration(133)
                .build();
        thirdFilm.setMpa(new Mpa(4, "R"));
        thirdFilm.setLikes(new HashSet<>());
        thirdFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Драма"))));
    }

    @Test
    public void testCreateUserAndGetUserById() {
        firstUser = userStorage.create(firstUser);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(firstUser.getId()));
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", firstUser.getId())
                                .hasFieldOrPropertyWithValue("name", "MisterFirst"));
    }

    @Test
    public void testGetUsers() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        List<User> listUsers = userStorage.getUsers();
        assertThat(listUsers).contains(firstUser);
        assertThat(listUsers).contains(secondUser);
    }

    @Test
    public void testUpdateUser() {
        firstUser = userStorage.create(firstUser);
        User updateUser = User.builder()
                .id(firstUser.getId())
                .name("UpdateMisterFirst")
                .login("First")
                .email("1@ya.ru")
                .birthday(LocalDate.of(1980, 12, 23))
                .build();
        Optional<User> testUpdateUser = Optional.ofNullable(userStorage.update(updateUser));
        assertThat(testUpdateUser)
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("name", "UpdateMisterFirst")
                );
    }

    @Test
    public void deleteUser() {
        firstUser = userStorage.create(firstUser);
        userStorage.delete(firstUser.getId());
        List<User> listUsers = userStorage.getUsers();
        assertThat(listUsers).hasSize(0);
    }

    @Test
    public void testCreateFilmAndGetFilmById() {
        firstFilm = filmStorage.create(firstFilm);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(firstFilm.getId()));
        assertThat(filmOptional)
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("id", firstFilm.getId())
                        .hasFieldOrPropertyWithValue("name", "Breakfast at Tiffany's")
                );
    }

    @Test
    public void testGetFilms() {
        firstFilm = filmStorage.create(firstFilm);
        secondFilm = filmStorage.create(secondFilm);
        thirdFilm = filmStorage.create(thirdFilm);
        List<Film> listFilms = filmStorage.getFilms();
        assertThat(listFilms).contains(firstFilm);
        assertThat(listFilms).contains(secondFilm);
        assertThat(listFilms).contains(thirdFilm);
    }

    @Test
    public void testUpdateFilm() {
        firstFilm = filmStorage.create(firstFilm);
        Film updateFilm = Film.builder()
                .id(firstFilm.getId())
                .name("UpdateName")
                .description("UpdateDescription")
                .releaseDate(LocalDate.of(1975, 11, 19))
                .duration(133)
                .build();
        updateFilm.setMpa(new Mpa(1, "G"));
        Optional<Film> testUpdateFilm = Optional.ofNullable(filmStorage.update(updateFilm));
        assertThat(testUpdateFilm)
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "UpdateName")
                                .hasFieldOrPropertyWithValue("description", "UpdateDescription")
                );
    }

    @Test
    public void deleteFilm() {
        firstFilm = filmStorage.create(firstFilm);
        secondFilm = filmStorage.create(secondFilm);
        filmStorage.delete(firstFilm.getId());
        List<Film> listFilms = filmStorage.getFilms();
        assertThat(listFilms).hasSize(1);
        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Avatar"));
    }

    @Test
    public void testAddLike() {
        firstUser = userStorage.create(firstUser);
        firstFilm = filmStorage.create(firstFilm);
        filmService.addLike(firstFilm.getId(), firstUser.getId());
        firstFilm = filmStorage.getFilmById(firstFilm.getId());
        assertThat(firstFilm.getLikes()).hasSize(1);
        assertThat(firstFilm.getLikes()).contains(firstUser.getId());
    }

    @Test
    public void testDeleteLike() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        firstFilm = filmStorage.create(firstFilm);
        filmService.addLike(firstFilm.getId(), firstUser.getId());
        filmService.addLike(firstFilm.getId(), secondUser.getId());
        filmService.deleteLike(firstFilm.getId(), firstUser.getId());
        firstFilm = filmStorage.getFilmById(firstFilm.getId());
        assertThat(firstFilm.getLikes()).hasSize(1);
        assertThat(firstFilm.getLikes()).contains(secondUser.getId());
    }

    @Test
    public void testGetPopularFilms() {

        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        thirdUser = userStorage.create(thirdUser);

        firstFilm = filmStorage.create(firstFilm);
        filmService.addLike(firstFilm.getId(), firstUser.getId());

        secondFilm = filmStorage.create(secondFilm);
        filmService.addLike(secondFilm.getId(), firstUser.getId());
        filmService.addLike(secondFilm.getId(), secondUser.getId());
        filmService.addLike(secondFilm.getId(), thirdUser.getId());

        thirdFilm = filmStorage.create(thirdFilm);
        filmService.addLike(thirdFilm.getId(), firstUser.getId());
        filmService.addLike(thirdFilm.getId(), secondUser.getId());

        List<Film> listFilms = filmService.getPopular(5);

        assertThat(listFilms).hasSize(3);

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Avatar"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "One Flew Over the Cuckoo's Nest"));

        assertThat(Optional.of(listFilms.get(2)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Breakfast at Tiffany's"));
    }

    @Test
    public void testAddFriend() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        assertThat(userService.getFriends(firstUser.getId())).hasSize(1);
        assertThat(userService.getFriends(firstUser.getId())).contains(secondUser);
    }

    @Test
    public void testDeleteFriend() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        thirdUser = userStorage.create(thirdUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        userService.deleteFriend(firstUser.getId(), secondUser.getId());
        assertThat(userService.getFriends(firstUser.getId())).hasSize(1);
        assertThat(userService.getFriends(firstUser.getId())).contains(thirdUser);
    }

    @Test
    public void testGetFriends() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        thirdUser = userStorage.create(thirdUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        assertThat(userService.getFriends(firstUser.getId())).hasSize(2);
        assertThat(userService.getFriends(firstUser.getId())).contains(secondUser, thirdUser);
    }

    @Test
    public void testGetCommonFriends() {
        firstUser = userStorage.create(firstUser);
        secondUser = userStorage.create(secondUser);
        thirdUser = userStorage.create(thirdUser);
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        userService.addFriend(secondUser.getId(), firstUser.getId());
        userService.addFriend(secondUser.getId(), thirdUser.getId());
        assertThat(userService.getCommonFriends(firstUser.getId(), secondUser.getId())).hasSize(1);
        assertThat(userService.getCommonFriends(firstUser.getId(), secondUser.getId()))
                .contains(thirdUser);
    }
}
