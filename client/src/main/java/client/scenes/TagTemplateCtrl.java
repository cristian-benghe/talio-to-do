package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class TagTemplateCtrl implements Initializable {
    private final ServerUtils server;

    private final MainCtrl mainCtrl;
    private Long tagId= Long.valueOf(0);
    private Long boardId;
    @FXML
    private CheckBox checkbox;

    @FXML
    private Button deleteButton;

    @FXML
    private ColorPicker font;

    @FXML
    private ColorPicker highlight;

    @FXML
    private TextField titlee;
    private Card card;


    @FXML
    void setFont() {

    }

    @FXML
    void setHighlight() {

    }

    @FXML
    void setTitle() {

    }

    @FXML
    void setCheckbox() {
//        HBox tagList = mainCtrl.getcardViewCtrl().getTagList();
//        System.out.println(title.getText());
//        if (checkbox.isSelected()) {
//            TextField tagTitle = new TextField(title.getText());
//            tagList.getChildren().add(tagTitle);
//        } else {
//            tagList.getChildren().removeIf(node -> node instanceof TextField);
//        }
//        mainCtrl.getcardViewCtrl().setCardViewCtrl(tagList);
    }



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


    /**
     * @param tagID set the id of the tag
     */
    public void setTagId(Long tagID) {
        this.tagId = tagID;
        //this.titlee.setText(server.getTagById(tagID).getTitle());
    }
    @FXML
    private void deleteTag() throws IOException {
        System.out.println(tagId);
        Node tagNode = deleteButton.getParent();
        AnchorPane parent = (AnchorPane) tagNode.getParent();
        int index = parent.getChildren().indexOf(tagNode);
        parent.getChildren().remove(index);    //remove the tag from the scene
        server.deleteTagFromBoard(tagId, boardId);  //remove tag from server
        //first delete tag from board, then from tags(convention)
        server.deleteTag(tagId);
        mainCtrl.getTagViewCtrl().refreshtaglist();
        //when deleting a tag, refresh the list of tags from tagviewctrl
        //so that it sets them in the right position
    }

    /**
     * @param server address
     */
    public void setConnection(String server) {
        this.server.setServerAddress(server);
    }

    /**
     * @param boardId id of the board
     */
    public void setBoardId(Long boardId) {
        this.boardId=boardId;
    }

    /**
     * @return the id of the tag set in the template
     */
    public Long getTagId() {
        return tagId;
    }

    /**
     * @param title of the tag
     */
    public void setTitleOfTag(String title) {
        this.titlee.setText(title);
    }

    /**
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //this.titlee.setText("New Tag");
    }

    public void setCard(Card card) {
        this.card=card;
    }

    public String getText() {
        return titlee.getText();
    }

    public boolean getCheckBox() {
        return checkbox.isSelected();
    }

    public void addTitle() {
        this.titlee.setOnKeyTyped(event -> {
                Tag tag=server.getTagById(tagId);
                tag.setTitle(this.titlee.getText());
                server.updateTagInBoard(Math.toIntExact(tagId), tag, boardId);
                server.updateTagTitle(Math.toIntExact(tagId),this.titlee.getText());
            });    }
//   public void deleteTag(){
//        server.deleteTag(tagId);
//        mainCtrl.deleteTag
//    }
}