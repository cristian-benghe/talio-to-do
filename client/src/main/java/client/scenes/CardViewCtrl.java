package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
public class CardViewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private Button cardViewBack;

    /**
     * Initialize the controller and the scene
     * @param server server parameter
     * @param mainCtrl mainController parameter to access scenes and methods
     */
    @Inject
    public CardViewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * To get back to the boardOverview
     */
    @FXML
    private void getBackCard()
    {
        mainCtrl.showBoardOverview("", (double) 0, (double) 0, (double) 0);
    }
}
