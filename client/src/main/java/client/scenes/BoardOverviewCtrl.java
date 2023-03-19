package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * gets the value of the id
     *
     * @return the value of the id
     */
    public Long getId() {
        return id;
    }

    //id of the board
    private Long id= Long.valueOf(-1);
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane board;

    @FXML
    private TextField board_title;

    @FXML
    private HBox hbox;

    @FXML
    private Text keyID;

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void showOverview() {
        mainCtrl.showBoardOverview("");
    }
    //the title is not yet stored correctly in the database
    public void setBoard_title(String idd){
        Long nr=Long.parseLong(idd.split("--")[1].trim());
        keyID.setText("keyID: "+nr);
        this.id=nr;
        if(!idd.contains("New Board")) {
            board_title.setText(idd.split("--")[0].trim());
            return;
        }
        //this should set a default title for boards that are not new but haven't been modified either
        //or set the title to the title of the board object with an ID
        Board board=server.getBoardById(nr);
        if(board.getTitle().equals("New Board")){
            board_title.setText("Board "+nr);
        }
        else {
            board_title.setText(server.getBoardById(nr).getTitle());
        }

    }
    //when you edit text it should update the title
    public void edit_title(){
        server.updateBoardTitle(this.id, board_title.getText());
    }
    //when you click on home button you go back to mainOverview
    public void go_back_home(){
        mainCtrl.showMainOverview();
    }
    public void addColumn(){
        addOneColumn("New column");
    }

    /**
     * Add a new column. All the set methods that are used to initialize the elements properly
     * Note: creation made without JavaFx ID's.
     * @param title title of the column that will be created...
     */
    public void addOneColumn(String title){
        AnchorPane anchorPaneVBox=new AnchorPane();
        ScrollPane scrollPane=new ScrollPane();
        VBox vBox=new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        scrollPane.setContent(vBox);
        vBox.setPrefHeight(380);
        vBox.setPrefWidth(150);
        Button button = createButton(vBox);
        setVBoxDragDrop(button, vBox);
        TextField textField = new TextField(title);
        textField.setAlignment(Pos.CENTER);
        Label columnLabel = new Label("...");
        vBox.setMargin(textField,new Insets(2));
        button.setAlignment(Pos.BOTTOM_CENTER);
        vBox.getChildren().addAll(columnLabel, textField, button);
        anchorPaneVBox.getChildren().add(vBox);
        setColumnDragDrop(anchorPaneVBox);  //to set the functionality of the drag and drop of the new column. Only for deletion not to replace!
        hbox.getChildren().add(anchorPaneVBox);

    }

    /**
     * A method to add a new card
     * @param vBox vBox that the card will be added
     * @return an anchorPane as a card
     */
    public AnchorPane addCard(VBox vBox)
    {
        AnchorPane anchorPane1 = createCard();
        setCardDragDrop(anchorPane1,vBox);
        vBox.setMargin(anchorPane1, new Insets(2,2,2,2));
        return anchorPane1;
    }

    /**
     * A method to create a new card
     * @return return an anchorPane as a card!
     */
    public AnchorPane createCard()
    {
        AnchorPane anchorPane1 = new AnchorPane();
        HBox hbox1 = new HBox();
        hbox1.setAlignment(Pos.CENTER);
        hbox1.setPrefSize(150,80);
        TextField textField = new TextField("Card");
        textField.setStyle("-fx-background-color: #C0C0C0");
        textField.setAlignment(Pos.BASELINE_CENTER);
        hbox1.getChildren().add(textField);
        anchorPane1.getChildren().add(hbox1);
        textField.setFont(new Font("System",18));
        textField.setOnMouseClicked(event -> {

        });
        anchorPane1.setStyle("-fx-background-color:  #C0C0C0; -fx-background-radius:  15");
        anchorPane1.setPrefSize(150,80);
        return anchorPane1;
    }

    /**
     * A method to create a button
     * @param vBox the vBox that the element is created in
     * @return new created button
     */
    public Button createButton(VBox vBox)
    {
        Button button = new Button("AddCard");
        setButtonAction(button, vBox);
        vBox.setMargin(button,new Insets(5,0,0,0));
        return  button;
    }

    /**
     * A method to set an action of the button
     * @param button a button to be arranged
     * @param vBox Vbox that contains the button
     */
    public void setButtonAction(Button button, VBox vBox)
    {
        button.setOnAction(event -> {
            AnchorPane anchorPane1 = addCard(vBox);
            vBox.getChildren().remove(button);
            vBox.getChildren().add(anchorPane1);
            vBox.getChildren().add(button);
        });
    }


    /**
     * This method sets the needed properties of the deletion of the columns.
     * @param column is column to set property of deletion
     */
    private void setColumnDragDrop(AnchorPane column) {

        //set the drag of the specific column
        column.setOnDragDetected(event -> {
            Dragboard dragboard = column.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString("DeletionColumn");
            dragboard.setContent(clipboardContent);
            event.consume();
            columnBin();
        });
    }

    /**
     * A function to replace/switch the card between the columns
     * @param button button parameters mostly passed because of deletion/recreation to always align at the bottom. I did a lot of research it was the most appropriate
     * @param myVBox vBox that will be effected
     */
    private void setVBoxDragDrop(Button button, VBox myVBox)
    {

        myVBox.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        myVBox.setOnDragDropped(event -> {
            myVBox.getChildren().remove(button);
            setCardDragDrop((AnchorPane) event.getGestureSource(),myVBox);
            myVBox.getChildren().add((AnchorPane) event.getGestureSource()); //gesture source to pass dragged item
            myVBox.getChildren().add(button);
            event.setDropCompleted(true);
            event.consume();
        });
    }

    /**
     * set drag and drop functionality into card
     * @param card a card to add functionality
     * @param vBox a parent element of the card which is vBox
     */
    private void setCardDragDrop(AnchorPane card, VBox vBox) {

        //set the drag of the specific column
        card.setOnDragDetected(event -> {
            Dragboard dragboard = card.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString("DeletionCard");
            dragboard.setContent(clipboardContent);
            event.consume();
            cardBin(vBox);
        });
    }

    /**
     * set the BIN according to column deletion to avoid gesture/drag and drop conflicts
     */
    private void columnBin()
    {
        //set the BIN text (get(2) because BIN is second indexed element in the anchorPane. This can be done byID later on)...
        anchorPane.getChildren().get(2).setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        //deletion of the dragged item
        anchorPane.getChildren().get(2).setOnDragDropped(event -> {
            hbox.getChildren().remove(event.getGestureSource()); //gesture source to pass dragged item
            event.setDropCompleted(true);
            event.consume();
        });
    }

    /**
     * set the BIN according to card deletion to avoid gesture/drag and drop conflicts
     */
    private void  cardBin(VBox vBox)
    {
        anchorPane.getChildren().get(2).setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        //deletion of the dragged item
        anchorPane.getChildren().get(2).setOnDragDropped(event -> {
            vBox.getChildren().remove(event.getGestureSource()); //gesture source to pass dragged item
            event.setDropCompleted(true);
            event.consume();
        });
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addOneColumn("To do");
        addOneColumn("Doing");
        addOneColumn("Done");
    }

    /**
     * This method deletes the board with the current id and then changes the scene to the DeleteBoardPopUp
     */
    public void deleteBoard(){
        mainCtrl.showDeleteBoardPopUp(board_title.getText(), id);
    }
}
