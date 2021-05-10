package com.check.chat.server;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientHandler {
    private int lastMsgTime;
    private MyServer server;
    private  Socket socket;
    private  DataInputStream in;
    private  DataOutputStream out;
    private String name="";

    public ClientHandler( Socket socket) {
        try {
            this.server=MyServer.getServer();
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            new Thread(() ->{
                try {
                    auth();
                    readMsg();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    closeConnection();
                }

            }).start();

        } catch (IOException e) {
            System.out.println("Проблемы при создании обработчика клиента");
            e.printStackTrace();
        }

    }

    private void  closeConnection() {
        server.unsubscribe(this);
        server.broadCastMsg(name+ " вышел из чата");
        try {
            in.close();
        }catch (IOException e){
            e.printStackTrace();
        } try {
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        } try {
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void auth() throws IOException{
            try {
                socket.setSoTimeout(50000);
                while (true) {
                    String str = in.readUTF();
                    if (str.startsWith("/auth")) {
                        String[] parts = str.split(" ");
                        String login = parts[1];
                        String password = parts[2];
                        String nick = server.getAuthService().getNickByLoginPass(login, password);
                        if (nick != null) {
                            if (!server.isNickBusy(nick)) {
                                socket.setSoTimeout(0);
                                sendMsg("/autok " + nick);
                                name = nick;
                                server.broadCastMsg(name + " зашел в чат");
                                server.subscribe(this);
                                sendMsg("/login: "+login);
                                return;
                            } else {
                                sendMsg("Учетная запись уже испльзуется");
                            }
                        } else {
                            sendMsg("Неверный логин/пароль");
                        }
                    } else {
                        sendMsg("Перед тем как отправлять сообщение авторизируйтесь через команду/auth login password");
                    }
                }

            }catch (SocketTimeoutException e){
                System.out.println("Отключен клиент");
                socket.close();
            }
        }


    public void sendMsg(String msg){
    try {
        out.writeUTF(msg);
    }catch (IOException e){
        e.printStackTrace();
    }
    }

    public void readMsg() throws IOException {
        while (true) {
            String strFromClient = in.readUTF();

            System.out.println("от " + name + ": " + strFromClient);
            if (strFromClient.startsWith("/")) {
                if (strFromClient.equals("/end")) {
                    return;
                }
                if (strFromClient.startsWith("/w")) {
                    String[] parts = strFromClient.split(" ");
                    String nickTo = parts[1];
                    String msg = strFromClient.substring(3 + nickTo.length() + 1);
                    server.sendMsgToClient(this, nickTo, msg);
                }
                if (strFromClient.startsWith("/change")){
                    String newNickname = strFromClient.split(" ")[1];
                    System.out.println(newNickname);
                    if (server.getAuthService().changeOfNickname(name,newNickname)){
                        System.out.print("Ник: "+name);
                        name=newNickname;
                        System.out.println(" изменен на: "+name);
                        server.broadcastClientList();
                    }else sendMsg("Ник занят");
                }continue;
            } server.broadCastMsg(name + ": " + strFromClient);
        }
    }


    public String getName() {
        return name;
    }


}