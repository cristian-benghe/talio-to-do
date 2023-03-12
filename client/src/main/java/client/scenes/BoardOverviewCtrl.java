package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Column;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class BoardOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    //id of the board
    private Long id= Long.valueOf(-1);
    @FXML
    private TextField keyID;
    @FXML
    private TextField board_title;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView tableView;

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
        System.out.println(idd);
        if(!idd.contains("New Board")) {
            board_title.setText(idd);
            return;
        }
        Long nr=Long.parseLong(idd.substring(13));
        String val= idd.substring(13);
        keyID.setText(val);
        this.id=nr;
        //this should set a default title for boards that are not new but haven't been modified either
        //or set the title to the title of the board object with an ID
        Board board=server.getBoards().get(Math.toIntExact(nr));

        if(board.getTitle().isBlank()){
            board_title.setText("Board"+idd);
        }
        else {
            board_title.setText(server.getBoards().get(Math.toIntExact(nr)).getTitle());
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
        TableColumn<String, String> newColumn = new TableColumn<>("New Column");
        newColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        newColumn.setPrefWidth(100);
        tableView.getColumns().add(newColumn);
    }
}
