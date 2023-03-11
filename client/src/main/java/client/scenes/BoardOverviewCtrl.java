package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
public class BoardOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    //id of the board
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
        //this should set a default title for boards that are not new but haven't been modified either
        //or set the title to the title of the board object with an ID
//        Board board=server.getBoards().get((int) (this.id-1));
//
//        if(board.getTitle().isBlank()){
//            board_title.setText("Board"+id);
//        }
//        else {
//            board_title.setText(server.getBoards().get((int) (nr-1)).getTitle());
//        }

    }
    //when you edit text it should update the title
    public void edit_title(){
        server.updateBoardTitle(this.id, board_title.getText());
    }
    //when you click on home button you go back to mainOverview
    public void go_back_home(){
        mainCtrl.showMainOverview();
    }
}
