package com.check.chat.server;

public interface AuthService {
    void start();
    String getNickByLoginPass(String login, String password);
    boolean changeOfNickname (String nick, String newNickname);
    void stop();
}
