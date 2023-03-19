package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

public class ClientConnectCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private RotateTransition shakeAnim; //Rotation animation for the error message.
    private boolean isAnimPlaying; //A flag for whenever the error message animation is playing.

    @FXML
    private TextField server_address;  //to get the text from the text field with ID server_address
//
//    @FXML
//    private Button go_to_home;
    @FXML
    private Button button_connect;

    @FXML
    private Label error_msg; //the error message label for the server address input

    @Inject
    public ClientConnectCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    //sets the default value for the address on port 8080
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        //Refresh the current scene
        refresh();

        //Set up the error message animation

        shakeAnim = new RotateTransition();
        shakeAnim.setNode(error_msg);
        shakeAnim.setInterpolator(Interpolator.LINEAR);
        shakeAnim.setDuration(Duration.millis(75));
        shakeAnim.setAxis(error_msg.getRotationAxis());
        shakeAnim.setByAngle(20);
        shakeAnim.setAutoReverse(true);
        shakeAnim.setCycleCount(6);
        shakeAnim.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {isAnimPlaying = false;}
        });



    }

    /**
     * This method attempts to connect to the server using the user-provided server address.
     * If the user inputs a non-blank address, then the method also changes the scene of the
     * primary stage to the MainOverview scene.
     */


    public boolean isValidUrl(String address){
        try {
            URL url = new URL(address);
            URLConnection conn = url.openConnection();
            conn.connect();
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    public boolean validConnection(String address){
        try{
            server.setServerAddress(address);
            server.getBoards();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public void changeScene() throws Exception{

        String serverAddress = server_address.getText();
        if (serverAddress.isBlank()) {


            //Show the error message
            error_msg.setVisible(true);

            //Play a short rotation animation if the animation is not already playing.
            if(!isAnimPlaying){
                isAnimPlaying = true;
                shakeAnim.play();
            }

            return;
        }

        // Set the server address in the ServerUtils class
        ServerUtils.setServerAddress(serverAddress);
        //Switch the scene to the main overview
        mainCtrl.showMainOverview();
    }

    public void refresh(){

        //Resets the visibility of the error message
        error_msg.setVisible(false);

        //Resets the default input of the serverAddress
        server_address.setText("http://localhost:8080/");


    }

}