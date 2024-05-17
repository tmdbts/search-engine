package pt.uc.dei.student.tmdbts.search_engine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pt.uc.dei.student.tmdbts.search_engine.models.Query;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.SearchResult;
import pt.uc.dei.student.tmdbts.search_engine.webserver.WebServerImpl;

@Controller
public class WebController {

    private final WebServerImpl webServer;

    @Autowired
    public WebController(WebServerImpl webServer) {
        this.webServer = webServer;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("query", new Query());
        return "index";
    }

    @PostMapping("/search")
    public String searchUrl(@ModelAttribute Query query, Model model) {
        String queryStr = query.getQuery();
        System.out.println("Query: " + queryStr);
        try {
            SearchResult searchResult = webServer.searchQuery(queryStr);
            model.addAttribute("result", searchResult);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("result", "Error occurred: " + e.getMessage());
        }
        return "redirect:/search";
    }
}
