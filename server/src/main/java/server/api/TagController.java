package server.api;

import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TagRepository;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagRepository tag_repo;

    /**
     * Constructs a new TagRepository object.
     * @param tag_repo - the tag repository
     */
    public TagController(TagRepository tag_repo) {
        this.tag_repo = tag_repo;
    }

    /**
     * Get request for obtaining all the tags
     * @return - all the tags
     */
    @GetMapping(path = { "", "/" })
    public List<Tag> getAll() {
        return tag_repo.findAll();
    }

    /**
     * Get request for obtaining the tag with the specified id
     * @param id - the id of the tag
     * @return - the searched tag if it exists or the request code
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") long id) {
        if (id < 0 || !tag_repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tag_repo.findById(id).get());
    }

    /**
     * Spring Boot RESTful API controller method that handles a POST request to add a new Tag resource
     * @param tag - the new tag that needs to be added to the server
     * @return - an HTTP response with the Tag object as the body
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {

        if (isNullOrEmpty(tag.getTitle())){

            return ResponseEntity.badRequest().build();
        }

        Tag saved = tag_repo.save(tag);
        return ResponseEntity.ok(saved);
    }

    /**
     * This method verifies if the given string is null or not
     * @param s - a string
     * @return true/false if the string is empty or not
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    // TODO: configure the PUT request
    // TODO: configure the DELETE request
}
