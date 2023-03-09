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

    //method to change scene linked to the home page
    public void changeScene() {
        mainCtrl.showMainOverview();
    }


    public void connect() {
        String serverAddress = server_address.getText();

        if (serverAddress.isBlank()) {
            return;
        }
        // Set the server address in the ServerUtils class
        ServerUtils.setServerAddress(serverAddress);
        //mainCtrl.showMainOverview();
    }

}