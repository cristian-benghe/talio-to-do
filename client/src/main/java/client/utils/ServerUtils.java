/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.lang.reflect.Type;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import commons.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Component
public class ServerUtils {

    private String server;
    private StompSession session;


    /**
     * set method used in the first scene of the application
     *
     * @param serverAddress the string containing the URL
     */
    public void setServerAddress(String serverAddress) {
        server = serverAddress;
    }

    /**
     * getter for the server address
     *
     * @return the string containing the URL
     */
    public String getServerAddress() {
        return server;
    }

    /**
     * Returns an URL with a certain path added
     *
     * @param path the desired path
     * @return the string containing the URL
     */
    private String getServerUrl(String path) {
        return server + path;
    }

    private Column updateCardInColumn(int cardId, Card card, Long columnId, Long boardId) {
        Board board = getBoardById(boardId);
        Column column = board.getColumns().get(Math.toIntExact(columnId) - 1);
        column.getCards().set(cardId - 1, card);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/columns/" + column.getId())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(column, MediaType.APPLICATION_JSON), Column.class);
    }

    /**
     * Delete a card from api/cards
     *
     * @param cardId Id of the card that will be deleted
     * @return Response of the server/request
     */
    public Response deleteCardFromCardApi(Long cardId) {

        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/cards/" + cardId)
                .request(MediaType.APPLICATION_JSON)
                .delete();
    }

    private Column updateCardDragDrop(Board board, int cardId,
                                      Card card, Long columnId, Long boardId) {
        Column column = board.getColumns().get(Math.toIntExact(columnId));
        board.getColumns().set(Math.toIntExact(columnId), column);
        column = board.getColumns().get(Math.toIntExact(columnId));
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/columns/" + (column.getId()))
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(column, MediaType.APPLICATION_JSON), Column.class);
    }

    /**
     * A method to change card arrangement
     * @param columnID An id of hte column that will changed
     * @param column the object ofcolumn
     * @param boardId the boardId
     */
    public void updateCardArrangement(int columnID, Column column, Long boardId) {
        Board board = getBoardById(boardId);
        board.setColumn(columnID, column);
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(board, MediaType.APPLICATION_JSON), Board.class);
    }

    /**
     * A method to delete a card with server from database
     *
     * @param board    board to be used
     * @param cardId   card to be deleted
     * @param columnId column that should be updated
     * @param boardId  boardid
     * @return Column
     */
    public Long deleteCardServer(Board board, Long cardId, Long columnId, Long boardId) {
        Column column = board.getColumns().get(Math.toIntExact(columnId) - 1);
        Card card = column.getCards().get(Math.toIntExact(cardId) - 2);
        column.getCards().remove(card);
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/columns/" + column.getId())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(column, MediaType.APPLICATION_JSON), Column.class);
        return card.getId();
    }

    /**
     * update the card title
     *
     * @param cardid   cardid to be updated
     * @param columnID columid to be updated
     * @param text     text that will be replaced
     * @param boardId  boardid
     */
    public void updateCardTitle(Long cardid, Long columnID, String text, Long boardId) {
        Board board = getBoardById(boardId);
        Column column = board.getColumns().get((int) (columnID - 1));
        Card card = column.getCards().get(Math.toIntExact(cardid) - 1);
        card.setTitle(text);
        //updateColumn(Math.toIntExact(column.getId()), column);
        updateCardInColumn(Math.toIntExact(cardid), card, columnID, boardId);
    }

    /**
     * A method to update the database when card is drag and dropped
     *
     * @param cardid      cardId that is drag and dropped
     * @param columnID    Id of the column that card dragged from
     * @param newColumnId Id of the column that the card is dropped
     * @param boardId     boardid
     */
    public void cardDragDropUpdate(Long cardid, Long columnID, Long newColumnId, Long boardId) {
        Board board = getBoardById(boardId);
        Column column = board.getColumns().get((int) (columnID - 0));
        Card card = column.getCards().get(Math.toIntExact(cardid) - 2);
        Column newcolumn = board.getColumns().get((int) (newColumnId - 0));
        newcolumn.getCards().add(card);
        board.getColumns().set((int) (newColumnId - 0), newcolumn);

//        column.getCards().remove(card);
//        board.getColumns().set((int) (columnID - 0),column);
        //deletion on server needed.
        deleteCardServer(board, cardid, columnID + 1, boardId);
        //updateColumn(Math.toIntExact(column.getId()), column);
        board = getBoardById(boardId);
        newcolumn = board.getColumns().get((int) (newColumnId - 0));
        newcolumn.getCards().add(card);
        board.getColumns().set((int) (newColumnId - 0), newcolumn);
        updateCardDragDrop(board, Math.toIntExact(cardid), card, newColumnId, boardId);
        System.out.println(getBoardById(boardId).getColumns().get(1).getCards());
    }


    /**
     * A method to Add card to column (serverside)
     *
     * @param boardid  board id to add the card
     * @param columnid an id of the column
     * @param newCard
     * @param cardid
     * @return column
     */
    public Column addCardToColumn(Long boardid, Long columnid, Card newCard, Long cardid) {
        Board board = getBoardById(boardid);
        Column column = board.getColumns().get(Math.toIntExact(columnid) - 1);
        newCard.setColumnId(cardid);
        column.getCards().add(newCard);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/columns/" + column.getId())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(column, MediaType.APPLICATION_JSON), Column.class);
    }

    /**
     * to get column by id
     *
     * @param columnId to get column
     * @return Column object
     */
    public Column getColumnById(long columnId) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("api/columns/" + columnId)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(Column.class);
    }

    /**
     * Adds a new board.
     * This method sends a POST request to the server to add a new card
     *
     * @param board The board to be added to the list.
     * @return The deserialized board
     */
    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }


    /**
     * Retrieves an HTTP GET request for the boards resource.
     *
     * @return a list of boards
     */
    public List<Board> getBoards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Board>>() {
                });
    }


    /**
     * Adds a new card.
     * This method sends a POST request to the server to add a new card
     * to the to-do list. The card parameter should contain all necessary
     * information about the new card, including the task description.
     *
     * @param card The card to add to the list.
     * @return The deserialized card.
     */
    public Card addCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * Retrieves an HTTP GET request for the cards resource.
     *
     * @return all the cards in the database
     */
    public List<Card> getCards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Card>>() {
                });
    }

    /**
     * Retrieves an HTTP GET request for the cards resource where the cards are in a certain column.
     *
     * @param column the column we search cards in
     * @return a list of cards
     */
    public List<Card> getCardsFromColumn(Column column) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards") //
                .queryParam("column_id", column.getId())
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Card>>() {
                });
    }

    /**
     * Adds a new column.
     * This method sends a POST request to the server to add a new card
     * to the to-do list. The column parameter should contain all necessary
     * information about the new column
     *
     * @param tag The column to add to the list.
     * @return returns a Column object, which is the server's representation
     * of the newly created column. The response body from the server is
     * deserialized into a Column object using the Jackson JSON library,
     * and this object is returned to the caller of the addColumn method
     */
    public Tag addTag(Tag tag) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tags") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * Retrieves an HTTP GET request for the columns resource.
     *
     * @return all the columns in the database
     */
    public List<Column> getColumns() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/columns") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Column>>() {
                });
    }

    /**
     * Retrieves an HTTP GET request for the columns resource
     * where the columns are in a certain board.
     *
     * @param board the board in which we are looking for columns
     * @return a list of cards
     */
    public List<Column> getColumnsFromBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/columns") //
                .queryParam("board_id", board.getId())
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Column>>() {
                });
    }

    /**
     * update the board title when it's modified
     *
     * @param boardId  the id of the board to be modified
     * @param newTitle the new title the board will have
     * @return The deserialized Board object
     */
    public Board updateBoardTitle(long boardId, String newTitle) {
        Board board = ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .get(Board.class);
        if (board == null) {
            throw new NoSuchElementException();
        }
        board.setTitle(newTitle);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(board, MediaType.APPLICATION_JSON), Board.class);
    }

    /**
     * This method sends an HTTP DELETE request using the Jersey client library
     *
     * @param boardId - the id of the board that has to be deleted
     */
    public void deleteBoard(long boardId) {
        ClientBuilder.newClient(new ClientConfig()) // creates a new client
                .target(server) // sets the target server for the request
                .path("api/boards/" + boardId) // specifies the API endpoint to delete the board
                .request() // creates a new request object
                .delete(); // sends the HTTP DELETE request and returns the response,
        // but the code does not handle the response explicitly
    }

    /**
     * @param tagId id of the tag in db
     */
    public void deleteTag(long tagId) {
        ClientBuilder.newClient(new ClientConfig()) // creates a new client
                .target(server) // sets the target server for the request
                .path("api/tags/" + tagId) // specifies the API endpoint to delete the board
                .request() // creates a new request object
                .delete(); // sends the HTTP DELETE request and returns the response,
        // but the code does not handle the response explicitly
    }

    /**
     * Retrieves using an HTTP GET request a board with a certain id
     *
     * @param boardId the id of the board we are looking for
     * @return The deserialized Board object
     */
    public Board getBoardById(long boardId) {
        System.out.println(boardId);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(Board.class);
    }

    /**
     * @param tagId the id of the tag
     * @return the tag with id tagid
     */
    public Tag getTagById(long tagId) {
        System.out.println(tagId);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("api/tags/" + tagId)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(Tag.class);
    }


    /**
     * Establishes a WebSocket connection to the specified
     * URL using STOMP protocol and returns a new {@link StompSession}.
     *
     * @param url The URL to connect to as a String.
     * @return A new {@link StompSession} object representing the established connection.
     * @throws IllegalStateException If connection cannot be established.
     * @throws RuntimeException      If an exception is thrown during the connection attempt.
     * @throws InterruptedException  If the thread is interrupted during the connection attempt.
     */
    public StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }


    /**
     * Registers a consumer for messages of the specified type from the specified destination.
     *
     * @param dest     the destination to subscribe to
     * @param type     the class of the message payload
     * @param consumer the consumer to handle the received messages
     * @param <T>      the method is can be used for any class template <T>
     */
    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    /**
     * Sends an object to the specified destination.
     *
     * @param dest the destination to send the object to
     * @param o    the object to send
     * @throws IllegalStateException if the WebSocket session is not connected
     */
    public void send(String dest, Object o) {
        session.send(dest, o);
    }

    /**
     * gets the URL
     *
     * @return the URL of the server in String format
     */
    public String getServer() {
        return server;
    }

    /**
     * sets the URL
     *
     * @param server the URL of the server in String format
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * gets the session
     *
     * @return the session
     */
    public StompSession getSession() {
        return session;
    }

    /**
     * sets the session
     *
     * @param session the session
     */
    public void setSession(StompSession session) {
        this.session = session;
    }

    /**
     * @param id        = id of the board
     * @param newColumn = column to add to board
     * @param i         = id of the column in the board
     * @return the board after you add a column
     */
    public Board addColumnToBoard(Long id, Column newColumn, int i) {
        Board board = getBoardById(id);
        newColumn.setIDinBoard(i);
        board.addColumn(newColumn);

        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + id)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(board, MediaType.APPLICATION_JSON), Board.class);
    }


    /**
     * @param columnID = id of the column in the board
     * @param text     = the new title of the column
     * @param boardId  = the id of the board
     */
    public void updateColTitle(int columnID, String text, Long boardId) {
        Board board = getBoardById(boardId);
        Column column = board.getColumns().get(columnID - 1);
        column.setTitle(text);
        //updateColumn(Math.toIntExact(column.getId()), column);
        updateColumnInBoard(columnID, column, boardId);
    }

    /**
     * A method to update the column in board
     * @param columnID An id of the column that will be updated
     * @param column The column object
     * @param boardId id of the board
     * @return Board object
     */
    public Board updateColumnInBoard(int columnID, Column column, Long boardId) {
        Board board = getBoardById(boardId);
        board.setColumn(columnID - 1, column);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(board, MediaType.APPLICATION_JSON), Board.class);
    }

    private Column updateColumn(int columnID, Column column) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + columnID)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(column, MediaType.APPLICATION_JSON), Column.class);
    }

    /**
     * A method to update the board after the column rearrangement
     * @param board board that will be updated
     * @param boardId id of the board
     */
    public void updateBoard(Board board, int boardId)
    {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(board, MediaType.APPLICATION_JSON), Board.class);
    }

    /**
     * A ,ethod to delete the column from api/columns
     *
     * @param columnId Id of the column that will be deleted
     * @return Response of the server/request
     */
    public Response deleteColumnFromApi(int columnId) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/columns/" + columnId)
                .request(MediaType.APPLICATION_JSON)
                .delete();
    }

    /**
     * @param colInd  the index of the the deleted column
     * @param boardId the id of the board the column is in
     * @return the updated board
     */
    public Long deleteColumn(int colInd, Long boardId) {
        Board board = getBoardById(boardId);
        Column column = board.getColumns().get(colInd);
        Long tmpId = column.getId();
        board.updateColIndex(colInd);
        board.deleteColumn(colInd);
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(board, MediaType.APPLICATION_JSON), Board.class);
        return tmpId;
    }

    /**
     * @param blue    the rgb value of blue
     * @param green   the rgb value of green
     * @param red     the rgb value of red
     * @param boardId the id of the board
     * @return an updated board with the new color
     */
    public Board updateBoardColor(Double blue, Double green, Double red, Long boardId) {
        Board board = getBoardById(boardId);
        board.setColor(red, green, blue);

        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(board, MediaType.APPLICATION_JSON), Board.class);
    }

    /**
     * @param id     the id of the tag in the db
     * @param newTag added tag in the board
     * @return the updated board
     */
    public Board addTagToBoard(Long id, Tag newTag) {
        Board board = getBoardById(id);
        board.addTag(newTag);

        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + id)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(board, MediaType.APPLICATION_JSON), Board.class);
    }

    /**
     * @param id the id of the card
     * @param newCard the new card
     * @return the updated card in the database
     */
    public Tag addCardToTag(Long id, Card newCard) {
        Tag tag = getTagById(id);
        tag.addCard(newCard);

        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/tags/" + id)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(tag, MediaType.APPLICATION_JSON), Tag.class);
    }


    /**
     * @param tagID   = id of the tag in the board
     * @param text    = the new title of the tag
     * @param boardId = the id of the board
     */
//    public void updateTagTitle(int tagID, String text, Long boardId) {
//        Board board = getBoardById(boardId);
//        Tag tag = board.getTags().get(tagID - 1);
//        tag.setTitle(text);
//        //updateColumn(Math.toIntExact(column.getId()), column);
//        updateTagInBoard(tagID, tag, boardId);
//    }

//    public Board updateTagInBoard(int tagID, Tag tag, Long boardId) {
//        Board board = getBoardById(boardId);
//        board.setTag(tagID - 1, tag);
//        return ClientBuilder.newClient(new ClientConfig())
//                .target(server).path("api/boards/" + boardId)
//                .request(MediaType.APPLICATION_JSON)
//                .put(Entity.entity(board, MediaType.APPLICATION_JSON), Board.class);
//    }

//    public Tag updateTag(int tagID, Tag tag) {
//        return ClientBuilder.newClient(new ClientConfig())
//                .target(server).path("api/boards/" + tagID)
//                .request(MediaType.APPLICATION_JSON)
//                .put(Entity.entity(tag, MediaType.APPLICATION_JSON), Tag.class);
//    }

    /**
     * @param tagInd  the index of the the deleted tag
     * @param boardId the id of the board the tag is in
     * @return the updated board
     */
    public Board deleteTagFromBoard(Long tagInd, Long boardId) {
        Board board = getBoardById(boardId);
        for(int i=0;i<board.getTags().size();i++){
            if(board.getTags().get(i).getTagID()==tagInd){
                board.removeTag(i);
                break;
            }
        }
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(board, MediaType.APPLICATION_JSON), Board.class);
    }
//    /**
//     * Adds a new column to a board.
//     * This method sends a POST request to the server to add a new column to a board.
//     * The boardId parameter specifies the ID of the board to add the column to,
//     * while the column parameter should contain all necessary information about the new column.
//     * @param boardId The ID of the board to add the column to.
//     * @param column The column to add to the board.
//     * @return The deserialized column.
//     */
//    public Column addColumnToBoard(Long boardId, Column column) {
//        // Send a POST request to the server to add a new column to a board
//        return ClientBuilder.newClient(new ClientConfig())
//                .target(getServerUrl("api/boards/" + boardId + "/columns"))
//                .request(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .post(Entity.entity(column, MediaType.APPLICATION_JSON), Column.class);
//    }

    /**
     * POST request to send the input passcode in ClientConnect
     * @param password the passcode that is provided
     * @return the password provided
     */
    public String sendPassword(String password) {
//        System.out.println(server);
//        return null;
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("add-password") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(password, APPLICATION_JSON), String.class);
    }

    /**
     * check in the server if the password is correct
     * @return true if passwords match, false else
     */
    public Boolean checkPassword() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("check-password") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Boolean>() {
                });
    }


    /**
     * Adds a new Task to the server, and links it to the
     * Card instance with the corresponding identifier.
     * @param cardId the identifier of the Card instance
     * @param task the newly created Task
     * @return the newly created Task
     */
    public Task addTask(long cardId, Task task){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("api/tasks/addTask/byCard")
                .queryParam("id",cardId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(task, APPLICATION_JSON),Task.class);
    }


    /**
     * Updates the entirety of the Task List of a particular
     * Card instance with the given identifier.
     * @param cardID the identifier the Card list
     * @param taskList the updated Task List
     * @return the updated Task List
     */
    public Card updateTaskList(long cardID, List<Task> taskList){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("api/cards/updateTaskList")
                .queryParam("id",cardID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(taskList, APPLICATION_JSON), Card.class);
    }

    /**
     * @param tagId the id of the tag
     * @param tag the new tag
     * @param boardId the id of the board
     * @return the updated board after addition
     */
    public Board updateTagInBoard(int tagId, Tag tag, Long boardId) {
        Board board=getBoardById(boardId);
        int ind=-1;
        for(int i=0;i<board.getTags().size();i++){
            if(board.getTags().get(i).getTagID()==tagId){
                ind=i;
            }
        }
        board.updateTag(ind, tag);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(board, MediaType.APPLICATION_JSON), Board.class);
    }

    /**
     * @param tagId the id of the tag
     * @param text the new title of the tag
     * @return the new tag with the updated title
     */
    public Tag updateTagTitle(int tagId, String text) {
        Tag tag=getTagById(tagId);
        tag.setTitle(text);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/tags/" + tagId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(tag, MediaType.APPLICATION_JSON), Tag.class);
    }

    /**
     * @param tagId the id of the tag
     * @param tag the updated tag
     * @return the updated tag in the database
     */
    public Tag updateTag(Long tagId, Tag tag) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/tags/" + tagId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(tag, MediaType.APPLICATION_JSON), Tag.class);
    }
    /**
     * Persists the changes given by the Task instance
     * in the server.
     * @param task the updated Task instance.
     */
    public void updateTask(Task task){

        ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("api/tasks/update")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(task, APPLICATION_JSON), Task.class);


    }


    /**
     * Returns the List of all Task instances that are linked
     * to the Card, identified by the given identifier.
     * @param cardId the identifier of the Card instance
     * @return the List of all linked Task instances
     */
    public List<Task> getAllTasksByCardId(long cardId){
        return ClientBuilder.newClient(new ClientConfig())
                    .target(server)
                    .path("api/cards/getTaskList")
                    .queryParam("id", cardId)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(new GenericType<List<Task>>(){});
    }
    public Column updateCardInColumnColor(Long cardId, Card cardd, Long columnId, Long boardId) {
        Column column=getColumnById(columnId);
        for(int i=0;i<column.getCards().size();i++){
            if(column.getCards().get(i).getId()==cardId){
                column.updateColorInCard(i, cardd.getRed(), cardd.getBlue(), cardd.getGreen());
            }
        }
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/columns/" + columnId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(column, MediaType.APPLICATION_JSON), Column.class);
    }

    
}