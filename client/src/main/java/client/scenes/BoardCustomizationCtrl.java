package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardCustomizationCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Double blue = (double) -1;
    private Double green = (double) -1;
    private Double red = (double) -1;
    private String text;
    @FXML
    private ColorPicker colorPicker;


    /**
     * Constructs a new instance of the ClientConnectCtrl class with the specified
     * ServerUtils and MainCtrl objects injected as dependencies.
     *
     * @param server   the ServerUtils object to use for interacting with the server
     * @param mainCtrl the MainCtrl object to use for coordinating the application's
     *                 main control flow
     */
    @Inject
    public BoardCustomizationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * creates the connection and sets the URL
     *
     * @param address the URL that provides where the connection needs to be established
     */
    public void setConnection(String address) {
        server.setServerAddress(address);
    }

    /**
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * gets the color from the color picker
     */
    public void getColor() {
        this.blue = colorPicker.getValue().getBlue();
        this.green = colorPicker.getValue().getGreen();
        this.red = colorPicker.getValue().getRed();

    }

    /**
     * save the changes of board customziation
     */
    public void save() {
        if (blue != -1 && green != -1 && red != -1) {
            server.updateBoardColor(blue, green, red, mainCtrl.getBoardId());
        }
        server.send("/app/update-in-board", server.getBoardById(mainCtrl.getBoardId()));
    }

    /**
     * @param text title of the board
     */
    public void setBoardText(String text) {
        this.text = text;
    }

    /**
     * To get back to the boardOverview
     */
    @FXML
    private void getBackBoardCustomization() {
        mainCtrl.showBoardOverview(text, (double) 1, (double) 1, (double) 1);
    }


}
