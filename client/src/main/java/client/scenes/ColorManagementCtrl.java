package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;


public class ColorManagementCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private ColorPicker boardColor;

    @FXML
    private CheckBox checkBoxBoard;

    @FXML
    private CheckBox checkBoxColumn;

    @FXML
    private ColorPicker columnColor;
    @FXML
    private CheckBox reset;


    /**
     * go back to board
     */
    @FXML
    public void back() {
        mainCtrl.showBoardOverview(mainCtrl.getBoardCtrl().getTitle(), 1.0, 1.0, 1.0);
        checkBoxColumn.setSelected(false);
        checkBoxBoard.setSelected(false);
    }

    /**
     * save changed for the selected colors
     */
    @FXML
    public void save() {
        server.updateBoardColor(boardColor.getValue().getBlue(),
                boardColor.getValue().getGreen(), boardColor.getValue().getRed()
                , mainCtrl.getBoardId());
        server.send("/app/update-in-board", server.getBoardById(mainCtrl.getBoardId()));
        server.updateAllColumnsInBoard(columnColor.getValue().getRed(),
                columnColor.getValue().getGreen(),
                columnColor.getValue().getBlue(), mainCtrl.getBoardId());
        server.send("/app/update-in-board", server.getBoardById(mainCtrl.getBoardId()));
        server.updateColumnColorBoard(columnColor.getValue().getRed(),
                columnColor.getValue().getGreen(),
                columnColor.getValue().getBlue(), mainCtrl.getBoardId());
        server.send("/app/update-in-board", server.getBoardById(mainCtrl.getBoardId()));
    }

    /**
     * reset checkbox for column and board if reset is selected
     */
    @FXML
    public void resetCheckboxes(){
        if(reset.isSelected()){
            checkBoxBoard.setSelected(false);
            checkBoxColumn.setSelected(false);
            boardColor.setValue(Color.color(1, 1, 1));
            columnColor.setValue(Color.color(1, 1, 1));
        }
    }

    /**
     * set color to default for board
     */
    @FXML
    public void defaultBoard(){
        if(checkBoxBoard.isSelected()){
            boardColor.setValue(Color.color(1, 1, 1));
            reset.setSelected(false);
        }
    }

    /**
     * set color to default for column
     */
    @FXML public void defaultColumn(){
        if(checkBoxColumn.isSelected()){
            columnColor.setValue(Color.color(1, 1, 1));
            reset.setSelected(false);
        }
    }
    /**
     * set default false if you select a column color
     */
    @FXML public void selectColumnColor(){
        checkBoxColumn.setSelected(false);
    }

    /**
     * set default false if you select a board color
     */
    @FXML
    public void selectBoardColor(){
        checkBoxBoard.setSelected(false);
    }
    /**
     * Initialize the controller and the scene
     * @param server server parameter
     * @param mainCtrl mainController parameter to access scenes and methods
     */
    @Inject
    public ColorManagementCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * @return column color
     */
    public ColorPicker getColumnColor(){
        return columnColor;
    }

    /**
     * Function implemented to use/load certain functions when the TagView scene is shown
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    /**
     * set colorPickers to default before entering colorm scene
     */
    public void setColorPick() {
        boardColor.setValue(Color.color(1, 1, 1));
        columnColor.setValue(Color.color(1, 1, 1));
    }

    /**
     * set color of color picker before entering the scene
     */
    public void setColorPickColumn() {
        reset.setSelected(false);
        checkBoxColumn.setSelected(false);
        checkBoxBoard.setSelected(false);
        Board board=server.getBoardById(mainCtrl.getBoardId());
        boardColor.setValue(Color.color(board.getRed(),
                board.getGreen(), board.getBlue()));
        columnColor.setValue(Color.color(board.getColumnRed(),
                board.getColumnGreen(), board.getColumnBlue()));

    }
}