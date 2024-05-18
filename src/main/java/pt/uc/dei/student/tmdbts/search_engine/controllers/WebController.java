package pt.uc.dei.student.tmdbts.search_engine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.uc.dei.student.tmdbts.search_engine.client.Monitor;
import pt.uc.dei.student.tmdbts.search_engine.models.Query;
import pt.uc.dei.student.tmdbts.search_engine.storage_barrels.SearchResult;
import pt.uc.dei.student.tmdbts.search_engine.webserver.WebServerImpl;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;

/**
 * Web controller
 */
@Controller
public class WebController {
    /**
     * Web server
     */
    private final WebServerImpl webServer;

    /**
     * Search result
     */
    private SearchResult searchResult;
  
    private String queryStr;

    /**
     * Constructor
     *
     * @param webServer web server
     */
    @Autowired
    public WebController(WebServerImpl webServer) {
        this.webServer = webServer;
    }

    /**
     * Get request for index
     *
     * @param model model
     * @return index
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("query", new Query());
        return "index";
    }

    /**
     * Post request for search. Makes a search query
     *
     * @param query query
     * @param model model
     * @return index
     * @throws RemoteException remote exception
     */
    @PostMapping("/search")
    public String searchUrl(@ModelAttribute Query query, Model model) throws RemoteException {
        queryStr = query.getQuery();
        System.out.println("Query: " + queryStr);
        if (queryStr.startsWith("https://")) {
            URI url = null;
            try {
                url = new URI(queryStr);
            } catch (URISyntaxException e) {
                System.out.println("INVALID URL: " + queryStr);
                return "index";
            }
            if (url != null) {
                webServer.addURL(url);
                return "index";
            }
        } else {
            try {
                searchResult = webServer.searchQuery(queryStr);

                model.addAttribute("result", searchResult);
                model.addAttribute("url", searchResult.getResults());

                return "search";
            } catch (Exception e) {
                e.printStackTrace();
                //model.addAttribute("result", "Error occurred: " + e.getMessage());
                return "search";
            }
        }

        return "index";
    }

    @GetMapping("/search")
    public String searchMore10(@RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex, Model model) throws RemoteException {
        try {
            searchResult = webServer.searchQuery(pageIndex);
            System.out.println("aqui " + pageIndex);
            model.addAttribute("result", searchResult);
            model.addAttribute("url", searchResult.getResults());
            model.addAttribute("pageIndex", pageIndex);
        } catch (Exception e) {
            e.printStackTrace();
            //model.addAttribute("result", "Error occurred: " + e.getMessage());
        }

        return "search";
    }


    /**
     * Get request for monitor
     *
     * @param model model
     * @return search
     */
    @GetMapping("/monitor")
    public String monitor(@ModelAttribute Monitor monitor, Model model) {
        System.out.println(monitor);
        model.addAttribute("monitor", monitor);
        return "monitor";
    }

    @MessageMapping("/monitor")
    @SendTo("/topic/monitor")
    public Monitor onMonitor(Monitor monitor) {
        System.out.println("Received message: " + monitor);

        return monitor;
    }
}