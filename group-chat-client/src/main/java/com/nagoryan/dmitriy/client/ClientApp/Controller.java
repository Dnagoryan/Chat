package com.nagoryan.dmitriy.client.ClientApp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;
import org.apache.commons.io.input.ReversedLinesFileReader;

import javax.security.auth.callback.Callback;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    TextArea chatArea;
    @FXML
    TextField inputField;
    @FXML
    Button sendBtn;

    private static boolean continueRead = true;
    private final String SERVER_IP = "localhost";
    private final int SERVER_PORT = 8199;


    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private File messageHistoryFile;
    private String nickname = null;
    private String login = null;
    private final int MAX_MESSAGE_IN_FILE = 100;
    private FileReader fileReader;
    private FileWriter fileWriter;
    private ReversedLinesFileReader reversedReader;
    private List<String> list;

    @FXML
    private void initialize() throws IOException {
        try {
            openConnection();
            addCloseListener();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Сервер не работает");
            alert.showAndWait();
            e.printStackTrace();
            throw e;
        }
    }

    private void openConnection() throws IOException {
        socket = new Socket(SERVER_IP, SERVER_PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (continueRead) {
                        System.out.println("Начало чтения");
                        String strFromServer = in.readUTF();
                        System.out.println("Считал: " + strFromServer);
                        if (strFromServer.equalsIgnoreCase("/end")) {
                            System.out.println("конец цикла");
                            break;
                        } else if (strFromServer.startsWith("/login")) {
                            login = strFromServer.split(": ")[1];
                            initHistoryFile();
                        } else {
                            chatArea.appendText(strFromServer + "\n");
                            if (fileWriter != null) {
                                fileWriter.write(strFromServer + "\n");
                                fileWriter.flush();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initHistoryFile() {
        messageHistoryFile = new File("history_" + login + ".txt");
        int lineScore = 0;
        try {
            fileReader = new FileReader(messageHistoryFile);
            fileWriter = new FileWriter(messageHistoryFile, true);
            if (!messageHistoryFile.exists()) {
                messageHistoryFile.createNewFile();
            } else {
                reversedReader = new ReversedLinesFileReader(messageHistoryFile, null);
                String line = reversedReader.readLine();
                list = new ArrayList<>();
                while (line != null && lineScore <= MAX_MESSAGE_IN_FILE) {
                    list.add(line);
                    line = reversedReader.readLine();
                    lineScore++;
                }
                for (int i = list.size()-1; i >=0 ; i--) {
                    chatArea.appendText(list.get(i)+"\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void addCloseListener() {
        EventHandler<WindowEvent> onCloseRequest = Main.mainStage.getOnCloseRequest();
        Main.mainStage.setOnCloseRequest(event -> {
            closeConnection();
            if (onCloseRequest != null) {
                onCloseRequest.handle(event);
            }
        });

    }

    private void closeConnection() {
        try {
            continueRead = false;
            out.writeUTF("/end");
            socket.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void sendMsg(ActionEvent actionEvent) {
        if (!inputField.getText().trim().isEmpty()) {
            //    chatArea.setText(chatArea.getText()+"Я: "+inputField.getText().trim()+"\n");
            try {
                out.writeUTF(inputField.getText());
                inputField.clear();
                inputField.requestFocus();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка отправки сообщения");
                alert.setHeaderText("Ошибка отправки сообщения");
                alert.setContentText("При отправке сообщения возникла ошибка: " + e.getMessage());
                alert.show();
            }
        }
    }
}
//  if (writingHistoryToFile().exists()){
//          FileReader fileReader=new FileReader(messageHistoryFile);
//          LineNumberReader lineNumberReader=new LineNumberReader(fileReader);
//          while (lineNumberReader.readLine()!=null){
//          lineScore++;
//          }
//          BufferedReader bufferedReader=new BufferedReader(new FileReader(messageHistoryFile));
//          for (int i = 0; i < lineScore; i++) {
//        String line=bufferedReader.readLine();
//        if (i>=lineScore-MAX_MESSAGE_IN_FILE){
//        chatArea.appendText(line+"\n");
//        }
//        }
//        }else {if (login!=null){ writingHistoryToFile().createNewFile();}}
//
//        chatArea.appendText(chatArea.getText()+strFromServer+"\n");
//        if (login!=null){
//
//        FileWriter fileWriter=new FileWriter(writingHistoryToFile(), true);
//        fileWriter.write(chatArea.getText()+strFromServer+"\n");
//        }

//                        if (strFromServer.startsWith("/autok")){
//                            nickname=strFromServer.split(" ")[1];
//
//                        }
