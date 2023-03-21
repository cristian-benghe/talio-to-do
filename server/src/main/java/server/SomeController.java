package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class SomeController {
    /**
     * Handles GET requests to the URL of the application
     * @return a String containing "Hello word"!
     */
    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Hello world!";
    }
}