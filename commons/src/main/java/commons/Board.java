package commons;


import javax.persistence.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Board implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private Double red=1.0;
    private Double green=1.0;
    private Double blue=1.0;

    @OneToMany( cascade = CascadeType.ALL)
    private List<Column> columns = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE,
        CascadeType.REFRESH, CascadeType.DETACH})
    private List<Tag> tags = new ArrayList<>();
    private double columnRed=1.0;
    private double columnGreen=1.0;
    private double columnBlue=1.0;

    /**
     * Constructs a new Board object with the specified title, columns, and tags.
     *
     * @param title the title of the new Board object
     * @param columns the columns of the new Board object
     * @param tags the tags of the new Board object
     */
    public Board(String title, List<Column> columns, List<Tag>tags) {
        this.title = title;
        this.columns = columns;
        this.tags=tags;
        this.setColor((double) 1, (double) 1, (double) 1);
    }

    /**
     * Constructs a new Board object with the specified title
     * @param title the title of the new Board object
     */
    public Board(String title) {
        this.title = title;
        columns = new ArrayList<>();
        tags = new ArrayList<>();
    }

    /**
     * Constructs a new Board object with the specified ID, title, columns, and tags.
     *
     * @param id the ID of the new Board object
     * @param title the title of the new Board object
     * @param columns the columns of the new Board object
     * @param tags the tags of the new Board object
     */
    public Board(long id, String title, List<Column> columns, List<Tag>tags) {
        this.id = id;
        this.title = title;
        this.columns = columns;
        this.tags=tags;
    }

    /**
     * Empty constructor for the Board
     */
    public Board(){}


    /**
     * Get method for Id attribute
     * @return the ID of this Board object
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of this Board object.
     * @param id the new ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get method for the title attribute
     * @return the title of this Board object
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param newColumn to add to a boad
     */
    public void addColumn(Column newColumn) {
        columns.add(newColumn);
    }

    /**
     * @param columnID = the id of the column in a board
     * @param column = set a column with a new title
     */
    public void setColumn(int columnID, Column column) {
        columns.set(columnID, column);
    }

    /**
     * Set method for the title
     * @param title the new title to set
     */

    /**
     * @param newTag to add to a borad
     */
    public void addTag(Tag newTag) {
        tags.add(newTag);
    }

    /**
     * @param tagID = the id of the tag in a board
     * @param tag = set a tag with a new title
     */
    public void setTag(int tagID, Tag tag) {
        tags.set(tagID, tag);
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get method to get the list of columns of the board
     * @return List of columns
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * Set method to set the list of columns of the board
     * @param columns columns the new list of columns to set
     */
    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    /**
     * Get method to get the list of tags of the board
     * @return List of tags
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Set method to set the list of columns of the board
     * @param tags tags the new list of tags to set
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * A needed method to updateColumnArrangement
     * @param boardSource
     * @param boardTmp
     * @return A board with copied columns
     */
    public Board copyBoard(Board boardSource, Board boardTmp)
    {
        boardTmp.columns = new ArrayList<>(boardSource.getColumns());
        return boardTmp;
    }


    /**
     * The method creates a shortened human-friendly String representation of the object.
     * Only the title and id are used in this shortened representation.
     * @return a shortened human-friendly String representation.
     */
    public String toStringShort(){
        return this.title + " -- " + this.id;
    }
    /**
     * Equals method of the board object
     * @param o what to compare with
     * @return true or false according to equality.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;

        Board board = (Board) o;

        if (getId() != null ? !getId().equals(board.getId()) :
                board.getId() != null) return false;
        if(getTitle()!=null?!getTitle().equals(board.getTitle()):board.getTitle() != null)return false;
        if(getColumns()!=null?!getColumns().equals(board.getColumns()):board.getColumns()!=null)return false;
        return getTags()!= null?getTags().equals(board.getTags()):board.getTags() == null;
    }

    /**
     * Hash method for the board
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getColumns() != null ? getColumns().hashCode() : 0);
        result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
        return result;
    }
    /**
     * @param colInd delete a column with an id
     */
    public void deleteColumn(int colInd) {
        columns.remove(colInd);
    }

    /**
     * @param colInd the updated id of the columns that shift to the left
     */
    public void updateColIndex(int colInd) {
        for(int i=colInd;i<columns.size();i++){
            columns.get(i).setIDinBoard(columns.get(i).getIDinBoard()-1);
        }
    }

    /**
     * @param red from rgb
     * @param green from rgb
     * @param blue from rgb
     */
    public void setColor(Double red, Double green, Double blue){
        this.red=red;
        this.green=green;
        this.blue=blue;
    }

    /**
     * @return the red double value in rgb
     */
    public Double getRed() {
        return red;
    }

    /**
     * @return the green double value in rgb
     */
    public Double getGreen() {
        return green;
    }
    /**
     * @return the blue double value in rgb
     */
    public Double getBlue() {
        return blue;
    }

    /**
     * @param tagInd the id of the tag
     */
    public void deleteTag(int tagInd) {
        tags.remove(tagInd);
    }

    /**
     * @param i the id of the tag to be removed in the board
     */
    public void removeTag(int i) {
        tags.remove(i);
    }

    /**
     * @param ind the index of the tag to update
     * @param tag the new tag
     */
    public void updateTag(int ind, Tag tag) {
        tags.set(ind, tag);
    }

    /**
     * @param red rgb value of red
     * @param green  rgb value of green
     * @param blue rgb value of blue
     */
    public void setColorColumn(double red, double green, double blue) {
        this.columnRed=red;
        this.columnGreen=green;
        this.columnBlue=blue;
    }

    /**
     * @return rgb value of red
     */
    public double getColumnRed() {
        return columnRed;
    }

    /**
     * @return rgb value of green
     */
    public double getColumnGreen() {
        return columnGreen;
    }

    /**
     * @return rgb value of blue
     */
    public double getColumnBlue() {
        return columnBlue;
    }
}