package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class DeleteBoardPopUpCtrl implements Initializable {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private String text;
    private Long id;

    @FXML
    private AnchorPane deletePopUp;

    @FXML
    private Button deleteButton;

    /**
     * Constructs a new DeleteBoardPopUpCtrl instance with
     * the given MainCtrl and ServerUtils objects.
     *
     * @param mainCtrl - the mainCtrl object
     * @param server - the serverUtils object
     */
    @Inject
    public DeleteBoardPopUpCtrl( MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server=server;
    }

    /**
     * Deletes the board associated with the current instance's ID field
     * and redirects the user to the main overview.
     */
    @FXML
    private void delete() {
        server.deleteBoard(id);
        mainCtrl.showMainOverview();
    }

    /**
     * Keeps the board and displays the board overview.
     */
    @FXML
    private void keep() {
        mainCtrl.showBoardOverview(text);
    }

    /**
     * Initializes the Delete Button Pop Up controller.
     *
     * @param url - the location used to resolve relative paths for the root object
     * @param resourceBundle - the resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Sets the text field of the Delete Button Pop Up.
     *
     * @param text - the text to set.
     */
    public void setText(String text) {
        this.text=text;
    }

    /**
     * Sets the ID field of the Delete Button Pop Up.
     *
     * @param id - the ID to set.
     */
    public void setID(Long id){
        this.id = id;
    }
}
