package server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@WebMvcTest(SomeController.class)
public class SomeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SomeController someController;


    /**
     * @throws Exception thrown
     */
    @Test
    public void testGetPasswordMatch() throws Exception {
        String password = "password";
        String passcode = "password";
        when(someController.getPassword()).thenReturn(ResponseEntity.ok(true));
        mockMvc.perform(MockMvcRequestBuilders.get("/check-password"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    /**
     * @throws Exception thrown
     */
    @Test
    public void testGetPasswordNoMatch() throws Exception {
        String password = "password";
        String passcode = "passcode";
        when(someController.getPassword()).thenReturn(ResponseEntity.ok(false));
        mockMvc.perform(MockMvcRequestBuilders.get("/check-password"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }
}