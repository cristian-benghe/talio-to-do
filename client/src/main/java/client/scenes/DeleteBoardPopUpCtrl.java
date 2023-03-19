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

    @Inject
    public DeleteBoardPopUpCtrl( MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server=server;
    }

    @FXML
    private void Delete() {
        server.deleteBoard(id);
        mainCtrl.showMainOverview();
//        deletePopUp.getScene().getWindow().hide();
//        deletePopUp.getScene().getWindow();
    }

    @FXML
    private void keep() {
        mainCtrl.showBoardOverview(text);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void setText(String text) {
        this.text=text;
    }
    public void setID(Long id){
        this.id=id;
    }
}
