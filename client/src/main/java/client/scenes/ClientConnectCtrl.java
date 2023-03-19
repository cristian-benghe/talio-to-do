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

import java.net.MalformedURLException;
import java.net.URL;
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
    public void connect(){

        String serverAddress = server_address.getText();

        //Check that the address is not blank
        if (serverAddress.isBlank()) {

            error_msg.setText("Please enter a non-blank address above.");
            errorShakeAnim();
            return;

        }

        //Check that the address is in a valid format
        if(!isValidUrl(serverAddress)){

            error_msg.setText("Please enter an address in a valid format.");
            errorShakeAnim();
            return;

        }

        //Check that a connection with the server can be established
        if(!validConnection(serverAddress)){
            error_msg.setText("Connection cannot be established. Try again!");
            errorShakeAnim();
            return;
        }

        // Set the server address in the ServerUtils class
        ServerUtils.setServerAddress(serverAddress);
        //Switch the scene to the main overview
        mainCtrl.showMainOverview();

    }
    public void errorShakeAnim(){

        //Show the error message
        error_msg.setVisible(true);

        //Play a short rotation animation if the animation is not already playing.
        if(!isAnimPlaying){
            isAnimPlaying = true;
            shakeAnim.play();
        }

    }

    public boolean isValidUrl(String address){
        try {
            URL url = new URL(address);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
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


    public void refresh(){

        //Resets the visibility of the error message
        error_msg.setVisible(false);

        //Resets the default input of the serverAddress
        server_address.setText("http://localhost:8080/");


    }

}