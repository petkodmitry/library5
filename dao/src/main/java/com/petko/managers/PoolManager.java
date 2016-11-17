package com.petko.managers;

import com.petko.MySQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class PoolManager {
    private static PoolManager instance;
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>();

    public static synchronized PoolManager getInstance(){
        if(instance == null){
            instance = new PoolManager();
        }
        return instance;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException{
        if (connectionHolder.get() == null) {
            Connection connection = MySQLConnection.getConnection();
            connectionHolder.set(connection);
        }
        return connectionHolder.get();
    }

    public void releaseConnection(Connection connection){
        if(connection != null){
//            try{
//                connection.close();
                connectionHolder.remove();
            /*} catch(SQLException e){
                e.printStackTrace();
            }*/
        }
    }
}
