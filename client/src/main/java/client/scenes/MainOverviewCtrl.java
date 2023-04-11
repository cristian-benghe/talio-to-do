package client.scenes;

import client.BoardData;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.NotFoundException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainOverviewCtrl implements Initializable {

    //Useful constants

    private final int searchMaxLength = 60;

    //Fields for the dependency injection
    private final ServerUtils server;

    private final MainCtrl mainCtrl;


    //The list of available boards in the server (for admin)
    private List<Board> availableBoards;

    //The list of available boards for the user
    private List<Board> availableUserBoards;

    //Scene elements
    @FXML
    private ListView boardsListElement;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label searchConstraintText;
    @FXML
    private Label boardsText;
    @FXML
    private Label emptyBoardListMsg;
    @FXML
    private Label labelMessage;

    @FXML
    private AnchorPane anchorPane;

    private AnchorPane selectedAnchorPane;

    //Help box for the help functionality
    private Dialog helpDialog;

    /**
     * Constructs a new instance of the MainOverviewCtrl class with the
     * specified ServerUtils and MainCtrl objects injected as dependencies.
     *
     * @param server   the ServerUtils object to use for interacting with the server
     * @param mainCtrl the MainCtrl object to use for coordinating the application's
     *                 main control flow
     */
    @Inject
    public MainOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Displays MainOverview Scene on the screen
     */
    public void showOverview() {
        mainCtrl.showMainOverview();
    }


    /**
     * The method is used to refresh all the elements of the MainOverview.
     */
    public void refreshOverview() {
        labelMessage.setVisible(false);

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
        if (!mainCtrl.isHasAdminRole()) {
            if (availableUserBoards == null || availableUserBoards.isEmpty()) {
                emptyBoardListMsg.setVisible(true);
                boardsListElement.setItems(null);
                return;
            }
        }else  {
            if (availableBoards == null || availableBoards.isEmpty()) {
                emptyBoardListMsg.setVisible(true);
                boardsListElement.setItems(null);
                return;
            }
        }
        emptyBoardListMsg.setVisible(false);
        //Convert all the boards' title&id into an ObservableList
        ObservableList<String> content = FXCollections.observableArrayList();

        if (mainCtrl.isHasAdminRole())
            for (Board board : availableBoards) {
                //The shortened String representation
                // solely includes the title and the id of the Board.
                content.add(board.toStringShort());
            }
        else {
            if (availableUserBoards == null)
                availableUserBoards = new ArrayList<>();
            for (Board board : availableUserBoards) {
                //The shortened String representation solely
                // includes the title and the id of the Board.
                content.add(board.toStringShort());
            }
        }
        //Set the items of the list element as the ObservableList.
        boardsListElement.setItems(content);

        /*
         * Lambda expression that adds delete board button
         * for each line in the Main Overview list view
         */
        boardsListElement.setCellFactory(lv -> new ListCell<String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    if (mainCtrl.isHasAdminRole()) {
                        Button deleteButton = new Button("Delete");
                        // This lambda expression deletes the row (board) from the list view
                        deleteButton.setOnAction(event -> {
                            // Call a function to remove the board from the server.
                            server.send("/app/delete-board",
                                    Long.parseLong(item.split("--")[1].trim()));

                            // Remove the item from the list view.
                            getListView().getItems().remove(item);
                            updateBoardsText();
                        });
                        // this line makes the delete button active in the client application
                        setGraphic(deleteButton);
                        // this line adjusts the size of the button based on the layout of the row
                        deleteButton.setManaged(true);
                        // this line makes the deleteBoard viewable
                        deleteButton.setVisible(true);
                        updateBoardsText();
                    } else {
                        Button removeButton = new Button("Remove");
                        // This lambda expression removes the row (board) from the list view
                        removeButton.setOnAction(event -> {
                            // Call a function to remove the board from the server.
                            Board toBeRemoved = null;
                            for (Board b : availableUserBoards)
                                if (Objects.equals(b.getId(),
                                        Long.parseLong(item.split("--")[1].trim()))) {
                                    toBeRemoved = b;
                                }
                            availableUserBoards.remove(toBeRemoved);
                            refreshWorkspaceFile();
                            updateBoardsList();
                        });
                        updateBoardsText();
                        // this line makes the remove button active in the client application
                        setGraphic(removeButton);
                        // this line adjusts the size of the button based on the layout of the row
                        removeButton.setManaged(true);
                        // this line makes the removeBoard viewable
                        removeButton.setVisible(true);
                    }
                }
                setText(item);
            }
        });
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
     * Updates the label searchConstraintText in
     * accordance to the current length of the input
     * in the search TextField. The label itself is
     * used to display whether the search input has reached
     * or exceeded the maximum length constraint.
     */
    public void updateSearchConstraintText() {
        //Find the length of the current
        int length = searchTextField.getText().length();

        //Update the SearchConstraintText label
        if (length < searchMaxLength) {
            searchConstraintText.setText((searchMaxLength - length) + " Characters Remaining.");
            searchConstraintText.setStyle("-fx-text-fill: green; -fx-text-weight: bold;");
        } else if (length == searchMaxLength) {
            searchConstraintText.setText("Reached Maximum Length.");
            searchConstraintText.setStyle("-fx-text-fill: orange; -fx-text-weight: bold;");
        } else {
            searchConstraintText.setText("Exceeded Maximum Length By "
                    + (length - searchMaxLength) + ".");
            searchConstraintText.setStyle("-fx-text-fill: red;");

        }

    }

    /**
     * Updates the boardsText label through
     * the current list of available boards. The label notifies the
     * user of the total number of available boards.
     */
    public void updateBoardsText() {

        //Check that there are boards in the list
        if (availableBoards == null || availableBoards.isEmpty()) {
            //Display that there are no available boards
            boardsText.setText("No Available Boards");
            return;
        }

        //Display the number of available boards
        if (availableUserBoards == null) availableUserBoards = new ArrayList<>();

        if (mainCtrl.isHasAdminRole())
            boardsText.setText(availableBoards.size() + " Available Boards");
        else
            boardsText.setText(availableUserBoards.size() + " Available Boards");
    }

    /**
     * This method is called upon when the user
     * presses the Search button to search for a board using
     * either a name or a key through the input in the searchTextField.
     */
    public void searchBoard() {

        //Retrieve the search input from the searchTextField
        String input = searchTextField.getText();
        labelMessage.setVisible(true);

        //Make sure that the
        if (input.length() > searchMaxLength) {
            labelMessage.setText("The input is too long");
            //TODO Add an error message through a ??? pop-up (to improve usability)
            return;
        }
        if (notNumber(input)) {
            labelMessage.setText("The input is not a valid ID");
            return;
        }
        try {
            int nr = Integer.parseInt(input);

            if (existsBoard(nr) == null) {
                labelMessage.setText("The board with the given ID doesn't exist");
                return;
            }
            String text = existsBoard(nr);
            if (availableUserBoards == null) availableUserBoards = new ArrayList<>();

            if (!mainCtrl.isHasAdminRole()) {
                Board toBeAdded = server.getBoardById(nr);
                availableUserBoards.add(toBeAdded);
                removeDuplicates(toBeAdded);
                refreshWorkspaceFile();
                server.send("/app/refresh", 10);
            }
            mainCtrl.showBoardOverview((text + " -- " + nr), (double) 1, (double) 1, (double) 1);
        } catch (NumberFormatException e) {
            labelMessage.setText("The board with the given ID doesn't exist");
        }
        //TODO Retrieve boards through key input or name input
        //TODO ??? Add a pop-up window to display all of the retrieved boards?
    }

    private String existsBoard(int nr) {
        List<Board> boards = server.getBoards();
        for (Board board : boards) {
            if (board.getId() == nr) {
                return board.getTitle();
            }
        }
        return null;
    }

    private boolean notNumber(String input) {
        for (int i = 0; i < input.length(); i++) {
            Character c = input.charAt(i);
            if (!(c >= '0' && c <= '9')) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method is used in order to create a new board.
     * The method first creates a new Board instance,
     * and posts it to the server. Then, it retrieves
     * the board with the generated ID in order to update
     * the list of available boards.
     */
    public void createBoard() {
        mainCtrl.setColorManagementDefault();
        //Create a new board with a generic title.
        Board board = new Board("New Board", null, null);
        //Post the new board to the server
        refreshOverview();
        if (!mainCtrl.isHasAdminRole())
            refreshWorkspaceFile();
        refreshOverview();

        server.send("/app/boards", board);
        if (server.getBoards().isEmpty())
            Platform.runLater(() ->
            {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Board board1 = server.getBoards().get(server.getBoards().size() - 1);
                availableUserBoards.add(board1);

                refreshOverview();
                if (!mainCtrl.isHasAdminRole())
                    refreshWorkspaceFile();
                refreshOverview();
                //server.send("/app/refresh", 10);

                if (!mainCtrl.isHasAdminRole())
                    refreshWorkspaceFile();
            });
        else
        {
            Board board1 = server.getBoards().get(server.getBoards().size() - 1);
            availableUserBoards.add(board1);

            refreshOverview();
            if (!mainCtrl.isHasAdminRole())
                refreshWorkspaceFile();
            refreshOverview();
            //server.send("/app/refresh", 10);

            if (!mainCtrl.isHasAdminRole())
                refreshWorkspaceFile();

        }

    }

    /**
     * Function used to join a board and change scene to the
     * selected board's overview
     */
    public void onBoardClick() {
        String selectedBoardStr = (String) boardsListElement.getSelectionModel().getSelectedItem();
        if (selectedBoardStr == null) {
            return;
        }


        if (!mainCtrl.isHasAdminRole()) {

            refreshWorkspaceFile();
            //server.send("/app/refresh", 10);
            //refreshWorkspaceFile();
        }
        // Navigate to the board view for the selected board
        mainCtrl.showBoardOverview(selectedBoardStr, (double) 1, (double) 1, (double) 1);

    }

    /**
     * Function implemented to use/load certain functions when the MainOverview scene is shown
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up the dialog for the help button
        helpPopUp();
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

        // Create a VBox to hold the keyboard shortcuts list
        VBox shortcutsList = new VBox();
        shortcutsList.setSpacing(5);

        // Add each keyboard shortcut to the VBox
        Label helpPopUpMessage = new Label("Shift+/ -> open help pop-up");
        Label upDownLeftRight = new Label("Up/Down/Left/Right -> select tasks");
        Label shiftUpDown = new Label("Shift+Up/Down -> change order of cards in the column");
        Label editCardTitle = new Label("E -> edit the card title");
        Label deleteCard = new Label("Del/Backspace -> delete a card");
        Label openCardDetails = new Label("Enter -> open card details");
        Label closeCardDetails = new Label("Esc -> close card details");
        Label addTags = new Label("T -> open popup for adding tags");
        Label colorPresetSelection = new Label("C -> open popup for color preset selection");

        // Add the keyboard shortcuts to the VBox
        shortcutsList.getChildren().addAll(helpPopUpMessage, upDownLeftRight,
                shiftUpDown, editCardTitle, deleteCard,
                openCardDetails, closeCardDetails, addTags, colorPresetSelection);

        // Add the VBox to the dialog's content
        helpDialog.getDialogPane().setContent(shortcutsList);

        // Add an OK button to the dialog
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        helpDialog.getDialogPane().getButtonTypes().add(okButtonType);

        // Add a listener to the scene to detect when the Shift+/ key combination is pressed
        anchorPane.setOnKeyPressed(event -> {
            if (event.isShiftDown() && event.getCode() == KeyCode.SLASH) {
                if (!(event.getTarget() instanceof TextField)) {
                    helpDialog.showAndWait();
                }
            }
        });
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
     * Method to be called one time for the websockets to
     * start.
     */
    public void socketsCall() {

        server.registerForMessages("/topic/delete-board", Long.class, id -> {
            Board toBeDeleted = null;
            for (Board b : availableBoards)
                if (Objects.equals(b.getId(), id)) toBeDeleted = b;
            if (toBeDeleted != null) {
                // System.out.println(availableBoards+" "+availableUserBoards);
                availableBoards.remove(toBeDeleted);
                availableUserBoards.remove(toBeDeleted);
            }
            Platform.runLater(this::refreshOverview);
        });
        server.registerForMessages("/topic/boards", Board.class, board -> {
            availableBoards.add(board);
//            if (!mainCtrl.isHasAdminRole()) {
//                if (availableUserBoards == null) availableUserBoards = new ArrayList<>();
//                availableUserBoards.add(board);
//            }
            Platform.runLater(this::refreshOverview);
        });

        server.registerForMessages("/topic/update-board", Board.class, board -> {
            for (Board b : availableBoards)
                if (Objects.equals(b.getId(), board.getId()))
                    b.setTitle(board.getTitle());
            for (Board b : availableUserBoards) {
                if (Objects.equals(b.getId(), board.getId())) {
                    b.setTitle(board.getTitle());
                }
            }
            Platform.runLater(this::refreshWorkspaceFile);
            Platform.runLater(this::refreshOverview);
        });

    }

    /**
     * method used for to refresh the availableUserBoards from the file
     */
    public void loadUserWorkspace() {

        BoardData boardData = new BoardData();

        try {
            // Deserialize the map from the file
            boardData.deserializeBoardData("client//src//main//resources//ClientData.data");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (boardData != null) {
            // Get the board list for a particular IP address
            if (boardData.getBoardMap().containsKey(server.getServer()))
                availableUserBoards = boardData.getBoardList(server.getServer());
            else availableUserBoards = new ArrayList<>();
        } else {
            availableUserBoards = new ArrayList<>();
        }


        if (availableUserBoards.size() == 1) {
            try {
                server.getBoardById(availableUserBoards.get(0).getId());
            } catch (NotFoundException e) {
                availableUserBoards = new ArrayList<>();
            }

        } else {

            Iterator<Board> iterator = availableUserBoards.iterator();
            while (iterator.hasNext()) {
                Board board = iterator.next();
                try {
                    server.getBoardById(board.getId());
                } catch (NotFoundException e) {
                    iterator.remove();
                }
            }
        }
        refreshWorkspaceFile();
    }

    /**
     * Method that removes all the duplicates of an Object that appear
     * after the first occurrence of it
     * @param board to have duplicates removed
     */
    public void removeDuplicates(Board board) {
        boolean first = true;
        Iterator<Board> iterator = availableUserBoards.iterator();
        while (iterator.hasNext()) {
            Board item = iterator.next();
            if (item.equals(board) && !first) {
                iterator.remove();
            } else if (item.equals(board) && first) {
                first = false;
            }
        }
    }

    /**
     * method used for to refresh the file from the availableUserBoards
     */
    public void refreshWorkspaceFile() {
        BoardData boardData = new BoardData();

        try {
            // Deserialize the map from the file
            boardData.deserializeBoardData("client//src//main//resources//ClientData.data");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        boardData.getBoardMap().put(server.getServer(), availableUserBoards);

        try {
            boardData.serializeBoardData("client//src//main//resources//ClientData.data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * set up the connection to a certain URL
     *
     * @param address the URL provided through a String
     */
    public void setConnection(String address) {
        server.setServerAddress(address);
    }

    /**
     * getter for the server
     *
     * @return the instance of the ServerUtils class
     */
    public ServerUtils getServer() {
        return server;
    }
}
