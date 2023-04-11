package server.service;


import commons.Card;
import commons.Task;
import org.springframework.stereotype.Service;
import server.database.CardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private final CardRepository repo;


    /**
     * Constructs a new ColumnController with the specified repositories.
     * @param repo the repository for Card objects
     */
    public CardService(CardRepository repo) {

        this.repo = repo;


    }
    /**
     * Returns a list of all Card objects in the database.
     * @return the list of Card objects
     */
    public List<Card> getAll() {
        return repo.findAll();
    }

    /**
     * Returns the Card object with the specified id.
     * @param id the id of the Card object to retrieve
     * @return the Card object with the specified id or null if it does not exist
     */
    public Optional<Card> getById(long id) {
        return repo.findById(id);
    }

    /**
     * Adds a Card entity.
     *
     * @param card the card entity to add
     * @return the added  card entity
     * @throws IllegalArgumentException if the title of the card entity is null or empty
     */

    public Card add(Card card) {
        if (card.getTitle() == null || card.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        return repo.save(card);
    }
    /**
     * Updates a Card entity by ID.
     *
     * @param id the ID of the Card entity to update
     * @param card the updated Card entity
     * @return the updated Card entity
     * @throws IllegalArgumentException if the Card entity with the specified ID does not exist or
     * the title of the Card entity is null or empty
     */

    public Card update(long id, Card card) {
        Optional<Card> existing = repo.findById(id);
        if (existing.isPresent()) {
            Card updated = existing.get();
            updated.setTitle(card.getTitle());
            updated.setDescription(card.getDescription());
            updated.setTags(card.getTags());
            updated.setColor(card.getBlue(), card.getGreen(), card.getRed());
            updated.setTaskList(card.getTaskList());
            return repo.save(updated);
        } else {
            throw new IllegalArgumentException("Card with ID " + id + " not found.");
        }
    }

    /**
     * Deletes the Card object with the specified id.
     * @param id the id of the card object to delete
     */
    public void delete(long id) {
        repo.deleteById(id);
    }

    /**
     * Adds a new Task to the Task List attribute of the
     * given Card instance
     * @param id the identifier of the Card instance
     * @param task the new Task to be added
     * @return the updated Card instance
     */
    public Card addTask(long id, Task task){

        Optional<Card> optCard = this.getById(id);
        if(optCard.isEmpty()){
            throw new IllegalArgumentException();
        }
        Card card = optCard.get();
        if(card.getTaskList()==null){
            card.setTaskList(new ArrayList<Task>());
        }
        card.getTaskList().add(task);
        return repo.save(card);
    }

    /**
     * Updates the Task List attribute for the given
     * Card instance.
     * @param id the identifier of the card instance
     * @param taskList the updated Task List
     * @return the updated Task List
     */
    public Card updateTaskList(long id, List<Task> taskList){

        Optional<Card> optCard = this.getById(id);
        if(optCard.isEmpty()){
            throw new IllegalArgumentException();
        }

        Card card = optCard.get();
        card.setTaskList(taskList);


        return repo.save(card);
    }


    /**
     * Returns the Task List for the Card instance
     * with the corresponding identifier.
     * @param cardId the identifier of the Card instance
     * @return the corresponding Task List.
     */
    public List<Task> getTaskList(long cardId){

        Optional<Card> optCard = this.getById(cardId);
        if(optCard.isEmpty()){
            throw new IllegalArgumentException();
        }

        return optCard.get().getTaskList();



    }
}

