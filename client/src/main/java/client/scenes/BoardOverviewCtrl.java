package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
public class BoardOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Long id= Long.valueOf(-1);
    @FXML
    private TextField keyID;
    @FXML
    private TextField board_title;

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void showOverview() {
        mainCtrl.showBoardOverview("");
    }
    //the title is not yet stored correctly in the database
    public void setBoard_title(String id){
        
        Long nr=Long.parseLong(id.substring(13));
        String val= id.substring(13);
        keyID.setText(val);
        this.id=nr;
//        Board board=server.getBoards().get((int) (this.id-1));
//
//        if(board.getTitle().isBlank()){
//            board_title.setText("Board"+id);
//        }
//        else {
//            board_title.setText(server.getBoards().get((int) (nr-1)).getTitle());
//        }

    }

    public void edit_title(){
        server.updateBoardTitle(this.id, board_title.getText());
    }
    public void go_back_home(){
        mainCtrl.showMainOverview();
    }
}
