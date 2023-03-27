package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;


public class TagViewCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;


    /**
     * Initialize the controller and the scene
     * @param server server parameter
     * @param mainCtrl mainController parameter to access scenes and methods
     */
    @Inject
    public TagViewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    private void gettoCard()
    {
        mainCtrl.showCardView();
    }
}