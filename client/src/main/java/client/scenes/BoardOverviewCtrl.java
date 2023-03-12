package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class BoardOverviewCtrl {
    private Long ind= Long.valueOf(0);

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    //id of the board
    private Long id= Long.valueOf(-1);
    List<ListView>list=new ArrayList<>();
    @FXML
    private TextField keyID;
    @FXML
    private TextField board_title;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ListView<String> toDo=new ListView<>();
    @FXML
    private Pane pane;


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
        if(!idd.contains("New Board")) {
            board_title.setText(idd.split("-")[0]);
            return;
        }
        Long nr=Long.parseLong(idd.substring(13));
        String val= idd.substring(13);
        keyID.setText(val);
        this.id=nr;
        //this should set a default title for boards that are not new but haven't been modified either
        //or set the title to the title of the board object with an ID
        Board board=server.getBoards().get(Math.toIntExact(nr-1));
        System.out.println(board.getTitle());
        if(board.getTitle().isBlank()){
            board_title.setText("Board"+idd);
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
        ListView<String> l = new ListView<>();
        l.setOrientation(Orientation.HORIZONTAL);
        list.add(l);
        pane.getChildren().add(l);
        ind++;
        scrollPane.setContent(pane);
    }
}
