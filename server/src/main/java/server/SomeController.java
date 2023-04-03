package server;

import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Random;

@Controller
@RequestMapping("/")
public class SomeController {

    private String password;
    private String inputPasscode;

    /**
     * Method which generates a random password of 7 characters
     * which consist of uppercase letters and numbers
     */
    @Bean
    public void generatePassword() {
        int passwordLength = 7;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder password = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        System.out.println("\n\n\u001B[33m The password for the admin is -> " +
                password.toString() + "\u001B[0m\n\n");

        this.password = password.toString();
    }

    /**
     * Handles GET requests to the URL of the application
     * @return a String containing "Hello world"!
     */
    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Hello world!";
    }

    /**
     * POST request handler in the server side
     * @param passcode to be added for further check
     * @return the passcode as a ResponseEntity
     */
    @PostMapping("/add-password")
    @ResponseBody
    public ResponseEntity<String> addPassword(@RequestBody String passcode){
        inputPasscode = passcode;
        return ResponseEntity.ok(passcode);
    }

    /**
     * GET request that checks if the passcodes match
     * @return true if they match, false else
     */
    @GetMapping("/check-password")
    @ResponseBody
    public ResponseEntity<Boolean> getPassword() {
        return ResponseEntity.ok(Objects.equals(inputPasscode, password));
    }

//    /**
//     * Used for refreshing availableUserBoards
//     * @param integer random
//     * @return param integer
//     */
//    @MessageMapping("/refresh") // app/refresh
//    @SendTo("/topic/refresh")
//    public Integer addMessageRefresh(Integer integer) {
//        return integer;
//    }
}