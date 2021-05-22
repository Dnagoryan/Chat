package com.check.chat.server;


import java.sql.*;
import java.util.Map;


public class BaseAuthService implements AuthService {
        Map<String, User> users;
        Statement statement;
        Connection connection;
        private final String url="jdbc:mysql://localhost:3306/?user=root";
        private final String urlPassword="Dnagoryan21061994";
        private final String userName="root";

    @Override
    public void start() {
        try {
            connection = DriverManager.getConnection(url, userName, urlPassword);
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    @Override
    public String getNickByLoginPass(String login, String password) {
        try {
            ResultSet resultSet= statement.executeQuery(String.format("SELECT * FROM chatsch.users WHERE login = '%s' " +
                    "&& password = '%s'",login, password));
            resultSet.next();
            return resultSet.getString(4);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Не найден ник");
        return null;
    }

    public boolean changeOfNickname (String nick, String newNickname){
        try {
            ResultSet switchNick=statement.executeQuery(String.format("SELECT * FROM chatsch.users WHERE nick ='%s'",newNickname));
            if (!switchNick.next()){
                statement.executeUpdate(String.format("UPDATE chatsch.users SET nick='%s' WHERE nick ='%s'",newNickname,nick));
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }    return false;
    }

    @Override
    public void stop() {
        System.out.println("Сервис остановился");

    }
}
