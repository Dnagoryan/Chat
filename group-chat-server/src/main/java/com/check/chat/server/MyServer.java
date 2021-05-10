package com.check.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {
    private static MyServer server;
    public static MyServer getServer(){
        return server;
    }
    private final int PORT=8199;
    private  List<ClientHandler> clients;
    private AuthService authService;


    public MyServer(){
        server=this;
    try(ServerSocket serverSocket=new ServerSocket(PORT)) {
        authService=new BaseAuthService();
        authService.start();
        clients=new ArrayList<>();
        while (true){
            System.out.println("Ждем подключения клиента");
            Socket socket= serverSocket.accept();
            System.out.println("Клиент подключился");

            new ClientHandler(socket);
        }
    }catch (IOException e){
        e.printStackTrace();
    }finally {
        if (authService!=null){
            authService.stop();
        }
    }
    }




    public synchronized void  unsubscribe(ClientHandler clientHandler){

        clients.remove(clientHandler);
        broadcastClientList();
    }

    public  synchronized void  subscribe(ClientHandler clientHandler){

        clients.add(clientHandler);
        broadcastClientList();
    }

    public synchronized void  broadCastMsg(String msg){
        for (ClientHandler clientHandler:clients){
            clientHandler.sendMsg(msg);
            }
    }

    public synchronized void sendMsgToClient(ClientHandler fromClient,String nickTo ,String msg){
        for (ClientHandler clientHandler:clients) {
            if (clientHandler.getName().equals(nickTo)) {
   //             clientHandler.sendMsg("от пользователя "+ fromClient.getName()+": "+msg);
                fromClient.sendMsg("/шепот от "+nickTo+": "+msg);
                return;
            }
        }
        fromClient.sendMsg("Участник с ником \" "+nickTo+"\" не найден");

    }

    public synchronized void broadcastClientList(){
        StringBuilder sb=new StringBuilder("/clients ");
        for (ClientHandler clientHandler:clients) {
                sb.append(" ").append(clientHandler.getName());
            }
        broadCastMsg(sb.toString());
    }

    public synchronized  boolean isNickBusy(String nick){
        for (ClientHandler clientHandler:clients){
            if (clientHandler.getName().equals(nick)){
                return true;
            }
        }return false;
    }
    public AuthService getAuthService() {
        return authService;
    }



}
