package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.Task;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.io.IOException;

public class CardViewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Card card;
    private String text;
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
    private boolean isTaskDragged;
    private boolean isDraggedOverBin;

    @FXML
    private ImageView binImage;

    //Scale Transition for BinImage contraction and expansion
    private ScaleTransition binContraction;
    private ScaleTransition binExpansion;


    /**
     * Initialize the controller and the scene
     *
     * @param server   server parameter
     * @param mainCtrl mainController parameter to access scenes and methods
     */
    @Inject
    public CardViewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * The method initializes this instance.
     *
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
        isTaskDragged = false;
        isDraggedOverBin = false;

        //Set the expansion and contraction animations
        binExpansion = new ScaleTransition();
        binExpansion.setDuration(Duration.millis(800));
        binExpansion.setNode(binImage);
        binExpansion.setInterpolator(Interpolator.EASE_IN);

        binExpansion.setToX(2);
        binExpansion.setToY(2);

        binContraction = new ScaleTransition();
        binContraction.setDuration(Duration.millis(800));
        binContraction.setNode(binImage);
        binContraction.setInterpolator(Interpolator.EASE_IN);

        binContraction.setToX(1);
        binContraction.setToY(1);

        binImage.setImage(new Image("BinImage.png"));
        setDragForBin(binImage);
    }

    /**
     * To get back to the boardOverview
     */
    @FXML
    private void getBackCard() {
        mainCtrl.showBoardOverview(text, (double) 1, (double) 1, (double) 1);
    }

    /**
     * @param text taken from the board overview and set back when returning to the board
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Changes the Scene to the TagView Scene
     */
    @FXML
    private void getTagView() throws IOException {
        mainCtrl.showTagView();
    }

    /**
     * Sets the card that the CardView scene currently inspects
     *
     * @param card the new Card instance to be inspected
     */
    public void setCard(Card card) {
        this.card = card;
        this.card.setTaskList(server.getAllTasksByCardId(card.getId()));
    }

    /**
     * Refreshes all the modifiable visual elements in the card scene
     * in accordance with the current card instance.
     */
    public void refresh() {

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
    public void displayTasks() {

        //Clear the task list
        taskList.getChildren().clear();

        //Check if the task list is empty
        if (card.getTaskList() == null || card.getTaskList().isEmpty()) {

            //Enable and show the foreground empty message
            emptyTaskList.setVisible(true);
            emptyTaskList.setDisable(false);
            return;
        }

        //Disable and high the foreground empty message
        emptyTaskList.setVisible(false);
        emptyTaskList.setDisable(true);


        //Sort the tasks by position
        sortTasksByPosition();

        //Create the pane for each task
        for (Task task : card.getTaskList()) {

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
     *
     * @return a newly created task list marker separator
     */
    private Separator createTaskDropMarker() {
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
     *
     * @param task the task that this HBox will represent
     * @return the newly created HBox representing the given task
     */
    private HBox createTaskBox(Task task) {

        //Create the main pane for the task
        HBox taskRoot = new HBox();
        taskRoot.setPrefSize(488, 20);
        taskRoot.setPadding(new Insets(10));
        taskRoot.setAlignment(Pos.CENTER_LEFT);

        //Create the draggable vertical separator for the task
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        separator.setPadding(new Insets(0, 3, 0, 3));
        taskRoot.getChildren().add(separator);

        //Create the checkbox for the task
        CheckBox completeBox = new CheckBox();
        completeBox.setSelected(task.getStatus());
        recordSelectedOnChange(completeBox);
        taskRoot.getChildren().add(completeBox);

        //Create the title label for the task
        TextField titleField = new TextField();
        titleField.setText(""+task.getTitle());
        titleField.setFont(new Font(18d));
        titleField.setPadding(new Insets(0, 0, 0, 10));
        recordTitleOnChange(titleField);
        taskRoot.getChildren().add(titleField);

        //Create a new edit mode image
        ImageView editImage = new ImageView();
        editImage.setImage(new Image("EditMode.png"));
        editImage.setFitHeight(18d);
        editImage.setFitWidth(18d);
        taskRoot.setPadding(new Insets(5));
        editImage.setVisible(false);
        taskRoot.getChildren().add(editImage);

        //Add drag detection
        setTaskDraggable(task, taskRoot);




        return taskRoot;
    }

    private void recordTitleOnChange(TextField field){

        field.setOnKeyTyped(event -> {


            int taskIndex = field.getParent().getParent()
                    .getChildrenUnmodifiable().indexOf(
                            field.getParent()
                    );
            taskIndex = (taskIndex-1)/2;

            field.getParent().getChildrenUnmodifiable().get(3).setVisible(
                    !field.getText().equals(card.getTaskList().get(taskIndex).getTitle()));

            event.consume();
        });

        field.setOnMouseClicked(event -> {
            if(event.getTarget() != field){

                int taskIndex = field.getParent().getParent()
                        .getChildrenUnmodifiable().indexOf(
                                field.getParent()
                        );
                taskIndex = (taskIndex-1)/2;

                if(!field.getText().equals(card.getTaskList().get(taskIndex).getTitle())) {
                    field.setText(card.getTaskList().get(taskIndex).getTitle());
                    event.consume();
                }
            }

        });

        field.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){

                int taskIndex = field.getParent().getParent()
                        .getChildrenUnmodifiable().indexOf(
                                field.getParent()
                        );
                taskIndex = (taskIndex-1)/2;


                if(!field.getText().equals(card.getTaskList().get(taskIndex).getTitle())) {
                    card.getTaskList().get(taskIndex).setTitle(field.getText());
                    server.updateTask(card.getTaskList().get(taskIndex));
                    event.consume();
                }

            }
        });
    }

    private void recordSelectedOnChange(CheckBox checkBox){

        checkBox.setOnAction(event -> {

            System.out.println("ping");
            //Find the index of the corresponding task instance
            int taskIndex = checkBox.getParent().getParent()
                    .getChildrenUnmodifiable().indexOf(
                            checkBox.getParent()
                    );
            taskIndex = (taskIndex-1)/2;


            //Record the change in the status of the task, and ensure it persists in the server
            card.getTaskList().get(taskIndex).setStatus(checkBox.isSelected());
            server.updateTask(card.getTaskList().get(taskIndex));

        });

    }

    /**
     * Sets the drag-and-drop event handlers for a particular task
     * and its representing HBox.
     *
     * @param task    the given task
     * @param taskBox the representing HBox of the given task
     */
    private void setTaskDraggable(Task task, HBox taskBox) {
        taskBox.setOnDragDetected(event -> {

            //Raise the dragged flag
            isTaskDragged = true;

            //Reset the current closest marker by considering
            //the closest of the two adjacent separators
            int taskIndex = taskList.getChildren().indexOf(taskBox);
            changeClosestMarker((Separator) taskList.getChildren().get(taskIndex + 1));
            //Add a dragged visual to the taskBox separator
            Separator sideSeparator = (Separator) taskBox.getChildren().get(0);
            sideSeparator.setStyle("-fx-background-color: gray");
            //Use the corresponding task ID as the transferred content
            Dragboard dragboard = taskBox.startDragAndDrop(TransferMode.ANY);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString("" + task.getPosition());
            dragboard.setContent(clipboardContent);
            event.consume();
        });
        taskBox.setOnDragDone(event -> {

            //Check for task deletion
            if (!isDraggedOverBin) {
                //Do the task rearrangement
                int taskIndex = taskList.getChildren().indexOf(taskBox);
                int markerIndex = taskList.getChildren().indexOf(closestMarker);


                if (taskIndex + 1 != markerIndex) {

                    //Swap the list positions
                    addTaskBeforeInList((taskIndex-1)/2,markerIndex/2);

                    Node temp = deleteTaskFromList(taskBox);
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
                isTaskDragged = false;
            } else {
                isDraggedOverBin = false;
            }
            event.consume();
        });
    }

    /**
     * Sets the drag-and-drop event handlers for the given
     * task list marker separator.
     *
     * @param marker the given marker separator
     */
    private void setMarkerFeedback(Separator marker) {

        marker.setOnDragEntered(event -> {

            //Check that the ClipBoard has String content
            if (event.getGestureSource() != marker
                    && event.getDragboard().hasString()) {

                changeClosestMarker(marker);
            }

            event.consume();
        });
    }

    /**
     * Updates the current closest marker separator. Update only occurs
     * if the isTaskDragged flag is raised.
     *
     * @param newMarker the new marker separator
     */
    public void changeClosestMarker(Separator newMarker) {

        //Check whether a task is currently dragged.
        if (!isTaskDragged) {
            return;
        }

        //Restore the previous closest marker if it exists
        if (closestMarker != null) {
            //Change the marker's visuals
            closestMarker.setStyle("-fx-background-color: null;" +
                    "-fx-pref-height: 6");
        }

        if (!isDraggedOverBin) {
            //Store the marker
            closestMarker = newMarker;


            //Add a visual marking to the new closest marker if it exists
            if (closestMarker != null) {
                //Change the marker's visuals
                closestMarker.setStyle("-fx-background-color: cyan;" +
                        "-fx-pref-height: 6");
            }
        }
    }

    /**
     * This method begins the bin expansion animation for the drag-and-drop dragOver Event
     * for the bin image.
     */
    public void expandBin() {
        binContraction.stop();
        binExpansion.stop();
        binExpansion.play();
    }

    /**
     * This method begins the bin contraction animation for the drag-and-drop dragOver Event
     * * for the bin image.
     */
    public void contractBin() {
        binContraction.stop();
        binExpansion.stop();
        binContraction.play();
    }

    /**
     * Sets the drag-and-drop event handlers for the binImage delete functionalities for
     * tasks.
     *
     * @param bin the binImage instance that will be assigned the event handlers
     */
    private void setDragForBin(Node bin) {
        bin.setOnDragOver(event -> {
            if (event.getGestureSource() != bin
                    && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        bin.setOnDragDropped(event -> {
            if (event.getGestureSource() != bin
                    && event.getDragboard().hasString()) {
                contractBin();
                if (isTaskDragged) {
                    isTaskDragged = false;

                    //Determine the ID of the task to be deleted
                    Dragboard dragboard = event.getDragboard();
                    int position = Integer.parseInt(
                            (String) dragboard.getContent(DataFormat.PLAIN_TEXT));

                    //Remove the task from the Card's task list
                    deleteCardInList(position);

                    //Remove the task from the task list.
                    deleteTaskFromList((HBox) event.getGestureSource());
                }
            }
            event.consume();
        });

        bin.setOnDragEntered(event -> {
            if (event.getGestureSource() != bin
                    && event.getDragboard().hasString()) {
                if (isTaskDragged) {
                    expandBin();
                    isDraggedOverBin = true;
                    changeClosestMarker(null);
                }
            }
            event.consume();
        });
        bin.setOnDragExited(event -> {
            if (event.getGestureSource() != bin
                    && event.getDragboard().hasString()) {
                if (isTaskDragged) {
                    contractBin();
                    isDraggedOverBin = false;
                    changeClosestMarker(closestMarker);
                }
            }
            event.consume();
        });
    }

    /**
     * Deletes a given task HBox and a consecutive separator marker from the
     * task list VBox. Returns the deleted task HBox.
     *
     * @param task the task HBox to be deleted
     * @return the deleted task HBox
     */
    private HBox deleteTaskFromList(HBox task) {

        if (card.getTaskList().isEmpty()) {
            displayTasks();
            return task;
        }

        int index = taskList.getChildren().indexOf(task);
        taskList.getChildren().remove(index + 1);
        return (HBox) taskList.getChildren().remove(index);
    }

    /**
     * Handles the addition of a new Task to the list in both the
     * UI and in the server.
     */
    public void addNewTask() {

        Task task = new Task(card.getTaskList().size(), "New task!", false);
        card.getTaskList().add(server.addTask(card.getId(), task));
        refresh();

    }

    /**
     * Resets and displays the available tags of the current card
     * instance in the scene.
     */
    private void displayTags() {


    }

    /**
     * @param address of the server
     */
    public void setConnection(String address) {
        server.setServerAddress(address);
    }

    private void addTaskBeforeInList(int current, int before){

        for(Task task : card.getTaskList()){
            if(task.getPosition() == current){
                if(current < before){
                    task.setPosition(before-1);
                }else {
                    task.setPosition(before);
                }
            }
            else if(task.getPosition() >= before && task.getPosition() < current){
                task.setPosition(task.getPosition()+1);
            }
            else if(task.getPosition() < before && task.getPosition() > current){
                task.setPosition(task.getPosition()-1);
            }

        }
        sortTasksByPosition();
        this.setCard(server.updateTaskList(card.getId(),card.getTaskList()));
    }

    private void deleteCardInList(int position){

        //Remove the specified card
        card.getTaskList().remove(position);

        //Decrement any greater subsequent positions
        for (Task task : card.getTaskList()) {
            if (task.getPosition() > position) {
                task.setPosition(task.getPosition() - 1);
            }
        }

        //Make changes persist in the DB
        sortTasksByPosition();
        this.setCard(server.updateTaskList(card.getId(), card.getTaskList()));

    }
    private void sortTasksByPosition(){
        List<Task> sortedList = card.getTaskList();
        sortedList.sort((o1, o2) -> (o1.getPosition() > o2.getPosition()) ?
                1 : (o1.getPosition() > o2.getPosition()) ? 0 : -1);
        card.setTaskList(sortedList);
        System.out.println(card.getTaskList().toString());

    }


}
