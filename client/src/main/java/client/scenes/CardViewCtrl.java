package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class CardViewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private String text;
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
        System.out.println(text);
        mainCtrl.showBoardOverview(text, (double) 1, (double) 1, (double) 1);
    }

    /**
     * @param text taken from the board overview and set back when returning to the board
     */
    public void setText(String text){
        this.text=text;
    }

    @FXML
    private void getTagView() throws IOException {
        mainCtrl.showTagView();
    }

    /**
     * @param address of the server
     */
    public void setConnection(String address) {
        server.setServerAddress(address);
    }
}
