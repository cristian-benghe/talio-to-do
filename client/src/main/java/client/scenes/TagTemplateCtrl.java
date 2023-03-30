package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;


public class TagTemplateCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private CheckBox checkbox;

    @FXML
    private Button deleteButton;

    @FXML
    private ColorPicker font;

    @FXML
    private ColorPicker highlight;

    @FXML
    private TextField title;



    @FXML
    void setCheckbox() {

    }

    @FXML
    void setFont() {

    }

    @FXML
    void setHighlight() {

    }

    @FXML
    void setTitle() {

    }




    private long tagId;



    /**
     * Initialize the controller and the scene
     * @param server server parameter
     * @param mainCtrl mainController parameter to access scenes and methods
     */
    @Inject
    public TagTemplateCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }


    public void setTagId(long tagID) {
        this.tagId=tagId;
    }
    @FXML
    private void deleteTag() {
        Node tagNode = deleteButton.getParent();
        AnchorPane parent = (AnchorPane) tagNode.getParent();
        int index = parent.getChildren().indexOf(tagNode);
        parent.getChildren().remove(index);
    }
//   public void deleteTag(){
//        server.deleteTag(tagId);
//        mainCtrl.deleteTag
//    }
}