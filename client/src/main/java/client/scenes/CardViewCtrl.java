package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class CardViewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Card card;
    private String text;
    @FXML
    private Button cardViewBack;
    @FXML
    private Label titleLabel;
    @FXML
    private TextArea longDescription;

    /**
     * Initialize the controller and the scene
     * @param server server parameter
     * @param mainCtrl mainController parameter to access scenes and methods
     */
    @Inject
    public CardViewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * The method initializes this instance.
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Initialize the card
        card = null;

    }

    /**
     * To get back to the boardOverview
     */
    @FXML
    private void getBackCard()
    {
        System.out.println(text);
        mainCtrl.showBoardOverview(text, (double) 1, (double) 1, (double) 1);
    }

    /**
     * @param text taken from the board overview and set back when returning to the board
     */
    public void setText(String text){
        this.text=text;
    }

    @FXML
    private void getTagView()
    {
        mainCtrl.showTagView();
    }

    public void setCard(Card card){this.card=card;}


    /**
     * Refreshes all the modifiable visual elements in the card scene
     * in accordance with the current card instance.
     */
    public void refresh(){

        //Reset the title label
        titleLabel.setText(card.getTitle());

        //Reset the long description text area
        longDescription.setText(card.getDescription());

        //Reset the task and tag lists
        displayTasks();
        displayTags();

    }

    /**
     * Resets and displays the available tasks of the current card
     * instance in the scene.
     */
    public void displayTasks(){

    }

    /**
     * Resets and displays the available tags of the current card
     * instance in the scene.
     */
    public void displayTags(){


    }
}
