package client.scenes;

import client.utils.ServerUtils;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TagTemplateCtrlTest {

    private MainCtrl mainCtrl;
    private ServerUtils server;
    private TagTemplateCtrl tagTemplateCtrl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);


        mainCtrl = mock(MainCtrl.class);
        server = mock(ServerUtils.class);

        tagTemplateCtrl = new TagTemplateCtrl(server, mainCtrl);


    }

    @Test
    void toRgbCode() {

        // Test with black color
        String expected = "#000000";
        String actual = tagTemplateCtrl.toRgbCode(Color.BLACK);
        assertEquals(expected, actual);

// Test with white color
        expected = "#FFFFFF";
        actual = tagTemplateCtrl.toRgbCode(Color.WHITE);
        assertEquals(expected, actual);

// Test with red color
        expected = "#FF0000";
        actual = tagTemplateCtrl.toRgbCode(Color.RED);
        assertEquals(expected, actual);

// Test with green color
        expected = "#008000";
        actual = tagTemplateCtrl.toRgbCode(Color.GREEN);
        assertEquals(expected, actual);

// Test with blue color
        expected = "#0000FF";
        actual = tagTemplateCtrl.toRgbCode(Color.BLUE);
        assertEquals(expected, actual);

    }

}