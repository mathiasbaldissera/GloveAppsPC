package javafx;

import bluetooth.BluetoothConnection;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import uncoupledglovedatathings.MyColors;
import uncoupledprograms.pconly.IReactTestScreen;
import uncoupledprograms.pconly.ReactionTestFunction;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.stage.Stage;

public class ReactionTestController implements Initializable, IReactTestScreen {
    @FXML
    public Label status;
    @FXML
    public CheckBox multicolor;
    @FXML
    JFXTextField mintimeTxt, maxtimeTxt, colorCountTxt;
    @FXML
    VBox container;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*colorCountTxt.setOnKeyReleased(event -> {
            if(Integer.parseInt(colorCountTxt.getText())<3){
                colorCountTxt.setText("3");
            }
            if(Integer.parseInt(colorCountTxt.getText())>6){
                colorCountTxt.setText("6");
            }
        });*/
    }




    @FXML
    public void start(ActionEvent event) {


        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();



        if (btStatus.isConnected() && !ReactionTestFunction.getInstance().isRunning()) {
            try {
                ReactionTestFunction.getInstance().start(this, multicolor.isSelected(),
                        Integer.parseInt(mintimeTxt.getText()), Integer.parseInt(maxtimeTxt.getText()),
                        Integer.parseInt(colorCountTxt.getText()));
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(
                        Alert.AlertType.WARNING
                );
                Text text = new Text(e.getMessage());
                text.setWrappingWidth(300);
                text.setStyle("-fx-padding: 5px; -fx-border-insets: 5px; -fx-background-insets: 5px;");
                alert.getDialogPane().setContent(text);
                alert.setHeaderText("Reaction Test configuration error");
                alert.setTitle("Reaction Test configuration error");
                alert.show();
            }
        } else if (!btStatus.isConnected()) {
            showBluetoothDisconnectedAlert();
        } else {
            System.out.println("q");
        }
    }

    @FXML
    public void stop(ActionEvent event) {
        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
        if (btStatus.isConnected()) {
            ReactionTestFunction.getInstance().stop();
            status.setText("Not Running");
        } else {
            showBluetoothDisconnectedAlert();
        }

    }

    private void showBluetoothDisconnectedAlert() {
        Alert closeConfirmation = new Alert(
                Alert.AlertType.WARNING,
                "You should to connect with the glove first."
        );
        closeConfirmation.setHeaderText("Bluetooth Disconnected");
        closeConfirmation.setTitle("Bluetooth Disconnected");
        closeConfirmation.show();
    }

    @Override
    public void showNotRunningMessage() {
        status.setText("NotRunning");
    }

    @Override
    public void showReadyMessage(String s) {
        status.setText(s);
    }

    @Override
    public void changeColor(MyColors myColors) {
        Color color;
        switch (myColors) {
            case BLUE:
                color = new Color(0, 0, 1, 0.8);
                break;
            case RED:
                color = new Color(1, 0, 0, 0.8);
                break;
            case PINK:
                color = new Color(1, 0, 1, 0.8);
                break;
            case GREEN:
                color = new Color(0, 1, 0, 0.8);
                break;
            case PURPLE:
                color = new Color(0.5, 0, 0.5, 0.8);
                break;
            case YELLOW:
                color = new Color(1, 1, 0, 0.8);
                break;
            case TRANSPARENT:
            default:
                color = new Color(1, 1, 1, 0);


        }
        status.setBackground(new Background(new BackgroundFill(color, new CornerRadii(4), Insets.EMPTY)));
    }

    @Override
    public void showErrorAlert() {
        try {
            playSound(getClass().getClassLoader().getResource("sounds/error.wav"));
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void showSucessAlert() {
        try {
            playSound(getClass().getClassLoader().getResource("sounds/sucess.wav"));
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    // Open audio clip and load samples from the audio input stream.
    private void playSound(URL url) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        AudioInputStream stream = AudioSystem.getAudioInputStream(url);
        AudioFormat format = stream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip clip = (Clip) AudioSystem.getLine(info);
        clip.open(stream);
        clip.start();
        stream.close();

    }

    @Override
    public void showResult(int erros, int countShownColors, ArrayList<Long> timeInMileSeconds) {

        String s = "Total de erros:" + erros
                + "\r\nTotal de cores mostradas: " + countShownColors;
        for (int i = 0; i < timeInMileSeconds.size(); i++) {
            if (timeInMileSeconds.get(i) == 10_000_000_000_000L) {
                s += ("\r\n  Tempo " + (i + 1) + ": Não respondeu");
            } else {
                s += ("\r\n  Tempo " + (i + 1) + ": " + timeInMileSeconds.get(i) + "ms");
            }
        }
        status.setText(s);
        changeColor(MyColors.TRANSPARENT);
    }

    @Override
    public void showResult(int timeInMileSeconds) {
        status.setText("Tempo de reação: " + timeInMileSeconds + "ms");
        changeColor(MyColors.TRANSPARENT);
    }
}
