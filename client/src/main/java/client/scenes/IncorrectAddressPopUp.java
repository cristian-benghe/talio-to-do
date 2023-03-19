package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class IncorrectAddressPopUp implements Initializable {

    private final MainCtrl mainCtrl ;
    private final ServerUtils server;



    @Inject
    public IncorrectAddressPopUp( MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server=server;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
    public void go_back_to_connect() throws Exception {
        mainCtrl.showClientConnect();
    }
}
