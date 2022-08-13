package com.chatwithme.Controllers;

import com.chatwithme.Thread.ListenerThread;
import com.chatwithme.util.Client;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang.ArrayUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Timer;

import static org.apache.commons.lang.ArrayUtils.*;


public class clientController {

    @FXML
    public JFXTextField msgField;
    public VBox msgBox;
    public ScrollPane scrollPane;
    public AnchorPane mainPane;
    public Pane emojiContainer;

    // TODO : hv to encapsulate client
    Client client;
    String clientName;
    ListenerThread listener;
    Stage stage;

    // file handling
    FileChooser fileChooser;

    // data handling
    byte[] payload;
    byte[] header;

    int mouseCounter = 0;


    public void initialize() throws IOException {

        Platform.runLater(() -> {
           // add a listener for scrollbar to be at the end
            msgBox.heightProperty().addListener(observable -> scrollPane.setVvalue(1D));
            stage.setOnCloseRequest(e -> {
                listener.stop();
            });
        });

        Connect();

    }

    private void Connect() throws IOException {
        client = new Client(clientName,"localhost",8080);

        Timer timer = new Timer();
        fileChooser = new FileChooser();

        // TODO : open up a listener for server
        try {
            listener = new ListenerThread(new DataInputStream(client.getInputStream()),msgBox,timer);
            timer.schedule(listener,0,1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendMsg(String clientName) throws IOException, InterruptedException {

        // TODO : bypass all the empty spaces after and before
        if (!msgField.getText().equals("")){
            String msg = clientName + " :\n" + msgField.getText();
            payload = msg.getBytes(StandardCharsets.UTF_16);
            int len = payload.length;

            header = ByteBuffer.allocate(4).putInt(len).array();
            byte[] frame = addAll(header,payload);

            client.getOut().write(0);
            client.getOut().write(frame);
            client.getOut().flush();

            return true;
        } else return false;


    }


    public void initData(String name, Stage stage) {
        this.stage = stage;
        this.clientName = name;
    }


    // TODO : pass the file type before sending file
    public void uploadPhoto(MouseEvent mouseEvent) throws IOException {

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile!=null) {

            String[] res = selectedFile.getName().split("\\.");

            BufferedImage finalImage = ImageIO.read(selectedFile);

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ImageIO.write(finalImage, res[1], bout);

            payload = bout.toByteArray();
            header = ByteBuffer.allocate(4).putInt(payload.length).array();

            byte[] frame = ArrayUtils.addAll(header,payload);

            client.getOut().write(-1);
            client.getOut().write(frame);

            client.getOut().flush();

        }

    }

    public void sendOnMousePressed(MouseEvent mouseEvent) throws IOException, InterruptedException {
        if(sendMsg(clientName)){
            msgField.clear();
        }
    }

    public void sendOnEnter(KeyEvent keyEvent) throws IOException, InterruptedException {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            if(sendMsg(clientName)){
                msgField.clear();
            }
        }
    }

    public void openUpEmojiMenu(MouseEvent mouseEvent) throws IOException {
        mouseCounter++;
        emojiContainer.setVisible(mouseCounter % 2 == 1);
    }
}
