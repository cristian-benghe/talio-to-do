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
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final Random random;
    private final BoardRepository repo;

    /**
     * Constructs a new BoardRepository object.
     * @param random - instance of class Random
     * @param repo - the board Repository
     */
    public BoardController(Random random, BoardRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    /**
     * GET request for obtaining all the boards from the database
     * @return - all the boards from the database
     */
    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        return repo.findAll();
    }

    /**
     * GET request for obtaining the board with the specified id
     * @param id - the id of the board
     * @return - the searched board (status 200) if it exists in the database
     * or BAD REQUEST (error 400) if it does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * POST request for adding a board
     * @param board - the board to be added
     * @return - status 200 if the board has been successfully added in the database or
     * BAD REQUEST (error 400) if the board title is null or empty
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {

        if (isNullOrEmpty(board.getTitle())) {
            return ResponseEntity.badRequest().build();
        }

        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }

    /**
     * Checks if the provided string is null or empty
     * @param s - the string to be checked
     * @return - true if the string is null or empty or false if not
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * PUT request for updating a board with that specific id
     * @param id - the id of the board to be updated
     * @param board - the new board which will replace the already existing board
     * @return - status 200 if the board has been successfully
     * updated in the database or NOT FOUND (error 404) if the board is not found in the database
     */
    @PutMapping("/{id}")
    public ResponseEntity<Board> update(@PathVariable("id") long id, @RequestBody Board board) {
        Optional<Board> existing = repo.findById(id);
        if (existing.isPresent()) {
            Board updated = existing.get();
            updated.setTitle(board.getTitle());
            updated.setTags(board.getTags());
            updated.setColumns(board.getColumns()); // update the columns
            Board saved = repo.save(board);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * This method will delete a board with the given ID from the database if it exists.
     * @param id - the id of the board, the user wants to delete
     * @return if the board is found and deleted,
     * it will return a response with HTTP status code 204 or
     * if the board is not found, it will return a response with HTTP status code 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            // this creates a ResponseEntity with a HTTP status code of 204 (No Content)
            return ResponseEntity.noContent().build();
        }
        else {
            // this creates a ResponseEntity with a HTTP status code of 404 (Not Found)
            return ResponseEntity.notFound().build();
        }
    }

}