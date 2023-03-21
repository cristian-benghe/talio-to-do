package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainOverviewCtrl implements Initializable {

    //Useful constants

    public static final int SEARCH_MAX_LENGTH = 25;

    //Fields for the dependency injection
    private final ServerUtils server;
    private final MainCtrl mainCtrl;


    //The list of available boards in the server
    private List<Board> availableBoards;

    //Scene elements
    @FXML
    private ListView boardsListElement;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label SearchConstraintText;
    @FXML
    private Label BoardsText;
    @FXML
    private Label EmptyBoardListMsg;

    @Inject
    public MainOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void showOverview() {
        mainCtrl.showMainOverview();
    }


    /**
     * The method is used to refresh all the elements of the MainOverview.
     */
    public void refreshOverview() {

        //Reset the availableBoards list
        availableBoards = server.getBoards();

        //Update the board list in the scene
        updateBoardsList();

        //Clear the searchTextField
        searchTextField.setText("");

        //Update the labels
        updateSearchConstraintText();
        updateBoardsText();
    }


    /**
     * This method is used to update the list of available
     * boards in the MainOverview scene by completely
     * replacing all the current items with a new list of items.
     */
    public void updateBoardsList() {

        //De-focus the ListView

        //Update the BoardsConstraintText Label
        updateBoardsText();

        //Check that there are boards in the list
        if (availableBoards == null || availableBoards.isEmpty()) {
            EmptyBoardListMsg.setVisible(true);
            boardsListElement.setItems(null);
            return;
        }
        EmptyBoardListMsg.setVisible(false);

        //Convert all the boards' title&id into an ObservableList
        ObservableList<String> content = FXCollections.observableArrayList();

        for (Board board : availableBoards) {
            //The shortened String representation solely includes the title and the id of the Board.
            content.add(board.toStringShort());
        }

        //Set the items of the list element as the ObservableList.
        boardsListElement.setItems(content);
    }


    /**
     * The method is used by the disconnectButton
     * to return to the ClientConnect scene, allowing the user
     * to connect to another server.
     */
    public void disconnect() throws Exception {
        mainCtrl.showClientConnect();
    }

    /**
     * Updates the label SearchConstraintText in
     * accordance to the current length of the input
     * in the search TextField. The label itself is
     * used to display whether the search input has reached
     * or exceeded the maximum length constraint.
     */
    public void updateSearchConstraintText() {
        //Find the length of the current
        int length = searchTextField.getText().length();

        //Update the SearchConstraintText label
        if (length < SEARCH_MAX_LENGTH) {
            SearchConstraintText.setText((SEARCH_MAX_LENGTH - length) + " Characters Remaining.");
            SearchConstraintText.setStyle("-fx-text-fill: green; -fx-text-weight: bold;");
        } else if (length == SEARCH_MAX_LENGTH) {
            SearchConstraintText.setText("Reached Maximum Length.");
            SearchConstraintText.setStyle("-fx-text-fill: orange; -fx-text-weight: bold;");
        } else {
            SearchConstraintText.setText("Exceeded Maximum Length By "
                    + (length - SEARCH_MAX_LENGTH) + ".");
            SearchConstraintText.setStyle("-fx-text-fill: red;");

        }

    }

    /**
     * Updates the BoardsText label through
     * the current list of available boards. The label notifies the
     * user of the total number of available boards.
     */
    public void updateBoardsText() {

        //Check that there are boards in the list
        if (availableBoards == null || availableBoards.isEmpty()) {
            //Display that there are no available boards
            BoardsText.setText("No Available Boards");
            return;
        }

        //Display the number of available boards
        BoardsText.setText(availableBoards.size() + " Available Boards");

    }

    /**
     * This method is called upon when the user
     * presses the Search button to search for a board using
     * either a name or a key through the input in the searchTextField.
     */
    public void searchBoard() {

        //Retrieve the search input from the searchTextField
        String input = searchTextField.getText();

        //Make sure that the
        if (input.length() > SEARCH_MAX_LENGTH) {

            //TODO Add an error message through a ??? pop-up (to improve usability)
            return;
        }

        //TODO Retrieve boards through key input or name input
        //TODO ??? Add a pop-up window to display all of the retrieved boards?
    }

    /**
     * This method is used in order to create a new board.
     * The method first creates a new Board instance,
     * and posts it to the server. Then, it retrieves
     * the board with the generated ID in order to update
     * the list of available boards.
     */
    public void createBoard() {

        //Create a new board with a generic title.
        Board board = new Board("New Board", null, null);
        System.out.println("\n\n\n" + board.getId() + "\n\n\n");
        //Post the new board to the server
        //TODO Fix the POST method for board!
        server.addBoard(board);
        refreshOverview();
//        //TODO Retrieve the new board from the server to determine the board's ID.
//        //board = server.();
//        //As a temporary measure, set ID as 0
//        //board.setId(0L);
//        System.out.println("\n\n\n" + board.getId() + "\n\n\n");
//        //Add the new board to the availableBoards list
//        availableBoards.add(board);
//
//
//
//        updateBoardsList(availableBoards);
    }

    public void on_board_click() {
        String selectedBoardStr = (String) boardsListElement.getSelectionModel().getSelectedItem();
        if (selectedBoardStr == null) {
            return;
        }

        // Navigate to the board view for the selected board
        mainCtrl.showBoardOverview(selectedBoardStr);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setConnection(String address) {
        server.setServerAddress(address);
    }
}
