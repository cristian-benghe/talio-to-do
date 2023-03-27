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

import commons.Board;
import commons.Card;
import commons.Column;
import jakarta.ws.rs.core.MediaType;
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

    private String server = "ws://localhost:8080/websocket";
    private StompSession session = connect("ws://localhost:8080/websocket");


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
     * @param column The column to add to the list.
     * @return returns a Column object, which is the server's representation
     * of the newly created column. The response body from the server is
     * deserialized into a Column object using the Jackson JSON library,
     * and this object is returned to the caller of the addColumn method
     */
    public Column addColumn(Column column) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/columns") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(column, APPLICATION_JSON), Column.class);
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
     * Retrieves using an HTTP GET request a board with a certain id
     *
     * @param boardId the id of the board we are looking for
     * @return The deserialized Board object
     */
    public Board getBoardById(long boardId) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(Board.class);
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
        server = server;
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

    private Board updateColumnInBoard(int columnID, Column column, Long boardId) {
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
     * @param colInd  the index of the the deleted column
     * @param boardId the id of the board the column is in
     * @return the updated board
     */
    public Board deleteColumn(int colInd, Long boardId) {
        Board board = getBoardById(boardId);
        board.updateColIndex(colInd);
        board.deleteColumn(colInd);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(board, MediaType.APPLICATION_JSON), Board.class);
    }

    /**
     * @param blue the rgb value of blue
     * @param green the rgb value of green
     * @param red the rgb value of red
     * @param boardId the id of the board
     * @return an updated board with the new color
     */
    public Board updateBoardColor(Double blue, Double green, Double red, Long boardId) {
        Board board=getBoardById(boardId);
        board.setColor(red, green, blue);
        
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + boardId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(board, MediaType.APPLICATION_JSON), Board.class);
    }
    /**
     * Adds a new column to a board.
     * This method sends a POST request to the server to add a new column to a board.
     * The boardId parameter specifies the ID of the board to add the column to,
     * while the column parameter should contain all necessary information about the new column.
     * @param boardId The ID of the board to add the column to.
     * @param column The column to add to the board.
     * @return The deserialized column.
     */
//    public Column addColumnToBoard(Long boardId, Column column) {
//        // Send a POST request to the server to add a new column to a board
//        return ClientBuilder.newClient(new ClientConfig())
//                .target(getServerUrl("api/boards/" + boardId + "/columns"))
//                .request(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .post(Entity.entity(column, MediaType.APPLICATION_JSON), Column.class);
//    }

}