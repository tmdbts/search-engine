package pt.uc.dei.student.tmdbts.search_engine.services;

/**
 * Hacker News service
 */
public class HackerNewsService {
    /**
     * Hacker News API URL
     */
    String hackerNewsUrl = "https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty";

    public String getHackerNewsUrl() {
        return hackerNewsUrl;
    }

    public void setHackerNewsUrl(String hackerNewsUrl) {
        this.hackerNewsUrl = hackerNewsUrl;
    }
}
