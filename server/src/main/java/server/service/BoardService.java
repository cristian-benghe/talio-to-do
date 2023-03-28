package server.service;

import commons.Board;
import server.database.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {


    private final BoardRepository repo;
    /**
     * Constructs a BoardService instance with the specified BoardRepository.
     *
     * @param repo the repository for Board entity
     */
    public BoardService(BoardRepository repo) {
        this.repo = repo;
    }
    /**
     * Retrieves all Board entities.
     *
     * @return a list of all Board entities
     */
    public List<Board> getAll() {
        return repo.findAll();
    }
    /**
     * Retrieves a Board entity by ID.
     *
     * @param id the ID of the Board entity to retrieve
     * @return an Optional of the Board entity with the specified ID
     */
    public Optional<Board> getById(long id) {
        return repo.findById(id);
    }
    /**
     * Adds a Board entity.
     *
     * @param board the Board entity to add
     * @return the added Board entity
     * @throws IllegalArgumentException if the title of the Board entity is null or empty
     */
    public Board add(Board board) {
        if (isNullOrEmpty(board.getTitle())) {
            throw new IllegalArgumentException("Board title cannot be null or empty.");
        }
        return repo.save(board);
    }
    /**
     * Updates a Board entity by ID.
     *
     * @param id the ID of the Board entity to update
     * @param board the updated Board entity
     * @return the updated Board entity
     * @throws IllegalArgumentException if the Board entity with the specified ID does not exist or
     * the title of the Board entity is null or empty
     */
    public Board update(long id, Board board) {
        Optional<Board> existing = repo.findById(id);
        if (existing.isPresent()) {
            Board updated = existing.get();
            updated.setTitle(board.getTitle());
            updated.setTags(board.getTags());
            updated.setColumns(board.getColumns());
            updated.setColor(board.getRed(), board.getGreen(), board.getBlue());
            return repo.save(updated);
        } else {
            throw new IllegalArgumentException("Board with ID " + id + " not found.");
        }
    }
    /**
     * Deletes a Board entity by ID.
     *
     * @param id the ID of the Board entity to delete
     * @throws IllegalArgumentException if the Board entity with the specified ID does not exist
     */
    public void delete(long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Board with ID " + id + " not found.");
        }
        repo.deleteById(id);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}