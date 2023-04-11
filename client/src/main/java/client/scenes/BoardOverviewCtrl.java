package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.Column;
import commons.Tag;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;


import java.io.IOException;
import java.net.URL;
import java.util.*;

import static java.lang.Long.parseLong;

public class BoardOverviewCtrl implements Initializable {
    private Long nrCol = 0L;
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

    //Help box for the help functionality
    private Dialog helpDialog;

    private Dialog cardCustomization;
    //id of the board
    private Long id = (long) -1;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label boardTitleLabel;

    @FXML
    private TextField boardTitle;

    @FXML
    private HBox hbox;

    @FXML
    private ScrollPane scrollPaneBoard;
    @FXML
    private Text keyID;
    @FXML
    private ImageView binImage;

    @FXML
    private Label copyLabel;
    @FXML
    private AnchorPane board;

    private AnchorPane selectedAnchorPane;

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
     * @param idd   the id of the board
     * @param blue  value of blue in rgb
     * @param green value of green in rgb
     * @param red   value of red in rgb
     */
    public void setBoardTitle(String idd, Double blue, Double green, Double red) {
        boardTitleLabel.setVisible(false);
        this.title = idd;
        long nr = parseLong(idd.split("--")[1].trim());
        keyID.setText("keyID: " + nr);
        this.id = nr;
        columnsRefresh();
        setColors(blue, green, red);
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
    public void editTitleEnter() {
        boardTitle.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                boardTitleLabel.setVisible(false);

                Board board = server.getBoardById(this.id);
                board.setTitle(boardTitle.getText());
                server.send("/app/update-board", board);
                server.send("/app/update-title-in-board", board);
            } else {
                boardTitleLabel.setVisible(true);
            }
        });
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
    //TODO: ADD column rearrangement functionality to the hbox!

    /**
     * It'll be done later on
     *
     * @param myhbox hbox to be changed
     */
    public void setHBoxDrop(HBox myhbox) {
        myhbox.setOnDragOver(event -> {
            if (hbox.getChildren().contains(event.getGestureSource())) {
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            }
        });

        myhbox.setOnDragDropped(event -> {
            Board board1 = server.getBoardById(id);
            int columnSize = board1.getColumns().size();
            if (event.getX() > columnSize * 150) {
                rightColumnDrop((VBox) ((AnchorPane)
                        hbox.getChildren().get(columnSize - 1)).getChildren().get(0), event);
                event.setDropCompleted(true);
                event.consume();
                columnsRefresh();
            }
            server.send("/topic/update-in-board", server.getBoardById(id));
        });
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
        VBox.setMargin(textField, new Insets(2));

        anchorPaneVBox.getChildren().add(vBox);
        //to set the functionality of the drag and drop of the new column.
        // Only for deletion not to replace!
        setColumnDragDrop(anchorPaneVBox);
        hbox.getChildren().add(anchorPaneVBox);
        nrCol++;
        Column column = new Column(("New column" + nrCol),
                new ArrayList<>());
        Board board = server.getBoardById(mainCtrl.getBoardId());
        column.updateColors(board.getColumnRed(),
                board.getColumnGreen(),
                board.getColumnBlue());
        server.addColumnToBoard(id, column, hbox.getChildren().indexOf(anchorPaneVBox) + 1);
        textField.setOnKeyPressed(e ->
        {
            columnLabel.setText("Press enter to save!!");
            if (e.getCode() == KeyCode.ENTER) {
                updateColTitle(hbox.getChildren().indexOf(anchorPaneVBox) + 1, textField.getText());
                server.send("/app/update-labels-in-board", server.getBoardById(id));
                columnLabel.setText("...");
            }
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
        AnchorPane anchorPane1 = createCard(vBox);

        setCardDragDrop(anchorPane1, vBox);
        vBox.setMargin(anchorPane1, new Insets(2, 2, 2, 2));

        anchorPane1.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)
            {
                labelActionGeneral(((Label) (((VBox) anchorPane1.getChildren().get(0))
                        .getChildren().get(0))));
            }

            if (event.getButton() == MouseButton.PRIMARY) {
                if (selectedAnchorPane != null && selectedAnchorPane != anchorPane1) {
                    // reset the previously selected anchor pane
                    resetAnchorPane(selectedAnchorPane);
                }
                if (anchorPane1 != selectedAnchorPane) {
                    // set the new selected anchor pane
                    selectAnchorPane(anchorPane1);
                } else {
                    // deselect the current anchor pane
                    resetAnchorPane(anchorPane1);
                }
            }
        });

        return anchorPane1;
    }


    /**
     * A method to create a new card
     *
     * @param vBox - the column with cards
     * @return return an anchorPane as a card!
     */
    public AnchorPane createCard(VBox vBox) {
        AnchorPane anchorPane1 = new AnchorPane();
        Label myLabel = new Label();
        myLabel.setText("...");
        setLabelAction(myLabel);
        VBox vbox = new VBox();
        anchorPane1.getChildren().add(myLabel);
        HBox hbox1 = new HBox();
        hbox1.setAlignment(Pos.CENTER);
        hbox1.setPrefSize(150, 80);
        TextField textField = new TextField("Card");
        textField.setStyle("-fx-background-color: #ffffff");
        textField.setStyle("-fx-border-color: #ffffff;");
        textField.setAlignment(Pos.BASELINE_CENTER);
        hbox1.getChildren().add(textField);
        vbox.getChildren().addAll(myLabel, hbox1);
        vbox.setAlignment(Pos.CENTER);
        anchorPane1.getChildren().add(vbox);


        textField.setFont(new Font("System", 18));

        anchorPane1.setStyle("-fx-background-color:  #ffffff; " +
                "-fx-background-radius:  15; -fx-border-color: #cccccc;");
        anchorPane1.setPrefSize(150, 80);
        // create a FadeTransition for the anchorPane
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(700), anchorPane1);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.8);

// create a ScaleTransition for the anchorPane
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(700), anchorPane1);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.02);
        scaleTransition.setToY(1.08);

        anchorPane1.setOnMouseEntered(e -> {
            if (anchorPane1 != selectedAnchorPane) {
//                anchorPane1.setStyle("-fx-background-color:  #ffffff; " +
//                        "-fx-background-radius:  15; " +
//                        "-fx-border-color: #cccccc; " +
//                        "-fx-border-radius: 15; " +
//                        "-fx-border-width: 4; " +
//                        "-fx-margin: -2;");
                // start the animations
                fadeTransition.play();
                scaleTransition.play();
            }
        });

        anchorPane1.setOnMouseExited(e -> {
            if (anchorPane1 != selectedAnchorPane) {
                anchorPane1.setStyle("-fx-background-color:  #C0C0C0; " +
                        "-fx-background-radius:  15; " +
                        "-fx-border-color: transparent; " +
                        "-fx-margin: 0;");
            }
            scaleTransition.playFrom(scaleTransition.getTotalDuration());
            scaleTransition.stop();
            fadeTransition.stop();
            anchorPane1.setScaleX(1.0);
            anchorPane1.setScaleY(1.0);

            // create a new FadeTransition to reset the opacity to its original value
            FadeTransition resetFadeTran = new FadeTransition(Duration.millis(800), anchorPane1);
            resetFadeTran.setFromValue(0.8);
            resetFadeTran.setToValue(1.0);
            resetFadeTran.play();
        });

        return anchorPane1;
    }

    /**
     * This method colours the margins of the selected card
     *
     * @param anchorPane - the anchor pane that is selected
     */
    private void selectAnchorPane(AnchorPane anchorPane) {
        // set the new selected anchor pane
        anchorPane.setStyle("-fx-background-color:  #C0C0C0; -fx-background-radius:  15; " +
                "-fx-border-color: lightblue; -fx-border-radius: 15; -fx-border-width: 4;");
        selectedAnchorPane = anchorPane;
    }

    /**
     * This method discolours the margins of the previous selected card
     *
     * @param anchorPane - the anchor pane whose margins to be discoloured
     */
    private void resetAnchorPane(AnchorPane anchorPane) {
        // reset the anchor pane to its default appearance
        anchorPane.setStyle("-fx-background-color:  #C0C0C0; -fx-background-radius:  15; " +
                "-fx-border-color: transparent;");
        selectedAnchorPane = null;
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
            var column = server.addCardToColumn(id, columnid, mycard,
                    (long) vBox.getChildren().indexOf(button) - 2);
            ((TextField) ((HBox) ((VBox) anchorPane1.getChildren().get(0)).
                    getChildren().get(1)).getChildren().get(0)).setOnKeyPressed(event1 -> {
                        ((Label) (((VBox) anchorPane1.getChildren().get(0))
                        .getChildren().get(0))).setText("Press enter to save!!");
                        if (event1.getCode() == KeyCode.ENTER) {
                            int cardPosition = (vBox.getChildren().indexOf(anchorPane1) - 1);
                            server.updateCardTitle((long) cardPosition,
                                columnid, ((TextField) ((HBox) ((VBox) anchorPane1
                                    .getChildren().get(0)).
                                    getChildren().get(1)).getChildren().get(0)).
                                    getText(), id);
                            ((Label) (((VBox) anchorPane1.getChildren().get(0))
                            .getChildren().get(0))).setText("" + cardPosition);
                            Board tempBoard = server.getBoardById(id);
                            server.send("/app/update-labels-in-board", tempBoard);
                            server.pingCardUpdate(tempBoard.getColumns()
                                .get((int) (columnid - 1)).getCards().
                                get(cardPosition - 1).getId());
                        }
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
        ((HBox) ((VBox) anchorPane1.getChildren().get(0)).getChildren().
                get(1)).getChildren().get(0).setOnKeyPressed(event1 -> {
                    ((Label) (((VBox) anchorPane1.getChildren().get(0))
                    .getChildren().get(0))).setText("Press enter to save!!");
                    if (event1.getCode() == KeyCode.ENTER) {

                        int cardPosition = vBox.getChildren().
                            indexOf(anchorPane1) - 1;
                        server.updateCardTitle((long) (cardPosition), columnid,
                            ((TextField) ((HBox) ((VBox) anchorPane1
                                .getChildren().get(0)).getChildren().get(1)).
                                getChildren().get(0)).getText(), id);
                        ((Label) (((VBox) anchorPane1.getChildren().get(0))
                        .getChildren().get(0))).setText("...");

                        Board tempBoard = server.getBoardById(id);
                        server.send("/app/update-in-board", tempBoard);


                        long cardId = tempBoard.getColumns()
                                .get((int) (columnid - 1))
                                .getCards().get(cardPosition - 1).getId();

                        server.pingCardUpdate(cardId);
                    }
                });
    }

    /**
     * A method the set the label mouseclick
     *
     * @param label a label to get the property
     */
    public void setLabelAction(Label label) {
        label.setOnMouseClicked(event -> {
            labelActionGeneral(label);
        });
    }

    /**
     * A method the run the label mouseclick
     *
     * @param label Label to related to the card
     */

    public void labelActionGeneral(Label label) {
        int cardIndex = -2 + label.getParent()//HBox
                .getParent()   //Anchor
                .getParent()
                .getChildrenUnmodifiable()
                .indexOf(label.getParent().getParent());
        int columnIndex = hbox.getChildren().
                indexOf((AnchorPane)(label).getParent().getParent().getParent().getParent());;

        Board board = server.getBoardById(id);
        Column column = board.getColumns().get(columnIndex);
        Card card = column.getCards().get(cardIndex);



        mainCtrl.showCardView(card);
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
            dragboard.setDragView(column.snapshot(null, null));
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
            if (((Objects.equals(event.getDragboard().getString(), "DeletionCard")) ||
                    (Objects.equals(event.getDragboard().getString(), "DeletionColumn")) &&
                            !Objects.equals(event.getGestureSource(), myVBox.getParent()) &&
                            !(((AnchorPane) event.getGestureSource())
                                    .getParent().equals(myVBox)))) {
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            }
        });

        // Bug fix of disappearing of the addCard button because of duplication error
        myVBox.setOnDragDropped(event -> {
            if (Objects.equals(event.getDragboard().getString(), "DeletionCard") &&
                    !(((AnchorPane) event.getGestureSource()).getParent().equals(myVBox))) {
                myVBox.getChildren().remove(button);
                setCardDragDrop((AnchorPane) event.getGestureSource(), myVBox);
                server.cardDragDropUpdate((long) ((AnchorPane) event.getGestureSource()).
                        getParent().getChildrenUnmodifiable().indexOf((AnchorPane) event.
                                getGestureSource()), (long) hbox.getChildren()
                        .indexOf(((AnchorPane) event.
                                getGestureSource()).getParent().
                                getParent()), (long) hbox.getChildren().
                        indexOf(myVBox.getParent()), id);
                server.send("/app/update-in-board", server.getBoardById(id));
                myVBox.getChildren().add((AnchorPane) event.getGestureSource());
                myVBox.getChildren().add(button);
                event.setDropCompleted(true);
                event.consume();
                server.send("app/update-in-board", server.getBoardById(id));
            }
            if (Objects.equals(event.getDragboard().getString(), "DeletionCard") &&
                    (((AnchorPane) event.getGestureSource()).getParent().equals(myVBox))) {
                sameColumnDirectBottomDragDrop(event, button, myVBox);
                event.setDropCompleted(true);
                event.consume();
            }
            if ((Objects.equals(event.getDragboard().getString(), "DeletionColumn"))) {
                if (event.getX() >= 75) {
                    if ((hbox.getChildren().indexOf(((AnchorPane) event.getGestureSource()))) -
                            (hbox.getChildren().indexOf(myVBox.getParent())) == 1) {
                        event.setDropCompleted(true);
                        event.consume();
                    } else {
                        rightColumnDrop(myVBox, event);
                        event.setDropCompleted(true);
                        event.consume();
                        server.send("app/update-in-board", server.getBoardById(id));
                    }
                } else {
                    if ((hbox.getChildren().indexOf(myVBox.getParent())) - (hbox.getChildren()
                            .indexOf(((AnchorPane) event.getGestureSource()))) == 1) {
                        event.setDropCompleted(true);
                        event.consume();
                    } else {
                        leftColumnDrop(myVBox, event);
                        event.setDropCompleted(true);
                        event.consume();
                        server.send("app/update-in-board", server.getBoardById(id));
                    }
                }
            }
            server.send("/app/update-in-board", server.getBoardById(id));

        });
    }

    /**
     * A method to rearrange Column with drag and drop
     *
     * @param vBox  vBox that the column is dropped in
     * @param event Drag event handler to get the source of the drag&drop
     */
    public void leftColumnDrop(VBox vBox, DragEvent event) {
        Board board1 = server.getBoardById(id);
        int sourceIndex = hbox.getChildren().indexOf((AnchorPane) event.getGestureSource());
        int targetIndex = hbox.getChildren().indexOf(vBox.getParent());
        Column targetColumn = board1.getColumns().get(targetIndex);
        Column sourceColumn = board1.getColumns().get(sourceIndex);
        Board boardTmp = new Board();
        boardTmp = boardTmp.copyBoard(board1, boardTmp);
        board1.getColumns().clear();
        boardTmp.getColumns().set(sourceIndex, null);
        for (int i = boardTmp.getColumns().size() - 1; i >= 0; i--) {

            if (boardTmp.getColumns().get(i) != null) {
                board1.getColumns().add(boardTmp.getColumns().get(i));
            }
            if (i == targetIndex) {
                board1.getColumns().add(sourceColumn);
            }
        }
        Collections.reverse(board1.getColumns());
        server.updateBoard(board1, Math.toIntExact(id));
        columnsRefresh();
    }

    /***
     *
     * @param event
     * @param button
     * @param myVBox
     */
    public void sameColumnDirectBottomDragDrop(DragEvent event, Button button, VBox myVBox) {
        int cardId = ((AnchorPane) event.getGestureSource()).
                getParent().getChildrenUnmodifiable().indexOf((AnchorPane) event.
                        getGestureSource());
        int columnId = hbox.getChildren().
                indexOf(myVBox.getParent());
        myVBox.getChildren().remove(button);
        myVBox.getChildren().remove((AnchorPane) event.getGestureSource());
        setCardDragDrop((AnchorPane) event.getGestureSource(), myVBox);
        Board board1 = server.getBoardById(id);
        Column column = board1.getColumns().get(columnId);
        Card card = column.getCards().get(cardId - 2);
        column.getCards().remove(card);
        column.getCards().add(card);
        server.updateCardArrangement(columnId, column, id);
        server.send("/app/update-in-board", server.getBoardById(id));
        myVBox.getChildren().add((AnchorPane) event.getGestureSource());
        myVBox.getChildren().add(button);
    }

    /**
     * A method to rearrange Column with drag and drop
     *
     * @param vBox  vBox that the column is dropped in
     * @param event Drag event handler to get the source of the drag&drop
     */
    public void rightColumnDrop(VBox vBox, DragEvent event) {
        Board board1 = server.getBoardById(id);
        int sourceIndex = hbox.getChildren().indexOf((AnchorPane) event.getGestureSource());
        int targetIndex = hbox.getChildren().indexOf(vBox.getParent());
        Column targetColumn = board1.getColumns().get(targetIndex);
        Column sourceColumn = board1.getColumns().get(sourceIndex);
        Board boardTmp = new Board();
        boardTmp = boardTmp.copyBoard(board1, boardTmp);
        board1.getColumns().clear();
        boardTmp.getColumns().set(sourceIndex, null);

        for (int i = 0; i < boardTmp.getColumns().size(); i++) {
            if (boardTmp.getColumns().get(i) != null) {
                board1.getColumns().add(boardTmp.getColumns().get(i));
            }
            if (i == targetIndex) {
                board1.getColumns().add(sourceColumn);
            }
        }
        server.updateBoard(board1, Math.toIntExact(id));
        columnsRefresh();

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
            dragboard.setDragView(card.snapshot(null, null));
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString("DeletionCard");
            dragboard.setContent(clipboardContent);
            event.consume();
            cardBin(vBox);
        });

        card.setOnDragOver(event -> {
            if (event.getDragboard().getString().
                    equals("DeletionCard") && !card.equals(event.getGestureSource())
            ) {  // && ((AnchorPane) event.getGestureSource()).getParent().equals(vBox)
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        card.setOnDragDropped(event -> {

            if (!((AnchorPane) event.getGestureSource()).getParent().equals(vBox)) {
                directDragDrop(card, vBox, event);
                event.setDropCompleted(true);
                event.consume();
            }
            //bottom side
            else {
                if (event.getY() >= 40) {
                    if ((vBox.getChildren().indexOf(event.getGestureSource()) - 2) -
                            (vBox.getChildren().indexOf(card) - 2) == 1) {
                        event.setDropCompleted(true);
                        event.consume();
                    } else {
                        lowerCardDrop(card, vBox, event);
                        server.send("/app/update-in-board", server.getBoardById(id));
                        event.setDropCompleted(true);
                        event.consume();


                    }
                } //upperside
                else {
                    if ((vBox.getChildren().indexOf(card) - 2) -
                            (vBox.getChildren().indexOf(event.getGestureSource()) - 2) == 1) {
                        event.setDropCompleted(true);
                        event.consume();
                    } else {
                        upperCardDrop(card, vBox, event);
                        server.send("/app/update-in-board", server.getBoardById(id));
                        event.setDropCompleted(true);
                        event.consume();
                    }
                }
            }
            server.send("/app/update-in-board", server.getBoardById(id));

        });
    }

    /**
     * A method to direct drag and drop of the card
     *
     * @param card  A card that is dragged
     * @param vBox  target vbox of the event
     * @param event DragEvent handler
     */
    public void directDragDrop(AnchorPane card, VBox vBox, DragEvent event) {
        if (event.getY() >= 40) {
            directLower(card, vBox, event);

        } else {
            directUpper(card, vBox, event);

        }
    }

    /**
     * A method for lower part of the card drop
     *
     * @param card  A card that is dragged
     * @param vBox  target vbox of the event
     * @param event DragEvent handler
     */
    public void directLower(AnchorPane card, VBox vBox, DragEvent event) {
        int indexColumnSource = hbox.getChildren()
                .indexOf(((AnchorPane) event.getGestureSource()).getParent().getParent());
        int indexColumnTarget = hbox.getChildren().indexOf(vBox.getParent());
        Board board1 = server.getBoardById(id);
        Column columnTarget = board1.getColumns()
                .get(indexColumnTarget);
        Column columnSource = board1.getColumns().get(indexColumnSource);
        Column columnTmp = new Column();
        columnTmp = columnSource.copyCards(columnSource, columnTmp);
        int indexCardTarget = vBox.getChildren().indexOf(card) - 2;
        int indexCardSource = ((AnchorPane) event.getGestureSource())
                .getParent().getChildrenUnmodifiable().indexOf(event.getGestureSource()) - 2;
        Card sourceCard = columnSource.getCards().get(indexCardSource);


        columnSource.getCards().clear();
        for (int i = 0; i < columnTmp.getCards().size(); i++) {
            if (i != indexCardSource) {
                columnSource.getCards().add(columnTmp.getCards().get(i));
            }
        }
        server.updateCardArrangement(indexColumnSource, columnSource, id);
//        columnsRefresh();
        columnTmp = new Column();
        columnTmp = columnTarget.copyCards(columnTarget, columnTmp);
        columnTarget.getCards().clear();
        for (int i = 0; i < columnTmp.getCards().size(); i++) {
            columnTarget.getCards().add(columnTmp.getCards().get(i));
            if (i == indexCardTarget) {
                columnTarget.getCards().add(sourceCard);
            }
        }
        server.updateCardArrangement(indexColumnTarget, columnTarget, id);
        server.send("app/update-in-board", server.getBoardById(id));
        columnsRefresh();
    }

    /**
     * A method for upper part of the card drop
     *
     * @param card  A card that is dragged
     * @param vBox  target vbox of the event
     * @param event DragEvent handler
     */
    public void directUpper(AnchorPane card, VBox vBox, DragEvent event) {
        int indexColumnSource = hbox.getChildren()
                .indexOf(((AnchorPane) event.getGestureSource()).getParent().getParent());
        int indexColumnTarget = hbox.getChildren().indexOf(vBox.getParent());
        Board board1 = server.getBoardById(id);
        Column columnTarget = board1.getColumns()
                .get(indexColumnTarget);
        Column columnSource = board1.getColumns().get(indexColumnSource);
        Column columnTmp = new Column();
        columnTmp = columnSource.copyCards(columnSource, columnTmp);
        int indexCardTarget = vBox.getChildren().indexOf(card) - 2;
        int indexCardSource = ((AnchorPane) event.getGestureSource())
                .getParent().getChildrenUnmodifiable().indexOf(event.getGestureSource()) - 2;
        Card sourceCard = columnSource.getCards().get(indexCardSource);

        columnSource.getCards().clear();
        for (int i = 0; i < columnTmp.getCards().size(); i++) {
            if (i != indexCardSource) {
                columnSource.getCards().add(columnTmp.getCards().get(i));
            }
        }
        server.updateCardArrangement(indexColumnSource, columnSource, id);
        columnTmp = new Column();
        columnTmp = columnTarget.copyCards(columnTarget, columnTmp);
        columnTarget.getCards().clear();
        for (int i = -1; i < columnTmp.getCards().size(); i++) {
            if (i != -1) {
                columnTarget.getCards().add(columnTmp.getCards().get(i));
            }

            if (i + 1 == indexCardTarget) {
                columnTarget.getCards().add(sourceCard);
            }
        }
        server.updateCardArrangement(indexColumnTarget, columnTarget, id);
        server.send("app/update-in-board", server.getBoardById(id));
        columnsRefresh();
    }

    /**
     * A method to handle lower part of the drag and drop
     *
     * @param card  the card that is dragged
     * @param vBox  The container that the cards will be arranged
     * @param event Drag event handler that handles
     *              the mouse movements and other functionalities etc.
     */
    public void lowerCardDrop(AnchorPane card, VBox vBox, DragEvent event) {
        Board board1 = server.getBoardById(id);
        Column column = board1.getColumns()
                .get(hbox.getChildren().indexOf(vBox.getParent()));
        Column columnTmp = new Column();
        columnTmp = column.copyCards(column, columnTmp);
        Card sourceCard = column.getCards()
                .get(vBox.getChildren().indexOf(event.getGestureSource()) - 2);
        int indexTarget = vBox.getChildren().indexOf(card) - 2;
        column.getCards().clear();
        columnTmp.getCards()
                .set(vBox.getChildren().indexOf(event.getGestureSource()) - 2, null);
        for (int i = 0; i < columnTmp.getCards().size(); i++) {

            if (columnTmp.getCards().get(i) != null) {
                column.getCards().add(columnTmp.getCards().get(i));
            }
            if (i == indexTarget) {
                column.getCards().add(sourceCard);
            }
        }
        server.updateCardArrangement(hbox.getChildren()
                .indexOf(vBox.getParent()), column, id);
        server.send("app/update-in-board", server.getBoardById(id));
        columnsRefresh();
    }

    /**
     * A method to handle upper part of the drag and drop
     *
     * @param card  the card that is dragged
     * @param vBox  The container that the cards will be arranged
     * @param event Drag event handler that handles the
     *              mouse movements and other functionalities etc.
     */
    public void upperCardDrop(AnchorPane card, VBox vBox, DragEvent event) {
        Board board1 = server.getBoardById(id);
        Column column = board1.getColumns()
                .get(hbox.getChildren().indexOf(vBox.getParent()));
        Column columnTmp = new Column();
        columnTmp = column.copyCards(column, columnTmp);
        Card sourceCard = column.getCards()
                .get(vBox.getChildren().indexOf(event.getGestureSource()) - 2);
        Card targetCard = column.getCards().get(vBox.getChildren().indexOf(card) - 2);
        int indexSource = vBox.getChildren().indexOf(event.getGestureSource()) - 2;
        int indexTarget = vBox.getChildren().indexOf(card) - 2;
        column.getCards().clear();
        columnTmp.getCards()
                .set(vBox.getChildren().indexOf(event.getGestureSource()) - 2, null);
        for (int i = columnTmp.getCards().size() - 1; i >= 0; i--) {

            if (columnTmp.getCards().get(i) != null) {
                column.getCards().add(columnTmp.getCards().get(i));
            }
            if (i == indexTarget) {
                column.getCards().add(sourceCard);
            }
        }
        Collections.reverse(column.getCards());
        server.updateCardArrangement(hbox.getChildren()
                .indexOf(vBox.getParent()), column, id);
        server.send("app/update-in-board", server.getBoardById(id));
        columnsRefresh();
    }

    /**
     * set the BIN according to column deletion to avoid gesture/drag and drop conflicts
     */
    private void columnBin() {
        //set the BIN text (get(2) because BIN is second indexed element in the anchorPane.
        // TODO This can be done byID later on)...
        anchorPane.getChildren().get(4).setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        //deletion of the dragged item
        anchorPane.getChildren().get(4).setOnDragDropped(event -> {
            //gesture source to pass dragged item
            int colInd = hbox.getChildren().indexOf(event.getGestureSource());
            //server.deleteColumn(colInd, id);
            var tempBoard = server.getBoardById(id);
            List<Card> cardList = tempBoard.getColumns().get(colInd).getCards();
            server.deleteColumnFromApi(Math.toIntExact(server.deleteColumn(colInd, id)));

            //Ping server for the deleted cards.
            for (Card c : cardList) {
                server.pingCardDeletion(c.getId());
            }
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
        anchorPane.getChildren().get(4).setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        //deletion of the dragged item
        anchorPane.getChildren().get(4).setOnDragDropped(event -> {
            // gesture source to pass dragged item

            Long cardId = server.deleteCardServer(server.getBoardById(id),
                    Long.valueOf(((AnchorPane) event.getGestureSource()).getParent().
                            getChildrenUnmodifiable().
                            indexOf((AnchorPane) event.getGestureSource())),
                    (long) hbox.getChildren().
                            indexOf(((AnchorPane) event.getGestureSource()).
                                    getParent().getParent()) + 1, id);
            server.send("/app/update-in-board", server.getBoardById(id));
            server.pingCardDeletion(cardId);
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
        Board boardd = server.getBoardById(mainCtrl.getBoardId());
        Color colorBoard = Color.color(boardd.getRed(), boardd.getGreen(), boardd.getBlue());
        hbox.setStyle("-fx-background-color: " + toRgbCode(colorBoard) +
                ";");
        board.setStyle("-fx-background-color: " + toRgbCode(colorBoard) +
                ";");
        scrollPaneBoard.setStyle("-fx-border-color: " + toRgbCode(colorBoard) +
                ";");
        var progressionHash = server.getProgressionHashMap(id);
        for (Column c : server.getBoardById(id).getColumns()) {
            AnchorPane anchorPaneVBox = new AnchorPane();
            ScrollPane scrollPane = new ScrollPane();
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.TOP_CENTER);
            scrollPane.setContent(vBox);
            vBox.setPrefHeight(500);
            vBox.setPrefWidth(150);
            Color colorColumn = Color.color(c.getRed(), c.getGreen(), c.getBlue());
            vBox.setStyle("-fx-background-color: " + toRgbCode(colorColumn) +
                    "; -fx-background-radius: 15px; -fx-border-radius: 15px;" +
                    " -fx-border-color: #000000;");
            TextField textField = new TextField(c.getTitle());
            textField.setAlignment(Pos.CENTER);
            textField.setStyle("-fx-border-color: #cccccc;" +
                    " -fx-background-radius:  15; -fx-border-radius: 15px;");
            Label columnLabel = new Label("...");
            vBox.setMargin(textField, new Insets(2));
            anchorPaneVBox.getChildren().add(vBox);
            setColumnDragDrop(anchorPaneVBox);
            hbox.getChildren().add(anchorPaneVBox);
            textField.setOnKeyPressed(e ->
            {
                columnLabel.setText("Press enter to save!!");
                if (e.getCode() == KeyCode.ENTER) {
                    updateColTitle(hbox.getChildren().indexOf(anchorPaneVBox) + 1,
                            textField.getText());
                    columnLabel.setText("...");}
            });
            Button button =
                    createButton(vBox, (long) hbox.getChildren().indexOf(anchorPaneVBox) + 1);
            setVBoxDragDrop(button, vBox);

            button.setAlignment(Pos.BOTTOM_CENTER);
            vBox.getChildren().addAll(columnLabel, textField);
            for (Card kard : c.getCards()) {
                AnchorPane anchorPane1 = addCard(vBox);
                HBox tagColors = new HBox();
                tagColors.setAlignment(Pos.BOTTOM_CENTER);
                tagColors.setPrefSize(150, 10);
                List<Tag> tags = new ArrayList<>(kard.getTags());
                //int a =0;
                for (int i = 0; i < kard.getTags().size(); i++) {
                    Tag tag = tags.get(i);
                    if (tag.getHighlightRed() != 1.0 || tag.getHighlightGreen() != 1.0
                            || tag.getHighlightBlue() != 1.0) {
                        AnchorPane colorPane = new AnchorPane();
                        Color color = Color.color(tag.getHighlightRed(), tag.getHighlightGreen(),
                                tag.getHighlightBlue());
                        String rgbCode = toRgbCode(color);
                        colorPane.setStyle("-fx-background-color: " + rgbCode + ";");
                        colorPane.setPrefSize(20, 10);
                        tagColors.getChildren().add(colorPane);
                        if (tagColors.getChildren().size() == 5) {
                            break;
                        }
                    }
                }


                AnchorPane.setBottomAnchor(tagColors, 25.0);
                anchorPane1.getChildren().addAll(tagColors);
                Color color = Color.color(kard.getRed(), kard.getGreen(), kard.getBlue());
                anchorPane1.setStyle("-fx-background-color: " + toRgbCode(color) +
                        "; -fx-background-radius: 15px; -fx-border-radius: 15px;");
                anchorPane1.getChildren().get(0).
                        setStyle("-fx-background-color: " + toRgbCode(color) +
                                "; -fx-background-radius: 15px; -fx-border-radius: 15px;");
                VBox child1 = (VBox) anchorPane1.getChildren().get(0);
                HBox child2 = (HBox) child1.getChildren().get(1);
                TextField child3 = (TextField) child2.getChildren().get(0);
                child3.setStyle("-fx-background-color: " + toRgbCode(color) +
                        "; -fx-background-radius: 15px; -fx-border-radius: 15px;");
                ((TextField) ((HBox) ((VBox) anchorPane1.getChildren().get(0)).
                        getChildren().get(1)).getChildren().get(0)).setText(kard.getTitle());
                setTextField(anchorPane1, button, vBox,
                        (long) hbox.getChildren().indexOf(anchorPaneVBox) + 1);
                ((VBox) anchorPane1.getChildren().get(0))
                        .getChildren()
                        .add(cardProgressionVisual(progressionHash.get(kard.getId()), kard));
                vBox.getChildren().add(anchorPane1);
            }
            vBox.getChildren().add(button);
        }
        setHBoxDrop(hbox);
    }

    /**
     * This method creates a new container pane that displays the progression in
     * the Tasks of the given Card instance, and whether the Card contains
     * further description
     *
     * @param progression the percentage of progression in the completion of
     *                    Tasks. Input -1 if there are no tasks at all.
     * @param card        the current Card instance.
     * @return the container pane HBox.
     */
    private HBox cardProgressionVisual(double progression, Card card) {

        //Create a main pane
        HBox mainPane = new HBox();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setPadding(new Insets(2, 0, 2, 0));

        //Create a description images
        ImageView descriptionIcon = new ImageView();
        descriptionIcon.setImage(new Image("DescriptionIcon.png"));
        descriptionIcon.setVisible(card.getDescription() != null
                && !card.getDescription().isBlank());
        descriptionIcon.setFitHeight(15);
        descriptionIcon.setFitWidth(15);
        mainPane.getChildren().add(descriptionIcon);
        VBox.setMargin(descriptionIcon, new Insets(5, 10, 5, 10));

        //Create a progress bar
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefHeight(15);
        progressBar.setVisible(progression != -1.0);
        progressBar.setProgress(progression);
        mainPane.getChildren().add(progressBar);
        VBox.setMargin(progressBar, new Insets(5, 10, 5, 10));

        return mainPane;

    }

    /**
     * The method initializes this instance.
     *
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

        // Set up the dialog for the help button
        helpPopUp();
        setScrollPaneShortcuts();

        showCardCustomization();
    }

    /**
     * A method that creates the labels for the ? button and shortcut
     *
     * @return list of labels
     */
    public ArrayList<Label> helpLabel() {
        ArrayList<Label> labels = new ArrayList<>();
        // Add each keyboard shortcut to the VBox
        labels.add(new Label("Shift+/ -> open help pop-up"));
        labels.add(new Label("Up/Down/Left/Right -> select tasks"));
        labels.add(new Label("Shift+Up/Down -> change order of cards in the column"));
        labels.add(new Label("E -> edit the card title"));
        labels.add(new Label("Del/Backspace -> delete a card"));
        labels.add(new Label("Enter -> open card details"));
        labels.add(new Label("Esc -> close card details"));
        labels.add(new Label("T -> open popup for adding tags"));
        labels.add(new Label("C -> open popup for color preset selection"));
        return labels;
    }

    /**
     * A method to return the list of labels to represent the help information for the drag and drop
     *
     * @return list of labels which includes information
     */
    public ArrayList<Label> helpDragDrop() {
        ArrayList<Label> labels = new ArrayList<>();
        // Add each keyboard shortcut to the VBox
        labels.add(new Label("* To use the drag and drop feature, please follow these steps:"));
        labels.add(new Label("\n"));
        labels.add(new Label("Locate the item you wish to move within your to-do list."));
        labels.add(new Label("Click and hold on the item with your mouse or trackpad."));
        labels.add(new Label("While holding down the mouse button, drag the " +
                "item to its new location within the list."));
        labels.add(new Label("Release the mouse button to drop the " +
                "item into its new location."));
        labels.add(new Label("-----------------------------------------------------" +
                "-----------------------------------\n"));
        labels.add(new Label("* Our application supports several combinations " +
                "of drag and drop actions, including:"));
        labels.add(new Label("\n"));
        labels.add(new Label("Moving a card to a different column or position within the same column."));
        labels.add(new Label("Rearranging columns by dragging and dropping them into different positions."));
        labels.add(new Label("Deleting items by dragging them to the BIN and dropping them."));
        return labels;
    }

    /**
     * Set up the dialog for the help button
     */
    public void helpPopUp() {
        helpDialog = new Dialog<String>();
        helpDialog.initModality(Modality.APPLICATION_MODAL);
        helpDialog.setTitle("Help");
        helpDialog.setHeaderText("Help zone");

        Stage dialogStage2 = (Stage) helpDialog.getDialogPane().getScene().getWindow();
// Create a TabPane to hold the keyboard shortcuts list and other tabs
        TabPane tabPane = new TabPane();
        tabPane.setTabMinWidth(Double.MAX_VALUE);
        tabPane.setTabMinWidth(Double.MAX_VALUE);
        tabPane.setTabMinHeight(50);
        tabPane.setTabMaxHeight(50);

        VBox shortcutsList = new VBox();
        shortcutsList.setSpacing(5);
        shortcutsList.setPadding(new Insets(15.0, 5.0, 5.0, 5.0));
        shortcutsList.getChildren().addAll(helpLabel());
        VBox dragAndDropList = new VBox();
        dragAndDropList.setSpacing(5);
        dragAndDropList.setPadding(new Insets(15.0, 5.0, 5.0, 5.0));
        dragAndDropList.getChildren().addAll(helpDragDrop());

        Tab shortcutsTab = new Tab("Keyboard Shortcuts", shortcutsList);
        tabPane.getTabs().add(shortcutsTab);

        Tab dragTab = new Tab("Drag and Drop Information", dragAndDropList);
        tabPane.getTabs().add(dragTab);
        tabPane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);
// Center and fill the TabPane
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setTabMinWidth(100);
        tabPane.setTabMaxWidth(Double.MAX_VALUE);
        tabPane.setTabMinHeight(30);
        tabPane.setTabMaxHeight(30);


        VBox.setVgrow(tabPane, Priority.ALWAYS);

// Add the TabPane to the dialog's content
        helpDialog.getDialogPane().setContent(tabPane);

// Add an OK button to the dialog
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        helpDialog.getDialogPane().getButtonTypes().add(okButtonType);
        // Add a listener to the scene to detect when the Shift+/ key combination is pressed
        anchorPane.setOnKeyPressed(event -> {
            int shiftColumnIndex = -1;
            if (selectedAnchorPane != null) {
                shiftColumnIndex = hbox.getChildren().
                        indexOf(selectedAnchorPane.getParent().getParent());
            }
            if (event.isShiftDown() && event.getCode() == KeyCode.SLASH) {
                if (!(event.getTarget() instanceof TextField)) {
                    helpDialog.showAndWait();
                }
            }
            if (event.getCode() == KeyCode.ENTER && selectedAnchorPane != null &&
                    !(event.getTarget() instanceof TextField)) {
                labelActionGeneral((Label) (((VBox) selectedAnchorPane.
                        getChildren().get(0)).getChildren().get(0)));
            }
            if (selectedAnchorPane != null && event.getCode() == KeyCode.E &&
                    !(event.getTarget() instanceof TextField)) {
                keyECard(event);

            } else if (event.isShiftDown() && event.getCode() == KeyCode.UP
                    && selectedAnchorPane != null) {
                int index = ((VBox) selectedAnchorPane.getParent()).
                        getChildren().indexOf(selectedAnchorPane);
                if (index != 2) {
                    keyShiftUpCard(index);
                    selectAnchorPane((AnchorPane) ((VBox) ((AnchorPane) hbox.getChildren().
                            get(shiftColumnIndex)).getChildren().get(0))
                            .getChildren().get(index - 1));
                }
                server.send("/app/update-in-board", server.getBoardById(id));
                event.consume();

            } else if (event.isShiftDown() && event.getCode() ==
                    KeyCode.DOWN && selectedAnchorPane != null) {
                int index = ((VBox) selectedAnchorPane.getParent()).
                        getChildren().indexOf(selectedAnchorPane);
                if (index != ((VBox) selectedAnchorPane.getParent()).getChildren().size() - 2) {
                    keyShiftDownCard(index);
                    selectAnchorPane((AnchorPane) ((VBox) ((AnchorPane) hbox.getChildren().
                            get(shiftColumnIndex)).getChildren().
                            get(0)).getChildren().get(index + 1));
                }
                server.send("/app/update-in-board", server.getBoardById(id));
                event.consume();
            } else if (event.getCode() == KeyCode.C && selectedAnchorPane != null) {

                cardCustomization.showAndWait();
            }
        });
    }

    /**
     * This method creates a popup for the card customization shortcut
     */
    public void showCardCustomization() {
        cardCustomization = new Dialog<>();
        cardCustomization.setTitle("Card Customization");

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.WHITE);
        colorPicker.getStyleClass().add("button");

        VBox content = new VBox();

        Label cardColor = new Label("New card colour:");
        HBox newColourBox = new HBox(10, cardColor, colorPicker);
        content.getChildren().add(newColourBox);

        ButtonType saveQuitButtonType = new ButtonType("Save & Quit", ButtonBar.ButtonData.OK_DONE);
        cardCustomization.getDialogPane().
                getButtonTypes().addAll(saveQuitButtonType, ButtonType.CANCEL);
        Node saveQuitButton = cardCustomization.getDialogPane().lookupButton(saveQuitButtonType);
        saveQuitButton.setDisable(true);
        colorPicker.setOnAction(event -> {
            saveQuitButton.setDisable(false);
        });

        ((Button) saveQuitButton).setOnAction(event -> {
            // Code to be executed when the button is clicked
            Color newColor = colorPicker.getValue();
            // Add more code here to perform specific actions when the button is clicked
            cardCustomization.close(); // Close the dialog
            int indexColumnCurrent = hbox.getChildren().
                    indexOf(selectedAnchorPane.getParent().getParent());
            int cardIndex = ((VBox) selectedAnchorPane.getParent()).
                    getChildren().indexOf(selectedAnchorPane);
            Board board = server.getBoardById(id);
            Column column = board.getColumns().get(indexColumnCurrent);
            Card card = column.getCards().get(cardIndex - 2);
            saveColor(card, newColor);
            saveQuitButton.setDisable(true);
        });
        cardCustomization.getDialogPane().setContent(content);
    }

    /**
     * Zort
     *
     * @param card
     * @param color
     */
    public void saveColor(Card card, Color color) {
        card.setColor(color.getBlue(), color.getGreen(), color.getRed());
        Board board = server.getBoardById(mainCtrl.getBoardId());
        Long colId = Long.valueOf(-1);
        for (Column c : board.getColumns()) {
            for (Card cardCheck : c.getCards()) {
                if (cardCheck.getId() == card.getId()) {
                    colId = c.getId();
                    break;
                }
            }
        }
        server.updateCardInColumnColor(card.getId(), card, colId, mainCtrl.getBoardId());
        server.send("/app/update-in-board", server.getBoardById(mainCtrl.getBoardId()));
    }


    /**
     * A method that sets the shortcuts of the scrollPane
     */
    public void setScrollPaneShortcuts() {
        ((ScrollPane) ((AnchorPane) anchorPane.getChildren().get(8)).getChildren().
                get(0)).setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.UP &&
                            selectedAnchorPane != null && !event.isShiftDown()) {
                        int index = ((VBox) selectedAnchorPane.getParent()).
                                getChildren().indexOf(selectedAnchorPane);
                        if (index != 2) keyUpCard(index);
                        event.consume();
                    } else if (event.getCode() == KeyCode.DOWN &&
                            selectedAnchorPane != null && !event.isShiftDown()) {
                        int index = ((VBox) selectedAnchorPane.getParent()).
                                getChildren().indexOf(selectedAnchorPane);
                        if (index != ((VBox) selectedAnchorPane.getParent()).
                                getChildren().size() - 2) {
                            keyDownCard(index);
                            event.consume();
                        }
                    } else if ((event.getCode() == KeyCode.DELETE ||
                            event.getCode() == KeyCode.BACK_SPACE) && selectedAnchorPane != null) {
                        server.deleteCardServer(server.getBoardById(id),
                                Long.valueOf(((AnchorPane) selectedAnchorPane).getParent().
                                        getChildrenUnmodifiable().
                                        indexOf((AnchorPane) selectedAnchorPane)),
                                (long) hbox.getChildren().
                                        indexOf(((AnchorPane) selectedAnchorPane).
                                                getParent().getParent()) + 1, id);
                        columnsRefresh();
                        server.send("/app/update-in-board", server.getBoardById(id));
                        event.consume();
                    } else if (event.getCode() == KeyCode.LEFT && selectedAnchorPane != null) {
                        leftKeyCheck(event);
                    } else if (event.getCode() == KeyCode.RIGHT && selectedAnchorPane != null) {
                        rightKeyCheck(event);
                    }
                    else if (event.getCode() == KeyCode.T && !(event.getTarget() instanceof TextArea)
                            && selectedAnchorPane != null) {
                        try {
                            Long selectedCardID = Long.valueOf(((AnchorPane) selectedAnchorPane).getParent().
                                    getChildrenUnmodifiable().
                                    indexOf((AnchorPane) selectedAnchorPane));
                            Board board = server.getBoardById(id);
                            Column column = board.getColumns().get(hbox.getChildren().
                                    indexOf(((AnchorPane) selectedAnchorPane).
                                            getParent().getParent()));
                            Card card = column.getCards().get(Math.toIntExact(selectedCardID - 2));
                            mainCtrl.getTagViewCtrl().setCard(card);
                            mainCtrl.showTagViewShortcut(card);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    /**
     * Check whether action can be done
     *
     * @param event key event handler
     */
    public void rightKeyCheck(KeyEvent event) {
        int indexColumnCurrent = hbox.getChildren().
                indexOf(selectedAnchorPane.getParent().getParent());
        int cardIndex = ((VBox) selectedAnchorPane.getParent()).
                getChildren().indexOf(selectedAnchorPane);
        if (indexColumnCurrent < hbox.getChildren().size() - 1 &&
                cardIndex < ((VBox) ((AnchorPane) hbox.getChildren().
                        get(indexColumnCurrent + 1)).getChildren().
                        get(0)).getChildren().size() - 1) {
            if ((AnchorPane) ((VBox) ((AnchorPane) hbox.getChildren().
                    get(indexColumnCurrent + 1)).getChildren().get(0)).
                    getChildren().get(cardIndex) instanceof AnchorPane) {
                keyRightCard(indexColumnCurrent);
                event.consume();
            }

        }
    }

    /**
     * Check whether action can be done
     *
     * @param event key event handler
     */
    public void leftKeyCheck(KeyEvent event) {
        int indexColumnCurrent = hbox.getChildren().
                indexOf(selectedAnchorPane.getParent().getParent());
        int cardIndex = ((VBox) selectedAnchorPane.getParent()).
                getChildren().indexOf(selectedAnchorPane);
        if (indexColumnCurrent != 0 && cardIndex < ((VBox) ((AnchorPane) hbox.getChildren().
                get(indexColumnCurrent - 1)).getChildren().get(0)).getChildren().size() - 1) {
            if ((AnchorPane) ((VBox) ((AnchorPane) hbox.getChildren().
                    get(indexColumnCurrent - 1)).getChildren().get(0)).
                    getChildren().get(cardIndex) instanceof AnchorPane) {
                keyLeftCard(indexColumnCurrent);
                event.consume();
            }
        }
    }

    /**
     * A method to replace a card upwards by pressing the shift + up/arrow
     *
     * @param index index of the card
     */
    public void keyShiftUpCard(int index) {
        Board board1 = server.getBoardById(id);
        int columnIndex = hbox.getChildren().indexOf(selectedAnchorPane.getParent().getParent());
        Column column = board1.getColumns()
                .get(columnIndex);
        Card cardLower = column.getCards().get(index - 2);
        Card cardUpper = column.getCards().get(index - 3);
        column.getCards().set(index - 2, cardUpper);
        column.getCards().set(index - 3, cardLower);
        server.updateCardArrangement(hbox.getChildren()
                .indexOf(selectedAnchorPane.getParent().getParent()), column, id);
        columnsRefresh();
    }

    /**
     * A method to replace a card downwards by pressing the shift + down/arrow
     *
     * @param index index of the card
     */
    public void keyShiftDownCard(int index) {
        Board board1 = server.getBoardById(id);
        int columnIndex = hbox.getChildren().indexOf(selectedAnchorPane.getParent().getParent());
        Column column = board1.getColumns()
                .get(columnIndex);
        Card cardUpper = column.getCards().get(index - 2);
        Card cardLower = column.getCards().get(index - 1);
        column.getCards().set(index - 2, cardLower);
        column.getCards().set(index - 1, cardUpper);
        server.updateCardArrangement(hbox.getChildren()
                .indexOf(selectedAnchorPane.getParent().getParent()), column, id);
        columnsRefresh();
    }

    /**
     * A method for a functionality to shift right between cards!
     *
     * @param index index of the card
     */
    public void keyRightCard(int index) {
        int cardIndex = ((VBox) selectedAnchorPane.getParent()).
                getChildren().indexOf(selectedAnchorPane);
        resetAnchorPane(selectedAnchorPane);
        selectAnchorPane((AnchorPane) ((VBox) ((AnchorPane) hbox.getChildren().
                get(index + 1)).getChildren().get(0)).getChildren().get(cardIndex));
    }

    /**
     * A method for a functionality to shift left between cards!
     *
     * @param index index of the card
     */
    public void keyLeftCard(int index) {
        int cardIndex = ((VBox) selectedAnchorPane.getParent()).
                getChildren().indexOf(selectedAnchorPane);
        resetAnchorPane(selectedAnchorPane);
        selectAnchorPane((AnchorPane) ((VBox) ((AnchorPane) hbox.getChildren().
                get(index - 1)).getChildren().get(0)).getChildren().get(cardIndex));
    }

    /**
     * A method to handle when down pressed in focus of card.
     *
     * @param index index of the card
     */
    public void keyDownCard(int index) {
        ((AnchorPane) ((VBox) selectedAnchorPane.getParent()).
                getChildren().get(index)).
                setStyle("-fx-background-color:  " +
                        "#C0C0C0; -fx-background-radius:  15; " +
                        "-fx-border-color: transparent;");

        selectAnchorPane((AnchorPane) ((VBox) selectedAnchorPane.
                getParent()).getChildren().get(index + 1));
    }

    /**
     * A method to handle when up pressed in focus of card.
     *
     * @param index index of the card
     */
    public void keyUpCard(int index) {
        ((AnchorPane) ((VBox) selectedAnchorPane.getParent()).
                getChildren().get(index)).
                setStyle("-fx-background-color:  " +
                        "#C0C0C0; -fx-background-radius:  15; " +
                        "-fx-border-color: transparent;");

        selectAnchorPane((AnchorPane) ((VBox) selectedAnchorPane.
                getParent()).getChildren().get(index - 1));
    }

    /**
     * A method to handle when up pressed in focus of card.
     *
     * @param event fine
     */
    public void keyECard(KeyEvent event) {
        Platform.runLater(() -> ((HBox) (((VBox) selectedAnchorPane.
                getChildren().get(0)).getChildren().get(1))).
                getChildren().get(0).requestFocus());
        ((TextField) ((HBox) (((VBox) selectedAnchorPane.
                getChildren().get(0)).getChildren().get(1))).
                getChildren().get(0)).setPromptText("");
        event.consume();
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
     * This method shows the help dialog when the "?" button is clicked
     */
    public void showHelp() {
        Optional<ButtonType> result = helpDialog.showAndWait();

        if (result.get().getButtonData() == ButtonBar.ButtonData.APPLY) {
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

        copyLabel.setVisible(true);
        // Start confirmation animation
        copyIDLabelInOut();
    }


    /**
     * Animation that starts when the user wants to copy the id of the current board
     * a timeline object is created that animates the opacity of the label
     * from 1 (fully visible) to 0 (fully transparent)
     * the duration of the animation is 3 seconds
     * when the animation is done, the visibility of the label is set to false and
     * resets the opacity to 1
     */
    public void copyIDLabelInOut() {
        // create a timeline animation to fade out the label after 3 seconds
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(copyLabel.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(3), new KeyValue(copyLabel.opacityProperty(), 0))
        );

        // when the animation finishes, hide the label and set the opacity to 1
        timeline.setOnFinished(event -> {
            copyLabel.setVisible(false);
            copyLabel.setOpacity(1);
        });

        timeline.play();
    }

    /**
     * @param blue  the rgb value of blue set from the database
     * @param green the rgb value of green set from the database
     * @param red   the rgb value of red set from the database
     */
    public void setColors(Double blue, Double green, Double red) {
        Board board = server.getBoardById(id);
        Color color = Color.color(board.getRed(), board.getGreen(), board.getBlue());

        // Set the background color of the AnchorPane to the RGB color value
        anchorPane.setStyle("-fx-background-color: " + toRgbCode(color) + ";");
    }

    /**
     * @param color conversion from rfb
     * @return the rgb code
     */
    public String toRgbCode(Color color) {
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
                Platform.runLater(this::columnsRefresh);
//            Platform.runLater(() ->
//                 setBoardTitle(boardTitle.getText() + " -- " + board.getId().toString()));
            Platform.runLater(() -> setColors(board.getBlue(), board.getGreen(), board.getRed()));
        });
        server.registerForMessages("/topic/update-title-in-board", Board.class, board -> {
            if (Objects.equals(board.getId(), id)) {
                Platform.runLater(this::refreshTitle);
            }
//            Platform.runLater(() ->
//                 setBoardTitle(boardTitle.getText() + " -- " + board.getId().toString()));
            Platform.runLater(() -> setColors(board.getBlue(), board.getGreen(), board.getRed()));
        });

        server.registerForMessages("/topic/update-labels-in-board", Board.class, board -> {
            if (Objects.equals(board.getId(), id)) {
                Platform.runLater(this::columnsRefresh);
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

    /**
     Gets the number of columns in the board.
     @return the number of columns in the board.
     */
    public Long getNrCol() {
        return nrCol;
    }
    /**

     Gets the server utility class.
     @return the ServerUtils object.
     */
    public ServerUtils getServer() {
        return server;
    }
    /**

     Gets the MainCtrl object associated with this BoardCtrl.
     @return the MainCtrl object.
     */
    public MainCtrl getMainCtrl() {
        return mainCtrl;
    }
    /**

     Gets the Dialog for deleting a board.
     @return the Dialog object for deleting a board.
     */
    public Dialog getDeleteBoardDialog() {
        return deleteBoardDialog;
    }
    /**

     Gets the Dialog for displaying help information.
     @return the Dialog object for displaying help information.
     */
    public Dialog getHelpDialog() {
        return helpDialog;
    }
    /**

     Gets the Dialog for customizing a card.
     @return the Dialog object for customizing a card.
     */
    public Dialog getCardCustomization() {
        return cardCustomization;
    }
    /**

     Gets the AnchorPane object associated with this BoardCtrl.
     @return the AnchorPane object.
     */
    public AnchorPane getAnchorPane() {
        return anchorPane;
    }
    /**

     Gets the Label for the board title.
     @return the Label object for the board title.
     */
    public Label getBoardTitleLabel() {
        return boardTitleLabel;
    }
    /**

     Gets the TextField for the board title.
     @return the TextField object for the board title.
     */
    public TextField getBoardTitle() {
        return boardTitle;
    }
    /**

     Gets the HBox for the board.
     @return the HBox object for the board.
     */
    public HBox getHbox() {
        return hbox;
    }
    /**

     Gets the Text object for the board's key ID.
     @return the Text object for the board's key ID.
     */
    public Text getKeyID() {
        return keyID;
    }
    /**

     Gets the ImageView object for the board's bin image.
     @return the ImageView object for the board's bin image.
     */
    public ImageView getBinImage() {
        return binImage;
    }

    /**

     Returns the copy label.
     @return the copy label
     */
    public Label getCopyLabel() {
        return copyLabel;
    }
    /**

     Returns the selected anchor pane.
     @return the selected anchor pane
     */
    public AnchorPane getSelectedAnchorPane() {
        return selectedAnchorPane;
    }
    /**

     Returns the bin contraction scale transition.
     @return the bin contraction scale transition
     */
    public ScaleTransition getBinContraction() {
        return binContraction;
    }
    /**

     Returns the bin expansion scale transition.
     @return the bin expansion scale transition
     */
    public ScaleTransition getBinExpansion() {
        return binExpansion;
    }
    /**

     Sets the number of columns to be displayed on the board.
     @param nrCol the number of columns to be displayed on the board
     */
    public void setNrCol(Long nrCol) {
        this.nrCol = nrCol;
    }
    /**

     Sets the title of the board.
     @param title the title of the board
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**

     Sets the delete board dialog.
     @param deleteBoardDialog the delete board dialog
     */
    public void setDeleteBoardDialog(Dialog deleteBoardDialog) {
        this.deleteBoardDialog = deleteBoardDialog;
    }
    /**

     Sets the help dialog.
     @param helpDialog the help dialog
     */
    public void setHelpDialog(Dialog helpDialog) {
        this.helpDialog = helpDialog;
    }
    /**

     Sets the card customization dialog.
     @param cardCustomization the card customization dialog
     */
    public void setCardCustomization(Dialog cardCustomization) {
        this.cardCustomization = cardCustomization;
    }
    /**

     Sets the ID of the board.
     @param id the ID of the board
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**

     Sets the AnchorPane of the BoardCard.
     @param anchorPane the AnchorPane of the BoardCard
     */
    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }
    /**

     Sets the Label that displays the board title of the BoardCard.
     @param boardTitleLabel the Label that displays the board title of the BoardCard
     */
    public void setBoardTitleLabel(Label boardTitleLabel) {
        this.boardTitleLabel = boardTitleLabel;
    }
    /**

     Sets the TextField that allows the user to edit the board title of the BoardCard.
     @param boardTitle the TextField that allows the user to edit the board title of the BoardCard
     */
    public void setBoardTitle(TextField boardTitle) {
        this.boardTitle = boardTitle;
    }
    /**

     Sets the HBox that contains the board key and copy button of the BoardCard.
     @param hbox the HBox that contains the board key and copy button of the BoardCard
     */
    public void setHbox(HBox hbox) {
        this.hbox = hbox;
    }
    /**

     Sets the Text that displays the board key of the BoardCard.
     @param keyID the Text that displays the board key of the BoardCard
     */
    public void setKeyID(Text keyID) {
        this.keyID = keyID;
    }
    /**

     Sets the ImageView that displays the bin icon of the BoardCard.
     @param binImage the ImageView that displays the bin icon of the BoardCard
     */
    public void setBinImage(ImageView binImage) {
        this.binImage = binImage;
    }
    /**

     Sets the Label that displays the copy status of the BoardCard.
     @param copyLabel the Label that displays the copy status of the BoardCard
     */
    public void setCopyLabel(Label copyLabel) {
        this.copyLabel = copyLabel;
    }
    /**

     Sets the AnchorPane of the selected BoardCard.
     @param selectedAnchorPane the AnchorPane of the selected BoardCard
     */
    public void setSelectedAnchorPane(AnchorPane selectedAnchorPane) {
        this.selectedAnchorPane = selectedAnchorPane;
    }
    /**

     Sets the ScaleTransition for the contraction animation of the bin icon.
     @param binContraction the ScaleTransition for the contraction animation of the bin icon
     */
    public void setBinContraction(ScaleTransition binContraction) {
        this.binContraction = binContraction;
    }
    /**

     Sets the ScaleTransition for the expansion animation of the bin icon.
     @param binExpansion the ScaleTransition for the expansion animation of the bin icon
     */
    public void setBinExpansion(ScaleTransition binExpansion) {
        this.binExpansion = binExpansion;
    }

    /**
     * This method set-ups the long polling tasks necessary
     * for auto-synchronization.
     */
    public void setUpLongPolling() {

        server.registerForCardUpdates(-1, card1 -> {
            Platform.runLater(() -> {
                columnsRefresh();

            });
        });
        server.registerForTaskUpdates(-1, list -> {
            Platform.runLater(() -> {
                columnsRefresh();
            });
        });
    }

    /**
     * This method halts all currently running tasks in
     * the executor instance.
     */
    public void resetLongPolling() {
        server.clearExecutor();
    }

    /**
     * This method shutdowns the executor instance that
     * handles long polling.
     */
    public void stopLongPolling() {
        server.stopCardUpdates();
    }

    /**
     * open colors
     */
    public void openColors() {
        mainCtrl.showColorManagment();
    }

}
