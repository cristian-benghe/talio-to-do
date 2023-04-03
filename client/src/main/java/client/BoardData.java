package client;

import commons.Board;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardData implements Serializable {

    private Map<String, List<Board>> boardMap;

    /**
     * Default constructor initialises the map
     */
    public BoardData() {
        boardMap = new HashMap<>();
    }

    /**
     * Serializes data in a file
     * @param fileName the file where the Object will be output
     * @throws IOException for example if the file does not exist
     */
    public void serializeBoardData(String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(boardMap);
        oos.close();
        fos.close();
    }

    /**
     * deserialization method for our object
     * @param fileName a string represeting the path where is the file
     *        from which we are taking the object
     * @throws IOException for example if the file does not exist
     * @throws ClassNotFoundException if it
     * is tried to deserialize a different object (not a Map<String, List<Board>>
     */
    public void deserializeBoardData(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        try {
            ObjectInputStream ois = new ObjectInputStream(fis);
            boardMap = (Map<String, List<Board>>) ois.readObject();
            ois.close();
            fis.close();
        }
        catch (EOFException e) {
            boardMap = new HashMap<>();
        }
    }

    /**
     * getter for the map
     * @return the map with all ips + boards
     */
    public Map<String, List<Board>> getBoardMap() {
        return boardMap;
    }

    /**
     * setter for the map
     * @param boardMap the map with all ips + boards to be set
     */
    public void setBoardMap(Map<String, List<Board>> boardMap) {
        this.boardMap = boardMap;
    }

    /**
     * gets the board list from a certain server
     * @param ip we are looking the board for
     *           Disclaimer: the ip includes the port
     *           (even though the naming is not suggestive)
     * @return the list of the boards from the specific server
     */
    public List<Board> getBoardList(String ip) {
        return boardMap.get(ip);
    }

    /**
     * setter for the workspace
     * @param ip the server address
     * @param boardList the list of the boards to be set
     */
    public void setBoardList(String ip, List<Board> boardList) {
        boardMap.put(ip, boardList);
    }
}