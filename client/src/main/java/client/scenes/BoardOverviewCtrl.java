package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.Column;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;


import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {
    private Long nrCol = Long.valueOf(0);
    private String title;

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

    //Dialog box for the delete board button
    private Dialog deleteBoardDialog;
    //id of the board
    private Long id = (long) -1;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane board;

    @FXML
    private TextField boardTitle;

    @FXML
    private HBox hbox;

    @FXML
    private Text keyID;
    @FXML
    private ImageView binImage;


    //Scale Transition for BinImage contraction and expansion
    private ScaleTransition binContraction;
    private ScaleTransition binExpansion;

    /**
     * Handles the initialization and interaction with the elements of the BoardOverview.fxml
     *
     * @param server   - the server of the application
     * @param mainCtrl - the main controller
     */
    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * This changes the scene to the Board Overview
     */
    public void showOverview() {
        mainCtrl.showBoardOverview("", (double) 255, (double) 255, (double) 255);
    }


    /**
     * @param idd the id of the board
     * @param blue value of blue in rgb
     * @param green value of green in rgb
     * @param red value of red in rgb
     */
    public void setBoardTitle(String idd, Double blue, Double green, Double red) {
        this.title=idd;
        Long nr = Long.parseLong(idd.split("--")[1].trim());
        keyID.setText("keyID: " + nr);
        this.id = nr;
        columnsRefresh();
        setColors(blue, green, red);
//        addOneColumn("To do");
//        addOneColumn("Doing");
//        addOneColumn("Done");
        if (!idd.contains("New Board")) {
            boardTitle.setText(idd.split("--")[0].trim());
            return;
        }
        //this should set a default title for boards that are not new
        // but haven't been modified either
        //or set the title to the title of the board object with an ID
        Board board = server.getBoardById(nr);
        if (board.getTitle().equals("New Board")) {
            boardTitle.setText("Board " + nr);
        } else {
            boardTitle.setText(server.getBoardById(nr).getTitle());
        }

    }

    /**
     * This method should update the title when you edit it
     */
    public void editTitle() {

        //server.updateBoardTitle(this.id, boardTitle.getText());

        Board board = server.getBoardById(this.id);
        board.setTitle(boardTitle.getText());
        System.out.println(board.toStringShort());
        //server.send("/app/delete-board", board.getId());
        server.send("/app/update-board", board);
        server.send("/app/update-title-in-board", board);
    }

    /**
     * This changes the scene to the Main Overview
     */

    public void goBackHome() {
        mainCtrl.showMainOverview();
    }

    /**
     * This method adds a new default column
     */
    public void addColumn() {
        addOneColumn("New column");
    }

    /**
     * Add a new column. All the set methods that are used to initialize the elements properly
     * Note: creation made without JavaFx ID's.
     *
     * @param title title of the column that will be created...
     */
    public void addOneColumn(String title) {
        AnchorPane anchorPaneVBox = new AnchorPane();
        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        scrollPane.setContent(vBox);
        vBox.setPrefHeight(380);
        vBox.setPrefWidth(150);

        TextField textField = new TextField(title);
        textField.setAlignment(Pos.CENTER);
        Label columnLabel = new Label("...");
        vBox.setMargin(textField, new Insets(2));

        anchorPaneVBox.getChildren().add(vBox);
        //to set the functionality of the drag and drop of the new column.
        // Only for deletion not to replace!
        setColumnDragDrop(anchorPaneVBox);
        hbox.getChildren().add(anchorPaneVBox);
        nrCol++;
        server.addColumnToBoard(id, new Column(("New column" + nrCol),
                new ArrayList<>()), hbox.getChildren().indexOf(anchorPaneVBox) + 1);
        textField.setOnKeyTyped(e ->
        {
            updateColTitle(hbox.getChildren().indexOf(anchorPaneVBox) + 1, textField.getText());
        });

        Button button = createButton(vBox, (long) hbox.getChildren().indexOf(anchorPaneVBox) + 1);
        setVBoxDragDrop(button, vBox);
        button.setAlignment(Pos.BOTTOM_CENTER);
        vBox.getChildren().addAll(columnLabel, textField, button);
        server.send("/app/update-in-board", server.getBoardById(id));
    }

    private void updateColTitle(int i, String text) {
        server.updateColTitle(i, text, id);
        server.send("/app/update-labels-in-board", server.getBoardById(id));
    }

    /**
     * A method to add a new card
     *
     * @param vBox vBox that the card will be added
     * @return an anchorPane as a card
     */
    public AnchorPane addCard(VBox vBox) {
        AnchorPane anchorPane1 = createCard();

        setCardDragDrop(anchorPane1, vBox);
        vBox.setMargin(anchorPane1, new Insets(2, 2, 2, 2));
        return anchorPane1;
    }

    /**
     * A method to create a new card
     *
     * @return return an anchorPane as a card!
     */
    public AnchorPane createCard() {
        AnchorPane anchorPane1 = new AnchorPane();
        Label myLabel = new Label();
        myLabel.setText("=====");
        setLabelAction(myLabel);
        VBox vbox = new VBox();
        anchorPane1.getChildren().add(myLabel);
        HBox hbox1 = new HBox();
        hbox1.setAlignment(Pos.CENTER);
        hbox1.setPrefSize(150, 80);
        TextField textField = new TextField("Card");
        textField.setStyle("-fx-background-color: #C0C0C0");
        textField.setAlignment(Pos.BASELINE_CENTER);
        hbox1.getChildren().add(textField);
        vbox.getChildren().addAll(myLabel, hbox1);
        vbox.setAlignment(Pos.CENTER);
        anchorPane1.getChildren().add(vbox);
        textField.setFont(new Font("System", 18));

        anchorPane1.setStyle("-fx-background-color:  #C0C0C0; -fx-background-radius:  15");
        anchorPane1.setPrefSize(150, 80);
        return anchorPane1;
    }

    /**
     * A method to create a button
     *
     * @param vBox     the vBox that the element is created in
     * @param columnid
     * @return new created button
     */
    public Button createButton(VBox vBox, Long columnid) {
        Button button = new Button("AddCard");
        setButtonAction(button, vBox, columnid);
        vBox.setMargin(button, new Insets(5, 0, 0, 0));
        return button;
    }

    /**
     * A method to set an action of the button
     *
     * @param button   a button to be arranged
     * @param vBox     Vbox that contains the button
     * @param columnid column id
     */
    public void setButtonAction(Button button, VBox vBox, Long columnid) {

        button.setOnAction(event -> {
            AnchorPane anchorPane1 = addCard(vBox);
            Card mycard = new Card("Card", null, null, null);
            server.addCardToColumn(id, columnid, mycard,
                    (long) vBox.getChildren().indexOf(button) - 2);
            ((TextField) ((HBox) ((VBox) anchorPane1.getChildren().get(0)).
                    getChildren().get(1)).getChildren().get(0)).setOnKeyTyped(event1 -> {
                        server.updateCardTitle((long) vBox
                                .getChildren().indexOf(anchorPane1) - 1,
                            columnid, ((TextField) ((HBox) ((VBox) anchorPane1
                                .getChildren().get(0)).
                                getChildren().get(1)).getChildren().get(0)).getText(), id);
                        server.send("/app/update-labels-in-board", server.getBoardById(id));
                    });
            vBox.getChildren().remove(button);
            vBox.getChildren().add(anchorPane1);
            vBox.getChildren().add(button);
            server.send("/app/update-in-board", server.getBoardById(id));
        });
    }

    /**
     * A method that is needed tu update the name of the card.
     *
     * @param anchorPane1 Card, namely
     * @param button      button to refresh
     * @param vBox        card container
     * @param columnid    id of the specific column
     */
    public void setTextField(AnchorPane anchorPane1, Button button, VBox vBox, Long columnid) {
        ((TextField) ((HBox) ((VBox) anchorPane1.getChildren().get(0)).getChildren().
                get(1)).getChildren().get(0)).setOnKeyTyped(event1 -> {
                    server.updateCardTitle((long) vBox.getChildren().
                            indexOf(anchorPane1) - 1, columnid,
                        ((TextField) ((HBox) ((VBox) anchorPane1
                            .getChildren().get(0)).getChildren().get(1)).
                            getChildren().get(0)).getText(), id);
                    server.send("/app/update-in-board", server.getBoardById(id));
                });
    }

    /**
     * A method the set the label mouseclick
     *
     * @param label a label to get the property
     */
    public void setLabelAction(Label label) {
        label.setOnMouseClicked(event -> {
            mainCtrl.showCardView();
        });
    }

    /**
     * This method sets the needed properties of the deletion of the columns.
     *
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
     *
     * @param button button parameters mostly passed because of deletion/recreation
     *               to always align at the bottom.
     *               I did a lot of research it was the most appropriate
     * @param myVBox vBox that will be effected
     */
    private void setVBoxDragDrop(Button button, VBox myVBox) {
        // Bug fix of disappearing of the addCard button because of duplication error
        myVBox.setOnDragOver(event -> {
            //To solve the issue of drag and drop of the column into column
            if (Objects.equals(event.getDragboard().getString(), "DeletionCard") &&
                    !(((AnchorPane) event.getGestureSource()).getParent().equals(myVBox))) {
                if (event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            }
        });

        // Bug fix of disappearing of the addCard button because of duplication error
        myVBox.setOnDragDropped(event -> {
            //To solve the issue of drag and drop of the column into column
            if (Objects.equals(event.getDragboard().getString(), "DeletionCard") &&
                    !(((AnchorPane) event.getGestureSource()).getParent().equals(myVBox))) {
                myVBox.getChildren().remove(button);
                setCardDragDrop((AnchorPane) event.getGestureSource(), myVBox);
                server.cardDragDropUpdate(
                        Long.valueOf(((AnchorPane) event.getGestureSource()).getParent().
                                getChildrenUnmodifiable().indexOf((AnchorPane)event.
                                        getGestureSource())),
                        (long) hbox.getChildren().indexOf(((AnchorPane) event.getGestureSource()).
                                getParent().getParent()),
                        (long) hbox.getChildren().indexOf(myVBox.getParent()), id);
                server.send("/app/update-in-board", server.getBoardById(id));
                //gesture source to pass dragged item
                myVBox.getChildren().add((AnchorPane) event.getGestureSource());
                myVBox.getChildren().add(button);
                event.setDropCompleted(true);
                event.consume();
                server.send("app/update-in-board",server.getBoardById(id));
            }
        });
    }

    /**
     * set drag and drop functionality into card
     *
     * @param card a carTd to add functionality
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
    private void columnBin() {
        //set the BIN text (get(2) because BIN is second indexed element in the anchorPane.
        // TODO This can be done byID later on)...
        anchorPane.getChildren().get(3).setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        //deletion of the dragged item
        anchorPane.getChildren().get(3).setOnDragDropped(event -> {
            //gesture source to pass dragged item
            int colInd = hbox.getChildren().indexOf(event.getGestureSource());
            //server.deleteColumn(colInd, id);
            server.deleteColumnFromApi(Math.toIntExact(server.deleteColumn(colInd,id)));
            hbox.getChildren().remove(event.getGestureSource());
            event.setDropCompleted(true);
            event.consume();
            server.send("/app/update-in-board", server.getBoardById(id));
        });
    }

    /**
     * set the BIN according to card deletion to avoid gesture/drag and drop conflicts
     */
    private void cardBin(VBox vBox) {
        anchorPane.getChildren().get(3).setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        //deletion of the dragged item
        anchorPane.getChildren().get(3).setOnDragDropped(event -> {
            // gesture source to pass dragged item

            Long cardId = server.deleteCardServer(server.getBoardById(id),
                    Long.valueOf(((AnchorPane) event.getGestureSource()).getParent().
                            getChildrenUnmodifiable().
                            indexOf((AnchorPane)event.getGestureSource())),
                    (long) hbox.getChildren().
                            indexOf(((AnchorPane) event.getGestureSource()).
                                    getParent().getParent())+1,id);
            server.send("/app/update-in-board", server.getBoardById(id));
            System.out.println(server.deleteCardFromCardApi(cardId));

            vBox.getChildren().remove(event.getGestureSource());
            event.setDropCompleted(true);
            event.consume();
        });
    }

    /**
     * refreshed the boardOverview scene so that it updates the columns
     */
    public void columnsRefresh() {
        hbox.getChildren().clear();
        for (Column c : server.getBoardById(id).getColumns()) {
            AnchorPane anchorPaneVBox = new AnchorPane();
            ScrollPane scrollPane = new ScrollPane();
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.TOP_CENTER);
            scrollPane.setContent(vBox);
            vBox.setPrefHeight(380);
            vBox.setPrefWidth(150);

            TextField textField = new TextField(c.getTitle());
            textField.setAlignment(Pos.CENTER);
            Label columnLabel = new Label("...");
            vBox.setMargin(textField, new Insets(2));

            anchorPaneVBox.getChildren().add(vBox);
            setColumnDragDrop(anchorPaneVBox);
            hbox.getChildren().add(anchorPaneVBox);
            textField.setOnKeyTyped(e ->
            {
                updateColTitle(hbox.getChildren().indexOf(anchorPaneVBox) + 1, textField.getText());
            });
            Button button =
                    createButton(vBox, (long) hbox.getChildren().indexOf(anchorPaneVBox) + 1);
            setVBoxDragDrop(button, vBox);
            button.setAlignment(Pos.BOTTOM_CENTER);
            vBox.getChildren().addAll(columnLabel, textField);
            for (Card kard : c.getCards()) {
                vBox.getChildren().add(button);
                AnchorPane anchorPane1 = addCard(vBox);
                ((TextField) ((HBox) ((VBox) anchorPane1.getChildren().get(0)).
                        getChildren().get(1)).getChildren().get(0)).setText(kard.getTitle());
                setTextField(anchorPane1, button, vBox,
                        (long) hbox.getChildren().indexOf(anchorPaneVBox) + 1);
                vBox.getChildren().add(anchorPane1);

                vBox.getChildren().remove(button);

            }
            vBox.getChildren().add(button);
        }
    }

    /**
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set the expansion and contraction animations
        binExpansion = new ScaleTransition();
        binExpansion.setDuration(Duration.millis(800));
        binExpansion.setNode(binImage);
        binExpansion.setInterpolator(Interpolator.EASE_IN);

        binExpansion.setToX(2);
        binExpansion.setToY(2);


        binContraction = new ScaleTransition();
        binContraction.setDuration(Duration.millis(800));
        binContraction.setNode(binImage);
        binContraction.setInterpolator(Interpolator.EASE_IN);

        binContraction.setToX(1);
        binContraction.setToY(1);

        binImage.setImage(new Image("BinImage.png"));


        //Set up the dialog box for the delete board button
        deleteBoardDialog = new Dialog<String>();
        deleteBoardDialog.initModality(Modality.APPLICATION_MODAL);
        deleteBoardDialog.setTitle("Are you sure you want to delete the board");

        deleteBoardDialog.setHeaderText("Are you sure you want to to delete the board?");
        deleteBoardDialog.setContentText("This action is irreversible.");

        Stage dialogStage = (Stage) deleteBoardDialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image("BinImage.png"));

        ButtonType confirmBT = new ButtonType("Delete", ButtonBar.ButtonData.APPLY);
        ButtonType cancelBT = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        deleteBoardDialog.getDialogPane().getButtonTypes().addAll(cancelBT, confirmBT);


    }

    /**
     * This method is part of the "drag and drop" animation of deleting cards and columns.
     * It increases the size of the bin.
     */
    public void expandBin() {
        binContraction.stop();
        binExpansion.stop();
        binExpansion.play();
    }

    /**
     * This method is part of the "drag and drop" animation of deleting cards and columns.
     * It decreases the size of the bin.
     */
    public void contractBin() {
        binContraction.stop();
        binExpansion.stop();
        binContraction.play();
    }

    /**
     * This method deletes the board with the current id and then changes
     * the scene to the DeleteBoardPopUp
     */
    public void deleteBoard() {

        //Show the board
        Optional<ButtonType> result = deleteBoardDialog.showAndWait();

        //Check whether the user confirmed the delete operation
        if (result.get().getButtonData() == ButtonBar.ButtonData.APPLY) {

            //server.deleteBoard(id);

            server.send("/app/delete-board", id);
            mainCtrl.showMainOverview();

        }
    }

    /**
     * A method that sets the connection to a server
     *
     * @param address the address of the server\
     */
    public void setConnection(String address) {
        server.setServerAddress(address);
    }

    /**
     * method which copies the id of the accessed board to the clipboard
     */
    public void copyID() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(String.valueOf(id));
        clipboard.setContent(content);
    }

    /**
     * changes scene to board customization
     */
    public void goToSettings(){
        mainCtrl.showBoardCustomization();
    }

    /**
     * @param blue the rgb value of blue set from the database
     * @param green the rgb value of green set from the database
     * @param red the rgb value of red set from the database
     */
    public void setColors(Double blue, Double green, Double red) {
        Board board=server.getBoardById(id);
        Color color = Color.color(board.getRed(), board.getGreen(), board.getBlue());

        // Set the background color of the AnchorPane to the RGB color value
        anchorPane.setStyle("-fx-background-color: " + toRgbCode(color) + ";");
    }

    /**
     * @param color conversion from rfb
     * @return the rgb code
     */
    private String toRgbCode(Color color) {
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);
        return String.format("#%02X%02X%02X", r, g, b);
    }

    /**
     * @return title of the board as it appears in the main overview
     */
    public String getTitle() {
        return title;
    }

    /**
     * Method to be called one time for the websockets to
     * start. (when boardOverview is shown)
     */
    public void socketsCall() {
        server.registerForMessages("/topic/update-in-board", Board.class, board -> {
            if (Objects.equals(board.getId(), id))
                Platform.runLater(() -> columnsRefresh());
//            Platform.runLater(() ->
//                 setBoardTitle(boardTitle.getText() + " -- " + board.getId().toString()));
            Platform.runLater(() -> setColors(board.getBlue(), board.getGreen(), board.getRed()));
        });
        server.registerForMessages("/topic/update-title-in-board", Board.class, board -> {
            if (Objects.equals(board.getId(), id)){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> refreshTitle());
            }
//            Platform.runLater(() ->
//                 setBoardTitle(boardTitle.getText() + " -- " + board.getId().toString()));
            Platform.runLater(() -> setColors(board.getBlue(), board.getGreen(), board.getRed()));
        });

        server.registerForMessages("/topic/update-labels-in-board", Board.class, board -> {
            System.out.println("fortza steaua");
            if (Objects.equals(board.getId(), id)){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> columnsRefresh());
            }
//            Platform.runLater(() ->
//                 setBoardTitle(boardTitle.getText() + " -- " + board.getId().toString()));
            Platform.runLater(() -> setColors(board.getBlue(), board.getGreen(), board.getRed()));
        });
    }

    private void refreshTitle() {
        Board board1 = server.getBoardById(id);
        boardTitle.setText(board1.getTitle());
    }

}
