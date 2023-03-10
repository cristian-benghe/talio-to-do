package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientConnectCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;


    @FXML
    private TextField server_address;  //to get the text from the text field with ID server_address

    @FXML
    private Button go_to_home;

    @Inject
    public ClientConnectCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    //sets the default value for the address on port 8080
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server_address.setText("http://localhost:8080/");
    }

    //

    /** The method changes the scene to the home page (MainOverview). It also first attempts
     * to connect to the server using the user-provided URL.
     */
    public void changeScene() {

        try {

            //First, connect to the server using the provided url.
            this.connect();

        }catch(Exception e){


            //TODO: must add an error message/pop-up indicating an empty URL.
            //TODO: It will be best to rework this section in the future to disable the possibility
            //TODO: of server connection when the URL is empty.


        }finally{

            //Then, show the main overview
            mainCtrl.showMainOverview();

        }

    }


    public void connect() throws Exception{
        String serverAddress = server_address.getText();

        if (serverAddress.isBlank()) {
            throw new Exception();
        }
        // Set the server address in the ServerUtils class
        ServerUtils.setServerAddress(serverAddress);
        //mainCtrl.showMainOverview();
    }

}