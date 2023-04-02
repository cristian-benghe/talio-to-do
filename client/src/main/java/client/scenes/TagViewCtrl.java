package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TagViewCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Card card;

    @FXML
    private Button createTag;

    @FXML
    private AnchorPane tagList;
    private List<TagTemplateCtrl>tags=new ArrayList<>();


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
        HBox hBox=new HBox();
        for(TagTemplateCtrl tagTemplateCtrl:tags){
            if(tagTemplateCtrl.getCheckBox()==true) {
                AnchorPane anchorPane=new AnchorPane();
                TextField textField = new TextField(tagTemplateCtrl.getText());
                textField.setMaxWidth(Double.MAX_VALUE);
                // set the maximum width of the text field
                textField.setMaxHeight(Double.MAX_VALUE);
                // set the maximum height of the text field
                anchorPane.getChildren().add(textField);
                hBox.getChildren().add(anchorPane);
            }
        }
        mainCtrl.getcardViewCtrl().setCardViewCtrl(hBox);
        mainCtrl.showCardView(card);

    }
    @FXML
    private void createTag() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/client/scenes/TagTemplate.fxml"));
        TagTemplateCtrl controller = new TagTemplateCtrl(server, mainCtrl);
        controller.setCard(mainCtrl.getCard());

        loader.setController(controller);
        Node node = loader.load();
        controller.setConnection(server.getServer());
        //sets connection of every card to the server so you can delete tags
        controller.setBoardId(mainCtrl.getBoardId());
        //sets the board id for deletion of tag from board
        tagList.getChildren().add(node);
        tags.add(controller);
        int index = tagList.getChildren().indexOf(node);
        AnchorPane.setTopAnchor(node, index * 50.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        Tag tag = new Tag("New tag", null);
        server.addTagToBoard(mainCtrl.getBoardId(), tag);
        //add tag to database
        Board board=server.getBoardById(mainCtrl.getBoardId());
        int ind=board.getTags().size()-1;
        //the last tag in the board which was last added
        Long id= board.getTags().get(ind).getTagID();
        //the id of the tag in the database
        controller.setTagId(id);  //set the tagid
    }


    /**
     *
     * @throws IOException
     */
    public void refreshtaglist() throws IOException {
        tagList.getChildren().clear();
        tags.clear();
        //clears the tags displayed in the scene to update them from the database
        for (Tag c : server.getBoardById(mainCtrl.getBoardId()).getTags()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/client/scenes/TagTemplate.fxml"));
            TagTemplateCtrl controller = new TagTemplateCtrl(server, mainCtrl);
            controller.setCard(mainCtrl.getCard());
            loader.setController(controller);
            controller.setTagId(c.getTagID());
            Node node = loader.load();
            controller.setTitleOfTag(c.getTitle()); //set title of the tag from the database
            controller.setConnection(server.getServer());
            controller.setBoardId(mainCtrl.getBoardId());
            tagList.getChildren().add(node);
            tags.add(controller);
            int index = tagList.getChildren().indexOf(node);
            AnchorPane.setTopAnchor(node, index * 50.0);
            AnchorPane.setLeftAnchor(node, 0.0);
        }

    }

    /**
     * @param address of the server
     */
    public void setConnection(String address) {
        server.setServerAddress(address);
    }

    public void setCard(Card card) {
        this.card=card;
    }
}