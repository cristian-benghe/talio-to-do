package server.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import commons.Tag;
import server.service.TagService;

public class TagControllerTest {

    private TagController tagController;
    private TagService tagService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        tagService = mock(TagService.class);
        tagController = new TagController(tagService);
    }

    @Test
    public void testGetAll() {
        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(new Tag("tag"));
        expectedTags.add(new Tag("title"));
        when(tagService.getAll()).thenReturn(expectedTags);

        List<Tag> actualTags = tagController.getAll();

        assertThat(actualTags).isEqualTo(expectedTags);
    }

    @Test
    public void testGetById() {
        long tagId = 1;
        Tag expectedTag = new Tag("tag");
        when(tagService.getById(tagId)).thenReturn(Optional.of(expectedTag));

        ResponseEntity<Tag> response = tagController.getById(tagId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedTag);
    }

    @Test
    public void testGetByIdNotFound() {
        long tagId = 1;
        when(tagService.getById(tagId)).thenReturn(Optional.empty());

        try {
            tagController.getById(tagId);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void testAdd() {
        Tag expectedTag = new Tag("tag");
        when(tagService.add(any(Tag.class))).thenReturn(expectedTag);

        ResponseEntity<Tag> response = tagController.add(expectedTag);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedTag);
    }

    @Test
    public void testDelete() {
        long tagId = 1;
        Tag existingTag = new Tag("tag");
        when(tagService.getById(tagId)).thenReturn(Optional.of(existingTag));

        ResponseEntity<Void> response = tagController.delete(tagId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(tagService).delete(tagId);
    }

    @Test
    public void testDeleteNotFound() {
        long tagId = 1;
        when(tagService.getById(tagId)).thenReturn(Optional.empty());

        try {
            tagController.delete(tagId);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}