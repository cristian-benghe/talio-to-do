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
package server.api;

import commons.Board;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.service.BoardService;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
public class BoardController {
    private final BoardService boardservice;

    /**
     * Constructs a new BoardController with the specified service.
     * @param boardservice the service for Board operation
     * @param random
     */
    public BoardController(Random random, BoardService boardservice) {
        this.boardservice = boardservice;
    }

    /**
     * GET request for obtaining all the boards from the database
     * @return - all the boards from the database
     */
    @GetMapping(path = { "/api/boards", "/api/boards/" })
    public List<Board> getAll() {
        return boardservice.getAll();
    }

    /**
     * GET request for obtaining the board with the specified id
     * @param id - the id of the board
     * @return - the searched board (status 200) if it exists in the database
     * or BAD REQUEST (error 400) if it does not exist
     */
    @GetMapping("/api/boards/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        Optional<Board> board = boardservice.getById(id);
        if (board.isPresent()) {
            return ResponseEntity.ok(board.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST request for adding a board
     * @param board - the board to be added
     * @return - status 200 if the board has been successfully added in the database or
     * BAD REQUEST (error 400) if the board title is null or empty
     */
    @PostMapping(path = { "/api/boards", "/api/boards/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {
        Board added = boardservice.add(board);
        return ResponseEntity.ok(added);
    }

    /**
     * PUT request for updating a board with that specific id
     * @param id - the id of the board to be updated
     * @param board - the new board which will replace the already existing board
     * @return - status 200 if the board has been successfully
     * updated in the database or NOT FOUND (error 404) if the board is not found in the database
     */
    @PutMapping("/api/boards/{id}")
    public ResponseEntity<Board> update(@PathVariable("id") long id, @RequestBody Board board) {
        Board updated = boardservice.update(id, board);
        return ResponseEntity.ok(updated);
    }

    /**
     * This method will delete a board with the given ID from the database if it exists.
     * @param id - the id of the board, the user wants to delete
     * @return if the board is found and deleted,
     * it will return a response with HTTP status code 204 or
     * if the board is not found, it will return a response with HTTP status code 404.
     */
    @DeleteMapping("/api/boards/{id}")
    public ResponseEntity delete(@PathVariable("id") long id) {
        boardservice.delete(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Receives a message with the specified mapping ("/boards") and adds a new board to the system.
     * The method sends the newly added board to the "/topic/boards" endpoint.
     * @param board the Board object to be added to the system
     * @return the Board object that was added to the system
     */
    @MessageMapping("/boards") // app/boards TODO: error handling
    @SendTo("/topic/boards")
    public Board addMessage(Board board) {
        add(board);
        return board;
    }

    /**
     * Receives a message with the specified mapping ("/delete-board") and deletes
     * a certain board from the system.
     * The method sends the newly added board to the "/topic/delete-board" endpoint.
     * @param id the id of the Board object to be deleted to the system
     * @return the id of the deleted board
     */
    @MessageMapping("/delete-board") // app/delete-board TODO: error handling
    @SendTo("/topic/delete-board")
    public Long addMessageDelete(Long id) {
        delete(id);
        return id;
    }

    /**
     * Receives a message with the specified mapping ("/update-board") and updates
     * a certain board from the system.
     * The method sends the updated board to the "/topic/update-board" endpoint.
     * @param board the Board object to be updated (with the new
     * title) to the system
     * @return the Board object that was updated in the system
     */
    @MessageMapping("/update-board") // app/update-board
    @SendTo("/topic/update-board")
    public Board addMessageUpdate(Board board) {
        update(board.getId(), board);
        return board;
    }

    /**
     * Receives a message with the specified mapping ("/update-in-board") and updates
     * a certain board from the system.
     * The method sends the updated board to the "/topic/update-in-board" endpoint.
     * It is used for refreshing BoardOverview
     * @param board the Board object to be updated (with the new
     * title) to the system
     * @return the Board object that was updated in the system
     */
    @MessageMapping("/update-in-board") // app/update-in-board
    @SendTo("/topic/update-in-board")
    public Board addMessageUpdateInBoard(Board board) {
        return board;
    }

    /**
     * Receives a message with the specified mapping ("/update-title-in-board") and updates
     * the title from
     * a certain board from the system.
     * The method sends the updated board to the "/topic/update-title-in-board" endpoint.
     * It is used for refreshing BoardOverview
     * @param board the Board object to be updated (with the new
     * title) to the system
     * @return the Board object that was updated in the system
     */
    @MessageMapping("/update-title-in-board") // app/update-title-in-board
    @SendTo("/topic/update-title-in-board")
    public Board addMessageUpdateTitleInBoard(Board board) {
        return board;
    }

    /**
     * Receives a message with the specified mapping ("/update-labels-in-board") and updates
     * only if a label has been changed (so it adds some seconds before refreshing)
     * a certain board from the system.
     * The method sends the updated board to the "/update-labels-in-board" endpoint.
     * It is used for refreshing BoardOverview
     * @param board the Board object to be updated (with the new
     * title) to the system
     * @return the Board object that was updated in the system
     */
    @MessageMapping("/update-labels-in-board") // app/update-labels-in-board
    @SendTo("/topic/update-labels-in-board")
    public Board addMessageUpdateLabelsInBoard(Board board) {
        return board;
    }


    /**
     * Handles the GET request by returning a progression HashMap that maps
     * the identifier of a Card in the given Board to its respective current
     * progression triplet, which includes (from left to right) the number of
     * completed associated Tasks, the total number of associated Tasks, and
     * whether the card has a long description
     * @param id the identifier of the board
     * @return the HashMap of the progression triplets.
     */
    @GetMapping("/api/boards/getCardProgression")
    public ResponseEntity<HashMap<Long, Double>>
        getCardProgression(@RequestParam("id") long id){

        Optional<Board> optBoard = boardservice.getById(id);

        if(optBoard.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        var temp = boardservice.progressionMap(optBoard.get());

        return ResponseEntity.ok(temp);
    }
}