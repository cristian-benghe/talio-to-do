package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


import java.net.URL;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {
    private Long ind= Long.valueOf(0);

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
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
        Board board=server.getBoards().get(Math.toIntExact(nr-1));
        if(board.getTitle().equals("New Board")){
            board_title.setText("Board "+nr);
        }
        else {
            board_title.setText(server.getBoards().get(Math.toIntExact(nr-1)).getTitle());
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

    public void addOneColumn(String title){
        AnchorPane anchorPaneVBox=new AnchorPane();
        ScrollPane scrollPane=new ScrollPane();
        VBox vBox=new VBox();
        scrollPane.setContent(vBox);
        vBox.setPrefHeight(380);
        vBox.setPrefWidth(150);
        vBox.getChildren().add(new TextField(title));
        anchorPaneVBox.getChildren().add(vBox);
        setColumnDragDropDeletion(anchorPaneVBox);  //to set the functionality of the drag and drop of the new column. Only for deletion not to replace!
        hbox.getChildren().add(anchorPaneVBox);

    }

    /**
     * This method sets the needed properties of the deletion of the columns.
     * @param column is column to set property of deletion
     */
    private void setColumnDragDropDeletion(AnchorPane column) {

        //set the drag of the specific column
        column.setOnDragDetected(event -> {
            Dragboard dragboard = column.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString("Deletion");
            dragboard.setContent(clipboardContent);
            event.consume();
        });

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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addOneColumn("To do");
        addOneColumn("Doing");
        addOneColumn("Done");
    }
}
