package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class TagTemplateCtrl implements Initializable {
    private final ServerUtils server;

    private final MainCtrl mainCtrl;
    private Long tagId= Long.valueOf(0);
    private Long boardId;
    private Double fontRed;
    private Double fontBlue;
    private Double fontGreen;
    private Double highlightRed;
    private Double highlightBlue;
    private Double highlightGreen;
    @FXML
    private CheckBox checkbox;

    @FXML
    private Button deleteButton;

    @FXML
    private ColorPicker font;

    @FXML
    private ColorPicker highlight;

    @FXML
    private TextField titlee;
    private Card card;


    @FXML
    void setFont() {
        this.fontRed=font.getValue().getRed();
        this.fontBlue=font.getValue().getBlue();
        this.fontGreen=font.getValue().getGreen();
        Tag tag=server.getTagById(tagId);
        tag.setFontColor(fontRed, fontGreen, fontBlue);
        server.updateTagInBoard(Math.toIntExact(tagId), tag, boardId);
        server.updateTag(tagId, tag);
        setFontColors(fontBlue, fontGreen, fontRed);
    }

    @FXML
    void setHighlight() {
        this.highlightRed=highlight.getValue().getRed();
        this.highlightBlue=highlight.getValue().getBlue();
        this.highlightGreen=highlight.getValue().getGreen();
        Tag tag=server.getTagById(tagId);
        tag.setHighlightColor(highlightBlue, highlightGreen, highlightRed);
        server.updateTagInBoard(Math.toIntExact(tagId), tag, boardId);
        server.updateTagTitle(Math.toIntExact(tagId),this.titlee.getText());
        setHighlightColor(highlightBlue, highlightGreen, highlightRed);

    }

    public Double getHighlightBlue() {
        return highlightBlue;
    }

    public Double getHighlightGreen() {
        return highlightGreen;
    }

    public Double getHighlightRed() {
        return highlightRed;
    }

    public void setFontColors(Double blue, Double green, Double red) {
        Tag tag = server.getTagById(tagId);
        Color color = Color.color(tag.getFontRed(), tag.getFontGreen(), tag.getFontBlue());
        // Set the background color of the AnchorPane to the RGB color value
        String rgbCode = toRgbCode(color);
        Color color2 = Color.color(tag.getHighlightRed(), tag.getHighlightGreen(), tag.getHighlightBlue());
        String rgbCode2 = toRgbCode(color2);
        titlee.setStyle("-fx-text-fill: " + rgbCode + "; -fx-background-color: " + rgbCode2 + ";");
        setFont(red, blue, green);
    }
    public void setHighlightColor(Double blue, Double green, Double red) {
        Tag tag = server.getTagById(tagId);
        Color color = Color.color(tag.getFontRed(), tag.getFontGreen(), tag.getFontBlue());
        // Set the background color of the AnchorPane to the RGB color value
        String rgbCode = toRgbCode(color);
        Color color2 = Color.color(tag.getHighlightRed(), tag.getHighlightGreen(), tag.getHighlightBlue());
        String rgbCode2 = toRgbCode(color2);
        titlee.setStyle("-fx-text-fill: " + rgbCode + "; -fx-background-color: " + rgbCode2 + ";");
        setFont(red, blue, green);
    }
    /**
     * @param color conversion from rfb
     * @return the rgb code
     */
    private String toRgbCode(Color color) {
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);
        return String.format("#%02X%02X%02X", r, g, b);
    }
    @FXML
    void setTitle() {
    }

    @FXML
    void setCheckbox() {
//        HBox tagList = mainCtrl.getcardViewCtrl().getTagList();
//        System.out.println(title.getText());
//        if (checkbox.isSelected()) {
//            TextField tagTitle = new TextField(title.getText());
//            tagList.getChildren().add(tagTitle);
//        } else {
//            tagList.getChildren().removeIf(node -> node instanceof TextField);
//        }
//        mainCtrl.getcardViewCtrl().setCardViewCtrl(tagList);
    }



    /**
     * Initialize the controller and the scene
     * @param server server parameter
     * @param mainCtrl mainController parameter to access scenes and methods
     */
    @Inject
    public TagTemplateCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }


    /**
     * @param tagID set the id of the tag
     */
    public void setTagId(Long tagID) {
        this.tagId = tagID;
        //this.titlee.setText(server.getTagById(tagID).getTitle());
    }
    @FXML
    private void deleteTag() throws IOException {
        System.out.println(tagId);
        Node tagNode = deleteButton.getParent();
        AnchorPane parent = (AnchorPane) tagNode.getParent();
        int index = parent.getChildren().indexOf(tagNode);
        parent.getChildren().remove(index);    //remove the tag from the scene
        server.deleteTagFromBoard(tagId, boardId);  //remove tag from server
        //first delete tag from board, then from tags(convention)
        server.deleteTag(tagId);
        mainCtrl.getTagViewCtrl().refreshtaglist();
        //when deleting a tag, refresh the list of tags from tagviewctrl
        //so that it sets them in the right position
    }

    /**
     * @param server address
     */
    public void setConnection(String server) {
        this.server.setServerAddress(server);
    }

    /**
     * @param boardId id of the board
     */
    public void setBoardId(Long boardId) {
        this.boardId=boardId;
    }

    /**
     * @return the id of the tag set in the template
     */
    public Long getTagId() {
        return tagId;
    }

    /**
     * @param title of the tag
     */
    public void setTitleOfTag(String title) {
        this.titlee.setText(title);
    }

    /**
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //this.titlee.setText("New Tag");
    }

    public void setCard(Card card) {
        this.card=card;
    }

    public String getText() {
        return titlee.getText();
    }

    public boolean getCheckBox() {
        return checkbox.isSelected();
    }

    public void addTitle() {
        this.titlee.setOnKeyTyped(event -> {
                Tag tag=server.getTagById(tagId);
                tag.setTitle(this.titlee.getText());
                server.updateTagInBoard(Math.toIntExact(tagId), tag, boardId);
                server.updateTagTitle(Math.toIntExact(tagId),this.titlee.getText());
            });
    }

    public void setFont(Double fontRed, Double fontBlue, Double fontGreen){
        this.fontGreen=fontGreen;
        this.fontBlue=fontBlue;
        this.fontRed=fontRed;
    }
    public void setHighlight(Double highlightRed, Double highlightBlue, Double highlightGreen){
        this.highlightBlue=highlightBlue;
        this.highlightRed=highlightRed;
        this.highlightGreen=highlightGreen;
    }
}