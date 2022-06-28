package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.HashSet;
import java.util.List;


@Component
public class LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private MpaService mpaService;
    private GenreService genreService;

    @Autowired
    public LikeStorage(JdbcTemplate jdbcTemplate, MpaService mpaService, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    public void addLike(Long filmId, Long userId) {
        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public List<Film> getPopular(Integer count) {
        String getPopularQuery = "SELECT id, name, description, release_date, duration, rating_id " +
                "FROM films LEFT JOIN film_likes ON films.id = film_likes.film_id " +
                "GROUP BY films.id ORDER BY COUNT(film_likes.user_id) DESC LIMIT ?";

        return jdbcTemplate.query(getPopularQuery, (rs, rowNum) -> new Film(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_Date").toLocalDate(),
                        rs.getInt("duration"),
                        new HashSet<>(getLikes(rs.getLong("id"))),
                        mpaService.getMpaById(rs.getInt("rating_id")),
                        genreService.getFilmGenres(rs.getLong("id"))),
                count);
    }

    public List<Long> getLikes(Long filmId) {
        String sql = "SELECT user_id FROM film_likes WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), filmId);
    }
}