package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import javafx.scene.*;
import commons.Board;
import java.util.List;

public class MainOverviewCtrl {

    //Fields for the dependency injection
    private final ServerUtils server;
    private final MainCtrl mainCtrl;


    //The list of available boards in the server
    private List<Board> availableBoards;

    //Scene elements
    @FXML
    private ListView BoardsListElement;
    @FXML
    private Button disconnectButton;


    @Inject
    public MainOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void showOverview() {
        mainCtrl.showMainOverview();
    }


    /**The method is used to retrieve all the necessary Boards data from the server and refreshes
     * the properties of the scene elements to reflect the retrieved data.
     */
    public void refreshOverview(){

        //Retrieve all the available boards from the server
        //TODO Fix the get request for the boards.
        //availableBoards = server.getBoards();

        //Update the board list in the scene
        updateBoardsList(availableBoards);
    }


    /**
     * This method is used to update the list of available boards in the MainOverview scene by completely
     * replacing all the current items with a new list of items.
     * @param boards the new list of boards that will be displayed
     */
    public void updateBoardsList(List<Board> boards){

        //Check that there are boards in the list
        if(boards == null || boards.isEmpty()){
            return;
        }

        //Convert all the boards' title&id into an ObservableList
        ObservableList<String> content = FXCollections.observableArrayList();

        for(Board B : boards) {
            //The shortened String representation solely includes the title and the id of the Board.
            content.add(B.toStringShort());
        }

        //Set the items of the list element as the ObservableList.
        BoardsListElement.setItems(content);
    }


    /**The method is used by the disconnectButton to return to the ClientConnect scene, allowing the user
     * to connect to another server.
     */
    public void disconnect(){
        mainCtrl.showClientConnect();
    }
}
