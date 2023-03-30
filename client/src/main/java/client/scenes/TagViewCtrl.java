package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;



public class TagViewCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Button createTag;

    @FXML
    private AnchorPane tagList;


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
        mainCtrl.showCardView(null);
    }
    @FXML
    private void createTag() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/client/scenes/TagTemplate.fxml"));
        TagTemplateCtrl controller = new TagTemplateCtrl(server, mainCtrl);
        loader.setController(controller);
        Node node = loader.load();
        controller.setConnection(server.getServer());
        controller.setBoardId(mainCtrl.getBoardId());
        tagList.getChildren().add(node);
        int index = tagList.getChildren().indexOf(node);
        AnchorPane.setTopAnchor(node, index * 50.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        Tag tag = new Tag("New tag", null);
        server.addTagToBoard(mainCtrl.getBoardId(), tag);
        Board board=server.getBoardById(mainCtrl.getBoardId());
        int ind=board.getTags().size()-1;
        controller.setTagId(board.getTags().get(ind).getTagID());
    }


    /**
     *
     * @throws IOException
     */
    public void refreshtaglist() throws IOException {
        tagList.getChildren().clear();
        for (Tag c : server.getBoardById(mainCtrl.getBoardId()).getTags()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/client/scenes/TagTemplate.fxml"));
            TagTemplateCtrl controller = new TagTemplateCtrl(server, mainCtrl);
            loader.setController(controller);
            Node node = loader.load();
            tagList.getChildren().add(node);
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
}