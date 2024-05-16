package pt.uc.dei.student.tmdbts.search_engine.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("message", "Welcome to the Googol Search Engine");
        return "index";
    }

    @PostMapping("/index")
    public String indexUrl(@RequestParam String url, Model model) {

        model.addAttribute("results", "URL successfully indexed: " + url);
        return "index";
    }

    @PostMapping("/search")
    public String searchUrl(@RequestParam String query, Model model) {

        model.addAttribute("results", "Searchs results for: " + query);


        return "search";
    }
}
