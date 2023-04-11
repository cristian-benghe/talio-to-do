package server.api;


import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.TagService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagservice;

    /**
     * Constructs a new TagService object.
     * @param tagservice - the tag service
     */
    public TagController(TagService tagservice) {
        this.tagservice = tagservice;
    }

    /**
     * Get request for obtaining all the tags
     * @return - all the tags
     */
    @GetMapping(path = { "", "/" })
    public List<Tag> getAll() {
        return tagservice.getAll();
    }

    /**
     * Get request for obtaining the tag with the specified id
     * @param id - the id of the tag
     * @return - the searched tag if it exists or OK (status 200)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") long id) {
        Optional<Tag> tag = tagservice.getById(id);
        if (tag.isPresent()) {
            return ResponseEntity.ok(tag.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Spring Boot RESTful API controller method that handles a POST request
     * to add a new Tag resource
     * @param tag - the new tag that needs to be added to the server
     * @return - an HTTP response with the Tag object as the body
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {

//        if (isNullOrEmpty(tag.getTitle())){
//
//            return ResponseEntity.badRequest().build();
//        }

        Tag saved = tagservice.add(tag);
        return ResponseEntity.ok(saved);
    }

    /**
     * Updates an existing Column object with the specified id.
     * @param id the id of the Column object to update
     * @param tag the updated Column object
     * @return a response containing the updated Column object,or a not found response
     * if the id is invalid
     */

    @PutMapping("/{id}")
    public ResponseEntity<Tag> update(@PathVariable("id") long id, @RequestBody Tag tag) {
        Optional<Tag> existing = tagservice.getById(id);
        if (existing.isPresent()) {
            Tag saved = tagservice.update(existing.get(),tag);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes the Tag object with the specified id and all cards contained by that column.
     * @param id the id of the Tag object to delete
     * @return a response indicating success or failure
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        Optional<Tag> existing = tagservice.getById(id);
        if (existing.isPresent()) {
            tagservice.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // TODO: configure the PUT request
    // TODO: configure the DELETE request
}
