package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Component
public class FriendStorage {
    private final JdbcTemplate jdbcTemplate;
    private UserStorage userStorage;

    @Autowired
    public FriendStorage(JdbcTemplate jdbcTemplate, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if ((user != null) && (friend != null)) {
            boolean status = false;
            if (friend.getFriends().contains(userId)) {
                status = true;  // дружба стала взаимной
                String sql = "UPDATE friends SET user_id = ? AND friend_id = ? AND status = ? " +
                        "WHERE user_id = ? AND friend_id = ?";
                jdbcTemplate.update(sql, friendId, userId, true, friendId, userId);
            }
            String sql = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, userId, friendId, status);
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if ((user != null) && (friend != null)) {
            String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
            jdbcTemplate.update(sql, userId, friendId);
            if (friend.getFriends().contains(userId)) {
                // дружба стала невзаимной - нужно поменять статус
                sql = "UPDATE friends SET user_id = ? AND friend_id = ? AND status = ? " +
                        "WHERE user_id = ? AND friend_id = ?";
                jdbcTemplate.update(sql, friendId, userId, false, friendId, userId);
            }
        }
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getUserById(userId);
        if (user != null) {
            String sql = "SELECT friend_id, email, login, name, birthday FROM friends" +
                    " INNER JOIN users ON friends.friend_id = users.id WHERE friends.user_id = ?";
            return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                            rs.getLong("friend_id"),
                            rs.getString("email"),
                            rs.getString("login"),
                            rs.getString("name"),
                            rs.getDate("birthday").toLocalDate(),
                            null),
                    userId
            );
        } else {
            return null;
        }
    }
}