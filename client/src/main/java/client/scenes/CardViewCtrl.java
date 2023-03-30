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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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

    //Drag-and-drop markers list
    private Separator closestMarker;
    private boolean isTaskedDragged;

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

        //Initialize the dragged task flag
        isTaskedDragged = false;

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

    /**
     * Changes the Scene to the TagView Scene
     */
    @FXML
    private void getTagView()
    {
        mainCtrl.showTagView();
    }

    /**
     * Sets the card that the CardView scene currently inspects
     * @param card the new Card instance to be inspected
     */
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

            //Add a separator marker
            taskList.getChildren().add(createTaskDropMarker());

            //Add the task pane to the task list
            taskList.getChildren().add(createTaskBox(task));

        }
        //Add a terminal marker
        taskList.getChildren().add(createTaskDropMarker());
    }

    /**
     * Create and customizes the separator marker that can be used in
     * the task list. The marker is already set with the relevant
     * drag-and-drop event handlers.
     * @return a newly created task list marker separator
     */
    private Separator createTaskDropMarker(){
        //Add a separator marker
        Separator marker = new Separator();
        marker.setPrefHeight(6);
        marker.setOrientation(Orientation.HORIZONTAL);
        setMarkerFeedback(marker);
        return marker;
    }

    /**
     * Creates a new HBox that will display a particular given
     * task that can be used in the task list. The HBox is
     * already set with the relevant drag-and-drop
     * event handlers.
     * @param task the task that this HBox will represent
     * @return the newly created HBox representing the given task
     */
    private HBox createTaskBox(Task task){

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

        //Add drag detection
        setTaskDraggable(task,taskRoot);

        taskRoot.getChildren().add(titleLabel);



        return taskRoot;
    }

    /**
     * Sets the drag-and-drop event handlers for a particular task
     * and its representing HBox.
     *
     * @param task the given task
     * @param taskBox the represnting HBox of the given task
     */
    private void setTaskDraggable(Task task, HBox taskBox){
        taskBox.setOnDragDetected(event -> {

            //Raise the dragged flag
            isTaskedDragged = true;

            //Reset the current closest marker by considering
            //the closest of the two adjacent separators
            int taskIndex = taskList.getChildren().indexOf(taskBox);

            changeClosestMarker ((Separator) taskList.getChildren().get(taskIndex+1));

            //Add a dragged visual to the taskBox separator
            Separator sideSeparator = (Separator) taskBox.getChildren().get(0);
            sideSeparator.setStyle("-fx-background-color: gray");

            Dragboard dragboard = taskBox.startDragAndDrop(TransferMode.ANY);
            ClipboardContent clipboardContent = new ClipboardContent();

            //Use the corresponding task ID as the transferred content
            clipboardContent.putString(""+task.getID());
            dragboard.setContent(clipboardContent);
            event.consume();
        });
        taskBox.setOnDragDone(event -> {
            //Do the task rearrangement
            int taskIndex = taskList.getChildren().indexOf(taskBox);
            int markerIndex = taskList.getChildren().indexOf(closestMarker);

            if(taskIndex + 1 != markerIndex) {
                taskList.getChildren().remove(taskIndex + 1);

                Node temp = taskList.getChildren().remove(taskIndex);
                markerIndex = taskList.getChildren().indexOf(closestMarker);

                taskList.getChildren().add(markerIndex + 1, temp);
                taskList.getChildren().add(markerIndex + 2, createTaskDropMarker());
            }

            //Remove the dragged visual to the taskBox separator
            Separator sideSeparator = (Separator) taskBox.getChildren().get(0);
            sideSeparator.setStyle("-fx-background-color: null");

            //Reset the closest marker
            changeClosestMarker(null);
            //Lower the drag task flag
            isTaskedDragged = false;
            event.consume();
        });
    }


    /**
     * Sets the drag-and-drop event handlers for the given
     * task list marker separator.
     * @param marker the given marker separator
     */
    private void setMarkerFeedback(Separator marker){

        marker.setOnDragEntered(event ->{

            //Check that the ClipBoard has String content
            if(event.getGestureSource() != marker
                    && event.getDragboard().hasString()){

                changeClosestMarker(marker);
            }

            event.consume();
        });
    }

    /**
     * Updates the current closest marker separator. Update only occurs
     * if the isTaskDragged flag is raised.
     * @param newMarker the new marker separator
     */
    private void changeClosestMarker(Separator newMarker){

        //Check whether a task is currently dragged.
        if(!isTaskedDragged){
            return;
        }

        //Restore the previous closest marker if it exists
        if(closestMarker != null) {
            //Change the marker's visuals
            closestMarker.setStyle("-fx-background-color: white;" +
                    "-fx-pref-height: 6");
        }

        //Store the marker
        closestMarker = newMarker;

        //Add a visual marking to the new closest marker if it exists
        if(closestMarker != null) {
            //Change the marker's visuals
            closestMarker.setStyle("-fx-background-color: cyan;" +
                    "-fx-pref-height: 6");
        }
    }

    /**
     * Resets and displays the available tags of the current card
     * instance in the scene.
     */
    public void displayTags(){


    }
}
