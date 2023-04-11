package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class TagViewCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Card card;

    @FXML
    private Button createTag;

    @FXML
    private AnchorPane tagList;

    @FXML
    private AnchorPane anchorPane;
    private List<TagTemplateCtrl>tags=new ArrayList<>();

    //Help box for the help functionality
    private Dialog helpDialog;


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

    /**
     * Function implemented to use/load certain functions when the TagView scene is shown
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up the dialog for the help button
        helpPopUp();
    }

    /**
     * Set up the dialog for the help button
     */
    public void helpPopUp(){
        helpDialog = new Dialog<String>();
        helpDialog.initModality(Modality.APPLICATION_MODAL);
        helpDialog.setTitle("Help");

        helpDialog.setHeaderText("Help zone");

        Stage dialogStage2 = (Stage) helpDialog.getDialogPane().getScene().getWindow();

        // Create a VBox to hold the keyboard shortcuts list
        VBox shortcutsList = new VBox();
        shortcutsList.setSpacing(5);

        // Add each keyboard shortcut to the VBox
        Label helpPopUpMessage = new Label("Shift+/ -> open help pop-up");
        Label upDownLeftRight = new Label("Up/Down/Left/Right -> select tasks");
        Label shiftUpDown = new Label("Shift+Up/Down -> change order of cards in the column");
        Label editCardTitle = new Label("E -> edit the card title");
        Label deleteCard = new Label("Del/Backspace -> delete a card");
        Label openCardDetails = new Label("Enter -> open card details");
        Label closeCardDetails = new Label("Esc -> close card details");
        Label addTags = new Label("T -> open popup for adding tags");
        Label colorPresetSelection = new Label("C -> open popup for color preset selection");

        // Add the keyboard shortcuts to the VBox
        shortcutsList.getChildren().addAll(helpPopUpMessage, upDownLeftRight,
                shiftUpDown, editCardTitle, deleteCard,
                openCardDetails, closeCardDetails, addTags, colorPresetSelection);

        // Add the VBox to the dialog's content
        helpDialog.getDialogPane().setContent(shortcutsList);

        // Add an OK button to the dialog
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        helpDialog.getDialogPane().getButtonTypes().add(okButtonType);

        // Add a listener to the scene to detect when the Shift+/ key combination is pressed
        anchorPane.setOnKeyPressed(event -> {
            if (event.isShiftDown() && event.getCode() == KeyCode.SLASH) {
                if (!(event.getTarget() instanceof TextField)) {
                    helpDialog.showAndWait();
                }
            }
        });
    }

    /**
     * This method shows the help dialog when the "?" button is clicked
     */
    public void showHelp(){
        Optional<ButtonType> result = helpDialog.showAndWait();

        if (result.get().getButtonData() == ButtonBar.ButtonData.APPLY){
            mainCtrl.showMainOverview();
        }
    }

    /**
     * get to card from tagview
     */
    @FXML
    public void gettoCard()
    {
        HBox hBox=new HBox();
        card=server.deleteTagsFromCard(card.getId());
        for(TagTemplateCtrl tagTemplateCtrl : tags){
            if(tagTemplateCtrl.getCheckBox()) {


                AnchorPane anchorPane=new AnchorPane();
                TextField textField = new TextField(tagTemplateCtrl.getText());
                textField.setEditable(false);
                //textField.setMaxWidth(Double.MAX_VALUE);
                textField.setPrefWidth(100);
                // set the maximum width of the text field
                textField.setMaxHeight(Double.MAX_VALUE);
                // set the maximum height of the text field
                anchorPane.getChildren().add(textField);
                hBox.getChildren().add(anchorPane);

                var tagInstance = server.getTagById(tagTemplateCtrl.getTagId());
                card = server.addTagtoCard(card.getId(), tagInstance);

            }
        }


        mainCtrl.getcardViewCtrl().setCardViewCtrl(hBox);
        mainCtrl.setCard(card);
        mainCtrl.showCardView(card);

    }





    @FXML
    private void addTag() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/client/scenes/TagTemplate.fxml"));
        TagTemplateCtrl controller = new TagTemplateCtrl(server, mainCtrl);
        controller.setCard(mainCtrl.getCard());
        //server.deleteTagsFromCard(card.getId());

        loader.setController(controller);
        Node node = loader.load();
        controller.setConnection(server.getServer());
        //sets connection of every card to the server, so you can delete tags
        controller.setBoardId(mainCtrl.getBoardId());
        //sets the board id for deletion of tag from board
        controller.addTitle();
        tagList.getChildren().add(node);
        tags.add(controller);
        int index = tagList.getChildren().indexOf(node);
        AnchorPane.setTopAnchor(node, index * 50.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        Tag tag = new Tag();
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
            controller.setFontColors(c.getFontRed(), c.getFontBlue(), c.getFontGreen());
            controller.setHighlightColor(c.getHighlightRed(),
                    c.getHighlightBlue(), c.getHighlightGreen());
            controller.addTitle();

            tagList.getChildren().add(node);
            tags.add(controller);
            int index = tagList.getChildren().indexOf(node);
            AnchorPane.setTopAnchor(node, index * 50.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            controller.setTagId(c.getTagID());
            if(card.hasTagWithId(c.getTagID())){
                controller.setCheckBox(true);
            }
            server.deleteTagsFromCard(card.getId());
        }
    }
    /**
     * @param tagTemplateCtrl the tagtemplatectrl deleted
     * @throws IOException geenrated by failed io operations
     */
    public void refreshtaglistDelete(TagTemplateCtrl tagTemplateCtrl) throws IOException {
        List<TagTemplateCtrl> tagsupdate=new ArrayList<>();
        for(int i=0;i<tags.size();i++){
            if(tags.get(i).equals(tagTemplateCtrl)){
                tags.remove(i);
                break;
            }
        }
        for(int i=0;i<tags.size();i++)  {
            tagsupdate.add(tags.get(i));
        }
        tagList.getChildren().clear();
        tags.clear();
        int ind=0;
        for (Tag c : server.getBoardById(mainCtrl.getBoardId()).getTags()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/client/scenes/TagTemplate.fxml"));
            TagTemplateCtrl controller = new TagTemplateCtrl(server, mainCtrl);
            controller.setCard(mainCtrl.getCard());
            loader.setController(controller);
            controller.setTagId(c.getTagID());
            ind++;
            Node node = loader.load();
            if(tagsupdate.get(ind-1).getCheckBox()) {
                controller.setCheckbox(true);
            }
            controller.setTitleOfTag(c.getTitle()); //set title of the tag from the database
            controller.setConnection(server.getServer());
            controller.setBoardId(mainCtrl.getBoardId());
            controller.setFontColors(c.getFontRed(), c.getFontBlue(), c.getFontGreen());
            controller.setHighlightColor(c.getHighlightRed(),
                    c.getHighlightBlue(), c.getHighlightGreen());
            controller.addTitle();

            tagList.getChildren().add(node);
            tags.add(controller);
            AnchorPane.setTopAnchor(node, (ind-1) * 50.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            controller.setTagId(c.getTagID());

        }
        server.deleteTagsFromCard(card.getId());
    }


    /**
     * @param address of the server
     */
    public void setConnection(String address) {
        server.setServerAddress(address);
    }

    /**
     * @param card set card of the tags
     */
    public void setCard(Card card) {
        this.card=card;
    }
}