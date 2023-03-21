package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private TextField serverAddressField;  //to get the text from the text field with ID serverAddressField
//
//    @FXML
//    private Button go_to_home;

    @FXML
    private Label errorMsg; //the error message label for the server address input

    /**
     * Constructs a new instance of the ClientConnectCtrl class with the specified
     * ServerUtils and MainCtrl objects injected as dependencies.
     * @param server the ServerUtils object to use for interacting with the server
     * @param mainCtrl the MainCtrl object to use for coordinating the application's
     * main control flow
     */
    @Inject
    public ClientConnectCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * sets the default value for the address on port 8080
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        //Refresh the current scene
        refresh();

        //Set up the error message animation

        shakeAnim = new RotateTransition();
        shakeAnim.setNode(errorMsg);
        shakeAnim.setInterpolator(Interpolator.LINEAR);
        shakeAnim.setDuration(Duration.millis(75));
        shakeAnim.setAxis(errorMsg.getRotationAxis());
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

        String serverAddress = serverAddressField.getText();

        //Check that the address is not blank
        if (serverAddress.isBlank()) {

            errorMsg.setText("Please enter a non-blank address above.");
            errorShakeAnim();
            return;

        }

        //Check that the address is in a valid format
        if(!isValidUrl(serverAddress)){

            errorMsg.setText("Please enter an address in a valid format.");
            errorShakeAnim();
            return;

        }

        //Check that a connection with the server can be established
        if(!validConnection(serverAddress)){
            errorMsg.setText("Connection cannot be established. Try again!");
            errorShakeAnim();
            return;
        }

        // Set the server address in the ServerUtils class
        ServerUtils.setServerAddress(serverAddress);
        mainCtrl.create_connection(serverAddress);
        //Switch the scene to the main overview
        mainCtrl.showMainOverview();

    }

    /**
     * Display a shake animation if the input text is wrong
     */
    public void errorShakeAnim(){

        //Show the error message
        errorMsg.setVisible(true);

        //Play a short rotation animation if the animation is not already playing.
        if(!isAnimPlaying){
            isAnimPlaying = true;
            shakeAnim.play();
        }

    }

    /**
     * check if a certain URL is valid
     * @param address the URL to be checked in String format
     * @return true if valid, false else
     */
    public boolean isValidUrl(String address){
        try {
            URL url = new URL(address);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /**
     * checks if a connection to a certain URL is valid
     * @param address the URL in String format
     * @return true if the connection is done with succes,
     * false else.
     */
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

    /**
     * Resets the visibility of the error message and
     * the default input of the serverAddress
     */
    public void refresh(){

        //Resets the visibility of the error message
        errorMsg.setVisible(false);

        //Resets the default input of the serverAddress
        serverAddressField.setText("http://localhost:8080/");


    }

    /**
     * creates the connection and sets the URL
     * @param address the URL that provides where the connection needs to be established
     */
    public void setConnection(String address) {
        server.setServerAddress(address);
    }
}