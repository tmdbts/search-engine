package pt.uc.dei.student.tmdbts.search_engine.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.InputSource;
import pt.uc.dei.student.tmdbts.search_engine.services.HackerNewsService;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

@Controller
public class HackerNewsController {
    private HackerNewsService hackerNewsService;

    @Autowired
    public HackerNewsController(HackerNewsService hackerNewsService) {
        this.hackerNewsService = hackerNewsService;
    }

    public ArrayList<URI> getTopStories(String query) {
        RestTemplate restTemplate = new RestTemplate();
        String endpoint = "https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty";

        ResponseEntity<String> res = restTemplate.getForEntity(endpoint, String.class);

        assert res.getStatusCode().equals(HttpStatus.OK);

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode jsonRes = mapper.readTree(res.getBody());
                System.out.println("Got URLs: ");
            for (JsonNode node : jsonRes) {
                URL uri = new URL("https://hacker-news.firebaseio.com/v0/item/" + node.asText() + ".json?print=pretty");

                System.out.println(uri.toURI());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL: " + e.getMessage());
        } catch (URISyntaxException e) {
            System.out.println("Invalid URI: " + e.getMessage();
        }

        return new ArrayList<>();
    }

    @GetMapping("/hackernews")
    public ArrayList<URI> dontUse() {
        ArrayList<URI> topStories = new ArrayList<>();

        try {
            URL endpoint = new URL("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");

            HttpURLConnection conn = (HttpURLConnection) endpoint.openConnection();

            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Content-Type", "application/json");

            InputSource res = new InputSource(conn.getInputStream());
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error opening connection: " + e.getMessage());
        }
        return topStories;
    }

    @Autowired
    public void setHackerNewsService(HackerNewsService hackerNewsService) {
        this.hackerNewsService = hackerNewsService;
    }
}
