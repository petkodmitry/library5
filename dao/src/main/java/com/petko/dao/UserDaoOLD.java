package com.petko.dao;

import com.petko.DaoException;
import com.petko.entities.UserEntityOLD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDaoOLD implements DaoOLD<UserEntityOLD> {
    private static UserDaoOLD instance;

    private UserDaoOLD() {}

    public static synchronized UserDaoOLD getInstance() {
        if (instance == null) {
            instance = new UserDaoOLD();
        }
        return instance;
    }

    public boolean isLoginSuccess(Connection connection, String login, String password) throws DaoException{
        try {
            PreparedStatement statement = null;
            ResultSet result = null;
            try {
                statement = connection.prepareStatement("SELECT * FROM USERS WHERE login = ? AND psw = ?");
                statement.setString(1, login);
                statement.setString(2, password);
                result = statement.executeQuery();
                return result.next();
            } finally {
                if (result != null) result.close();
                if (statement != null) statement.close();
            }
        } catch (SQLException e) {
            throw new DaoException("Ошибка выполнения запроса логина/пароля к базе");
        }
    }

    public int getUserStatus(Connection connection, String login) throws DaoException{
        try {
            PreparedStatement statement = null;
            ResultSet result = null;
            try {
                statement = connection.prepareStatement("SELECT isadmin FROM USERS WHERE login = ?");
                statement.setString(1, login);
                result = statement.executeQuery();
                if (result.next()) {
                    return result.getInt("isadmin");
                }
                return 0;
            } finally {
                if (result != null) result.close();
                if (statement != null) statement.close();
            }
        } catch (SQLException e) {
            throw new DaoException("Ошибка выполнения запроса к базе об уровне пользователя");
        }
    }

    @Override
    public void add(UserEntityOLD entity) {

    }

    public void add(Connection connection, UserEntityOLD entity) throws DaoException{
        try {
//            UserEntityOLD answer = new UserEntityOLD();
            PreparedStatement statement = null;
//            ResultSet result = null;
            try {
                statement = connection.prepareStatement("INSERT INTO USERS (fname, lname, login, psw, isadmin, isblocked) VALUES (?, ?, ?, ?, ?, ?)");
                statement.setString(1, entity.getFirstName());
                statement.setString(2, entity.getLastName());
                statement.setString(3, entity.getLogin());
                statement.setString(4, entity.getPassword());
                statement.setBoolean(5, entity.isAdmin());
                statement.setBoolean(6, entity.isBlocked());
                statement.executeUpdate();
            } finally {
//                if (result != null) result.close();
                if (statement != null) statement.close();
            }
        } catch (SQLException e) {
            throw new DaoException("Ошибка выполнения запроса на добавление пользователя");
        }
    }

    public List<UserEntityOLD> getAll() {
        return null;
    }

    public Set<String> getAllLogins(Connection connection) throws DaoException{
        Set<String> answer = new HashSet<String>();
        try {
            PreparedStatement statement = null;
            ResultSet result = null;
            try {
                statement = connection.prepareStatement("SELECT login FROM USERS");
                result = statement.executeQuery();
                while (result.next()) {
                    String user = result.getString(1);
                    answer.add(user);
                }
                return answer;
            } finally {
                if (result != null) result.close();
                if (statement != null) statement.close();
            }
        } catch (SQLException e) {
            throw new DaoException("Ошибка выполнения запроса к базе о всех логинах");
        }
    }

    public UserEntityOLD getById(int id) {
        return null;
    }

    public UserEntityOLD getByLogin(Connection connection, String login) throws DaoException{
        try {
            UserEntityOLD answer = new UserEntityOLD();
            PreparedStatement statement = null;
            ResultSet result = null;
            try {
                statement = connection.prepareStatement("SELECT * FROM USERS WHERE login = ?");
                statement.setString(1, login);
                result = statement.executeQuery();
                if (result.next()) {
                    answer = createNewEntity(result.getString(2), result.getString(3), result.getString(4),
                            result.getString(5), result.getBoolean(6), result.getBoolean(7));
                    answer.setUserId(result.getInt(1));
//                    answer.setFirstName(result.getString(2));
//                    answer.setLastName(result.getString(3));
//                    answer.setLogin(result.getString(4));
//                    answer.setPassword(result.getString(5));
//                    answer.setAdmin(result.getBoolean(6));
//                    answer.setBlocked(result.getBoolean(7));
                }
                return answer;
            } finally {
                if (result != null) result.close();
                if (statement != null) statement.close();
            }
        } catch (SQLException e) {
            throw new DaoException("Ошибка выполнения запроса информации о пользователе");
        }
    }

    public Set<UserEntityOLD> getAllByBlock(Connection connection, boolean isBlocked) throws DaoException{
        Set<UserEntityOLD> answer = new HashSet<UserEntityOLD>();
        try {
            PreparedStatement statement = null;
            ResultSet result = null;
            try {
                statement = connection.prepareStatement("SELECT * FROM USERS WHERE isblocked = ?");
                statement.setBoolean(1, isBlocked);
                result = statement.executeQuery();
                while (result.next()) {
                    UserEntityOLD entity = createNewEntity(result.getString(2), result.getString(3), result.getString(4),
                            result.getString(5), result.getBoolean(6), result.getBoolean(7));
                    entity.setUserId(result.getInt(1));
                    answer.add(entity);
                }
                return answer;
            } finally {
                if (result != null) result.close();
                if (statement != null) statement.close();
            }
        } catch (SQLException e) {
            throw new DaoException("Ошибка выполнения запроса к базе о всех пользователях по блокировочному статусу");
        }
    }

    public void update(Connection connection, UserEntityOLD entity) throws DaoException {
        try {
            PreparedStatement statement = null;
            try {
                statement = connection.prepareStatement("UPDATE USERS SET fname = ?, lname = ?, login = ?, psw = ?, isadmin = ?, isblocked = ? WHERE uid = ?");
                statement.setString(1, entity.getFirstName());
                statement.setString(2, entity.getLastName());
                statement.setString(3, entity.getLogin());
                statement.setString(4, entity.getPassword());
                statement.setBoolean(5, entity.isAdmin());
                statement.setBoolean(6, entity.isBlocked());
                statement.setInt(7, entity.getUserId());
                statement.executeUpdate();
            } finally {
                if (statement != null) statement.close();
            }
        } catch (SQLException e) {
            throw new DaoException("Ошибка обновления данных пользователя " + entity.getLogin());
        }
    }

    public UserEntityOLD createNewEntity(String name, String lastName, String login, String password,
                                         boolean isAdmin, boolean isBlocked) {
        UserEntityOLD result = new UserEntityOLD();
        result.setFirstName(name);
        result.setLastName(lastName);
        result.setLogin(login);
        result.setPassword(password);
        result.setAdmin(isAdmin);
        result.setBlocked(isBlocked);
        return result;
    }

    public void delete(int id) {

    }
}
