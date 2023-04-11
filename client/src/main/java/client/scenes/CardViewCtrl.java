package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CardViewCtrl implements Initializable {
    private Double blue=1.0;
    private Double green=1.0;
    private Double red=1.0;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private String text;
    @FXML
    private Label longDescConstraint;
    @FXML
    private ColorPicker cardColor;
    @FXML
    private Label titleLabel;
    @FXML
    private TextArea longDescription;
    @FXML
    private VBox taskList;
    @FXML
    private Label emptyTaskList;
    @FXML
    private HBox taglist;
    @FXML
    private Label taskListCounter;
    private Dialog kickedDialog;

    private Card card;

    /**
     * @return the list of tags
     */
    public HBox getTagList() {
        return taglist;
    }


    //Drag-and-drop markers list
    private Separator closestMarker;
    private boolean isTaskDragged;
    private boolean isDraggedOverBin;


    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView longDescIcon;

    //Help box for the help functionality
    private Dialog helpDialog;

    //Scale Transition for BinImage contraction and expansion
    private ScaleTransition binContraction;
    private ScaleTransition binExpansion;
    private final int maximumLongDescriptionLength = 150;
    @FXML
    private ImageView binImage;

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

        //Set up the long description text area
        setUpLongDescription();
        longDescIcon.setImage(new Image("EditMode.png"));

        //Set up the Dialog Box for being kicked out of Card Overview
        kickedDialog = new Dialog<String>();
        kickedDialog.initModality(Modality.APPLICATION_MODAL);
        kickedDialog.setTitle("Card Was Deleted!");

        kickedDialog.setHeaderText("The card you were viewing was deleted!");
        kickedDialog.setContentText("You have been forcibly returned to the Board Overview!");

        Stage dialogStage = (Stage) kickedDialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image("BinImage.png"));

        ButtonType confirmBT = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);

        kickedDialog.getDialogPane().getButtonTypes().addAll(confirmBT);

        // Set up the dialog for the help button
        helpPopUp();
    }

    /**
     * A method that creates the labels for the ? button and shortcut
     *
     * @return list of labels
     */
    public ArrayList<Label> helpLabel() {
        ArrayList<Label> labels = new ArrayList<>();
        // Add each keyboard shortcut to the VBox
        labels.add(new Label("Shift+/ -> open help pop-up"));
        labels.add(new Label("Up/Down/Left/Right -> select tasks"));
        labels.add(new Label("Shift+Up/Down -> change order of cards in the column"));
        labels.add(new Label("E -> edit the card title"));
        labels.add(new Label("Del/Backspace -> delete a card"));
        labels.add(new Label("Enter -> open card details"));
        labels.add(new Label("Esc -> close card details"));
        labels.add(new Label("T -> open popup for adding tags"));
        labels.add(new Label("C -> open popup for color preset selection"));
        return labels;
    }

    /**
     * A method to return the list of labels to represent the help information for the drag and drop
     *
     * @return list of labels which includes information
     */
    public ArrayList<Label> helpDragDrop() {
        ArrayList<Label> labels = new ArrayList<>();
        // Add each keyboard shortcut to the VBox
        labels.add(new Label("To delete a subtask you can drag and drop it to the BIN"));
        labels.add(new Label("To rearrange subtasks you can drag and drop"));
        return labels;
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

// Create a TabPane to hold the keyboard shortcuts list and other tabs
        TabPane tabPane = new TabPane();
        tabPane.setTabMinWidth(Double.MAX_VALUE);
        tabPane.setTabMinWidth(Double.MAX_VALUE);
        tabPane.setTabMinHeight(50);
        tabPane.setTabMaxHeight(50);

        VBox shortcutsList = new VBox();
        shortcutsList.setSpacing(5);
        shortcutsList.setPadding(new Insets(15.0, 5.0, 5.0, 5.0));
        shortcutsList.getChildren().addAll(helpLabel());
        VBox dragAndDropList = new VBox();
        dragAndDropList.setSpacing(5);
        dragAndDropList.setPadding(new Insets(15.0, 5.0, 5.0, 5.0));
        dragAndDropList.getChildren().addAll(helpDragDrop());

        Tab shortcutsTab = new Tab("Keyboard Shortcuts", shortcutsList);
        tabPane.getTabs().add(shortcutsTab);

        Tab dragTab = new Tab("Drag and Drop Information", dragAndDropList);
        tabPane.getTabs().add(dragTab);
        tabPane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);


// Center and fill the TabPane
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setTabMinWidth(100);
        tabPane.setTabMaxWidth(Double.MAX_VALUE);
        tabPane.setTabMinHeight(30);
        tabPane.setTabMaxHeight(30);


        VBox.setVgrow(tabPane, Priority.ALWAYS);

// Add the TabPane to the dialog's content
        helpDialog.getDialogPane().setContent(tabPane);

// Add an OK button to the dialog
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        helpDialog.getDialogPane().getButtonTypes().add(okButtonType);
        // Add a listener to the scene to detect when the Shift+/ key combination is pressed
        anchorPane.setOnKeyPressed(event -> {
            int shiftColumnIndex = -1;
            if (event.isShiftDown() && event.getCode() == KeyCode.SLASH) {
                if (!(event.getTarget() instanceof TextField)) {
                    helpDialog.showAndWait();
                }
            }
            if (event.getCode() == KeyCode.ESCAPE && !(event.getTarget() instanceof TextArea)) {
                getBackCard();
            }
            if (event.getCode() == KeyCode.T && !(event.getTarget() instanceof TextArea)) {
                try {
                    getTagView();
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
     * To get back to the boardOverview
     */
    @FXML
    public void getBackCard() {
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
    public void getTagView() throws IOException {
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
        //Hide Long Description aiding visual elements
        longDescConstraint.setVisible(false);
        longDescIcon.setVisible(false);
    }

    /**
     * Resets and displays the available tasks of the current card
     * instance in the scene.
     */
    public void displayTasks() {

        //Update the counter label
        updateTaskListCounter();

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
    public Separator createTaskDropMarker() {
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
        titleField.setText("" + task.getTitle());
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

    private void recordTitleOnChange(TextField field) {

        field.setOnKeyTyped(event -> {


            int taskIndex = field.getParent().getParent()
                    .getChildrenUnmodifiable().indexOf(
                            field.getParent()
                    );
            taskIndex = (taskIndex - 1) / 2;

            field.getParent().getChildrenUnmodifiable().get(3).setVisible(
                    !field.getText().equals(card.getTaskList().get(taskIndex).getTitle()));

            event.consume();
        });

        field.setOnMouseClicked(event -> {
            if (event.getTarget() != field) {

                int taskIndex = field.getParent().getParent()
                        .getChildrenUnmodifiable().indexOf(
                                field.getParent()
                        );
                taskIndex = (taskIndex - 1) / 2;

                if (!field.getText().equals(card.getTaskList().get(taskIndex).getTitle())) {
                    field.setText(card.getTaskList().get(taskIndex).getTitle());
                    event.consume();
                }
            }

        });

        field.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {

                int taskIndex = field.getParent().getParent()
                        .getChildrenUnmodifiable().indexOf(
                                field.getParent()
                        );
                taskIndex = (taskIndex - 1) / 2;


                if (!field.getText().equals(card.getTaskList().get(taskIndex).getTitle())) {
                    card.getTaskList().get(taskIndex).setTitle(field.getText());
                    server.updateTask(card.getId(),card.getTaskList().get(taskIndex));
                    event.consume();
                }

            }
        });
    }

    private void recordSelectedOnChange(CheckBox checkBox) {

        checkBox.setOnAction(event -> {

            //Find the index of the corresponding task instance
            int taskIndex = checkBox.getParent().getParent()
                    .getChildrenUnmodifiable().indexOf(
                            checkBox.getParent()
                    );
            taskIndex = (taskIndex - 1) / 2;


            //Record the change in the status of the task, and ensure it persists in the server
            card.getTaskList().get(taskIndex).setStatus(checkBox.isSelected());
            server.updateTask(card.getId(), card.getTaskList().get(taskIndex));

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
                    addTaskBeforeInList((taskIndex - 1) / 2, markerIndex / 2);

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
    public void setDragForBin(Node bin) {
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
    public void displayTags() {
        HBox hBox = new HBox();
        if(card.getTags() == null){
            return;
        }

        for (Tag t : card.getTags()) {
            if (card.hasTagWithId(t.getTagID())) {
                AnchorPane anchorPane = new AnchorPane();
                TextField textField = new TextField(t.getTitle());
                textField.setEditable(false);
                textField.setPrefWidth(100);
                //textField.setMaxWidth(Double.MAX_VALUE);
                // set the maximum width of the text field
                textField.setMaxHeight(Double.MAX_VALUE);
                // set the maximum height of the text field
                Color color = Color.color(t.getFontRed(), t.getFontGreen(), t.getFontBlue());
                String rgbCode = toRgbCode(color);
                Color color2 = Color.color(t.getHighlightRed(),
                        t.getHighlightGreen(), t.getHighlightBlue());
                String rgbCode2 = toRgbCode(color2);
                textField.setStyle("-fx-text-fill: " + rgbCode +
                        "; -fx-background-color: " + rgbCode2 + ";");
                anchorPane.getChildren().add(textField);
                hBox.getChildren().add(anchorPane);


            }
        }

        mainCtrl.getcardViewCtrl().setCardViewCtrl(hBox);

    }

    /**
     * @param color conversion from rfb
     * @return the rgb code
     */
    public String toRgbCode(Color color) {
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);
        return String.format("#%02X%02X%02X", r, g, b);
    }


    /**
     * @param address of the server
     */
    public void setConnection(String address) {
        server.setServerAddress(address);
    }

    private void addTaskBeforeInList(int current, int before) {

        for (Task task : card.getTaskList()) {
            if (task.getPosition() == current) {
                if (current < before) {
                    task.setPosition(before - 1);
                } else {
                    task.setPosition(before);
                }
            } else if (task.getPosition() >= before && task.getPosition() < current) {
                task.setPosition(task.getPosition() + 1);
            } else if (task.getPosition() < before && task.getPosition() > current) {
                task.setPosition(task.getPosition() - 1);
            }

        }
        sortTasksByPosition();
        this.setCard(server.updateTaskList(card.getId(), card.getTaskList()));
        mainCtrl.setCard(card);

    }

    private void deleteCardInList(int position) {

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
        mainCtrl.setCard(card);

    }

    private void sortTasksByPosition() {
        List<Task> sortedList = card.getTaskList();
        sortedList.sort((o1, o2) -> (o1.getPosition() > o2.getPosition()) ?
                1 : (o1.getPosition() > o2.getPosition()) ? 0 : -1);
        card.setTaskList(sortedList);
    }

    /**
     * @param tagList to set in the cardviewctrl
     */
    public void setCardViewCtrl(HBox tagList) {
        taglist.getChildren().clear();
        this.taglist.getChildren().addAll(tagList);
    }

    /**
     * This method set-ups the long polling tasks necessary
     * for auto-synchronization.
     */
    public void setUpLongPolling(){

        server.registerForCardUpdates(card.getId(),card1 -> {
            Platform.runLater(()->{
                if(card1.getTitle() == null) {
                    kickedDialog.show();
                    mainCtrl.showBoardOverview(text, (double) 1, (double) 1, (double) 1);
                }else{
                    setCard(card1);
                    refresh();
                }

            });
        });
        server.registerForTaskUpdates(card.getId(), list -> {
            Platform.runLater(()->{
                card.setTaskList(list);
                refresh();
            });
        });
    }

    /**
     * This method halts all currently running tasks in
     * the executor instance.
     */
    public void resetLongPolling(){
        server.clearExecutor();
    }

    /**
     * This method shutdowns the executor instance that
     * handles long polling.
     */
    public void stopLongPolling(){
        server.stopCardUpdates();
    }
    private void setUpLongDescription(){

        longDescription.setOnKeyTyped(event ->{
            updateLongDescConstraintText();
            longDescIcon.setVisible(!longDescription.getText().equals(card.getDescription()));
            longDescConstraint.setVisible(!longDescription.getText().equals(card.getDescription()));
        });
        longDescription.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                var temp = longDescription.getText()
                        .replace("\n", "");
                longDescription.setText(
                        temp.substring(0,Math.min(temp.length(),maximumLongDescriptionLength)));
                longDescConstraint.setVisible(false);

                if(!longDescription.getText().equals(card.getDescription())){
                    card.setDescription(longDescription.getText());
                    server.updateCard(card);
                    event.consume();
                    longDescIcon.setVisible(false);
                }

            }
        });
    }

    private void updateLongDescConstraintText() {
        //Find the length of the current
        int length = longDescription.getText().length();

        //Update the SearchConstraintText label
        if (length < maximumLongDescriptionLength) {
            longDescConstraint.setText(
                    (maximumLongDescriptionLength - length) + " Characters Remaining.");
            longDescConstraint.setStyle("-fx-text-fill: green; -fx-text-weight: bold;");
        } else if (length == maximumLongDescriptionLength) {
            longDescConstraint.setText("Reached Maximum Length.");
            longDescConstraint.setStyle("-fx-text-fill: orange; -fx-text-weight: bold;");
        } else {
            longDescConstraint.setText("Exceeded Maximum Length");
            longDescConstraint.setStyle("-fx-text-fill: red;");

        }

    }

    private void updateTaskListCounter() {
        if (card.getTaskList() == null || card.getTaskList().isEmpty()) {

            taskListCounter.setText("No tasks are available.");

        } else if (card.getTaskList().size() == 1) {

            taskListCounter.setText("1 task is available.");
        } else {
            taskListCounter.setText(card.getTaskList().size() + " tasks are available.");
        }
    }
    
    /**
     * Getter for the long description TextArea.
     *
     * @return the long description TextArea
     */
    public TextArea getLongDescription() {
        return longDescription;
    }

    /**
     * Setter for the long description TextArea.
     *
     * @param longDescription the long description TextArea to set
     */
    public void setLongDescription(TextArea longDescription) {
        this.longDescription = longDescription;
    }

    /**
     * Getter for the ServerUtils object.
     *
     * @return the ServerUtils object
     */
    public ServerUtils getServer() {
        return server;
    }

    /**
     * Getter for the MainCtrl object.
     *
     * @return the MainCtrl object
     */
    public MainCtrl getMainCtrl() {
        return mainCtrl;
    }

    /**
     * Getter for the text string.
     *
     * @return the text string
     */
    public String getText() {
        return text;
    }

    /**
     * Getter for the title label.
     *
     * @return the title label
     */
    public Label getTitleLabel() {
        return titleLabel;
    }

    /**
     * Getter for the task list VBox.
     *
     * @return the task list VBox
     */
    public VBox getTaskList() {
        return taskList;
    }

    /**
     * Getter for the empty task list Label.
     *
     * @return the empty task list Label
     */
    public Label getEmptyTaskList() {
        return emptyTaskList;
    }

    /**
     * Getter for the tag list HBox.
     *
     * @return the tag list HBox
     */
    public HBox getTaglist() {
        return taglist;
    }

    /**
     * Getter for the Card object.
     *
     * @return the Card object
     */
    public Card getCard() {
        return card;
    }

    /**
     * Getter for the closest marker Separator.
     *
     * @return the closest marker Separator
     */
    public Separator getClosestMarker() {
        return closestMarker;
    }

    /**
     * Getter for the task dragged boolean.
     *
     * @return true if a task is being dragged, false otherwise
     */
    public boolean isTaskDragged() {
        return isTaskDragged;
    }

    /**
     * Getter for the dragged over bin boolean.
     *
     * @return true if a task is being dragged over the bin, false otherwise
     */
    public boolean isDraggedOverBin() {
        return isDraggedOverBin;
    }

    /**
     * Getter for the bin contraction ScaleTransition.
     *
     * @return the bin contraction ScaleTransition
     */
    public ScaleTransition getBinContraction() {
        return binContraction;
    }

    /**
     * Getter for the bin expansion ScaleTransition.
     *
     * @return the bin expansion ScaleTransition
     */
    public ScaleTransition getBinExpansion() {
        return binExpansion;
    }

    /**
     * Getter for the bin image ImageView.
     *
     * @return the bin image ImageView
     */
    public ImageView getBinImage() {
        return binImage;
    }

    /**
     * Setter for the title label.
     *
     * @param titleLabel the title label to set
     */
    public void setTitleLabel(Label titleLabel) {
        this.titleLabel = titleLabel;
    }

    /**
     * Setter for the task list VBox.
     *
     * @param taskList the task list VBox to set
     */
    public void setTaskList(VBox taskList) {
        this.taskList = taskList;
    }

    /**
     * Setter for the empty task list Label.
     *
     * @param emptyTaskList the empty task list Label to set
     */
    public void setEmptyTaskList(Label emptyTaskList) {
        this.emptyTaskList = emptyTaskList;
    }

    /**
     * Setter for the tag list HBox.
     *
     * @param taglist the tag list HBox to set
     */
    public void setTaglist(HBox taglist) {
        this.taglist = taglist;
    }

    /**
     * Sets the closest marker to the specified separator object.
     *
     * @param closestMarker the separator object to set as the closest marker
     */
    public void setClosestMarker(Separator closestMarker) {
        this.closestMarker = closestMarker;
    }

    /**
     * Sets whether a task is currently being dragged or not.
     *
     * @param taskDragged true if a task is being dragged, false otherwise
     */
    public void setTaskDragged(boolean taskDragged) {
        isTaskDragged = taskDragged;
    }

    /**
     * Sets whether a task is currently being dragged over the bin or not.
     *
     * @param draggedOverBin true if a task is being dragged over the bin, false otherwise
     */
    public void setDraggedOverBin(boolean draggedOverBin) {
        isDraggedOverBin = draggedOverBin;
    }

    /**
     * Sets the bin contraction scale transition.
     *
     * @param binContraction the bin contraction scale transition to set
     */
    public void setBinContraction(ScaleTransition binContraction) {
        this.binContraction = binContraction;
    }

    /**
     * Sets the bin expansion scale transition.
     *
     * @param binExpansion the bin expansion scale transition to set
     */
    public void setBinExpansion(ScaleTransition binExpansion) {
        this.binExpansion = binExpansion;
    }

    /**
     * Sets the bin image to the specified image view.
     *
     * @param binImage the image view to set as the bin image
     */
    public void setBinImage(ImageView binImage) {
        this.binImage = binImage;
    }

    /**
     * setter for colors
     */
    @FXML
    public void getColor() {
        this.blue = cardColor.getValue().getBlue();
        this.red = cardColor.getValue().getRed();
        this.green = cardColor.getValue().getGreen();
    }

    /**
     * save color in the db
     */
    @FXML
    void saveColor() {
        card.setColor(this.blue, this.green, this.red);
        Board board = server.getBoardById(mainCtrl.getBoardId());
        Long colId = Long.valueOf(-1);
        for (Column c : board.getColumns()) {
            for (Card cardCheck : c.getCards()) {
                if (cardCheck.getId() == card.getId()) {
                    colId = c.getId();
                    break;
                }
            }
        }
        server.updateCardInColumnColor(card.getId(), card, colId, mainCtrl.getBoardId());
        server.send("/app/update-in-board", server.getBoardById(mainCtrl.getBoardId()));

    }
}
