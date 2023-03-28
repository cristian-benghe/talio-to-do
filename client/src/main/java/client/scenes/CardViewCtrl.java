package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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
    @FXML
    private VBox taskList;
    @FXML
    private Label emptyTaskList;

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

        //Clear the task list
        taskList.getChildren().clear();

        //Check if the task list is empty
        if(card.getTaskList() == null || card.getTaskList().isEmpty()){

            //Enable and show the foreground empty message
            emptyTaskList.setVisible(true);
            emptyTaskList.setDisable(false);
            return;
        }

        //Disable and high the foreground empty message
        emptyTaskList.setVisible(false);
        emptyTaskList.setDisable(true);

        //Create the pane for each task
        for(Task task : card.getTaskList()){

            //Create the main pane for the task
            HBox taskRoot = new HBox();
            taskRoot.setPrefSize(488,20);
            taskRoot.setPadding(new Insets(10));
            taskRoot.setAlignment(Pos.CENTER_LEFT);


            //Create the draggable vertical separator for the task
            Separator separator = new Separator();
            separator.setOrientation(Orientation.VERTICAL);
            separator.setPadding(new Insets(0,3,0,3));
            taskRoot.getChildren().add(separator);

            //Create the checkbox for the task
            CheckBox completeBox = new CheckBox();
            completeBox.setSelected(task.isComplete());
            taskRoot.getChildren().add(completeBox);

            //Create the title label for the task
            Label titleLabel = new Label();
            titleLabel.setText(task.getTitle());
            titleLabel.setFont(new Font(18d));
            titleLabel.setPadding(new Insets(0,0,0,10));
            taskRoot.getChildren().add(titleLabel);

            //Add the task pane to the task list
            taskList.getChildren().add(taskRoot);
        }
    }

    /**
     * Resets and displays the available tags of the current card
     * instance in the scene.
     */
    public void displayTags(){


    }
}
