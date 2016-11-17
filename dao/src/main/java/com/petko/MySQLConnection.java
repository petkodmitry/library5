package com.petko;

import com.petko.constants.Constants;
import com.petko.managers.ResourceDaoManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    public static Connection getConnection() throws ClassNotFoundException, SQLException{
        try {
            Class.forName(ResourceDaoManager.getInstance().getProperty(Constants.MYSQL_DRIVER));
            return DriverManager.getConnection(ResourceDaoManager.getInstance().getProperty(Constants.MYSQL_URL)
//                    + "&useJDBCCompliantTimezoneShift=true"
//                    + "&useLegacyDatetimeCode=false"
//                    + "&serverTimezone=UTC"
                    , ResourceDaoManager.getInstance().getProperty(Constants.MYSQL_USER)
                    , ResourceDaoManager.getInstance().getProperty(Constants.MYSQL_PSW));
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Не удалось загрузить класс " + ResourceDaoManager.getInstance().getProperty(Constants.MYSQL_DRIVER));
        } catch (SQLException e) {
            throw new SQLException("Не удалось подключиться к базе");
        }
    }
}
