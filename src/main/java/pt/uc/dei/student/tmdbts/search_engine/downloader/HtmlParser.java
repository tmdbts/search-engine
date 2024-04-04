package pt.uc.dei.student.tmdbts.search_engine.downloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HtmlParser {
    /**
     * Get URLs from a given URL
     *
     * @param url URL of a website
     * @return List of URLs
     */
    public static List<Element> getURLs(String url) {
        List<Element> urls = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[href]");

            urls.addAll(links);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return urls;
    }

    public static List<String> getWords(String url) {
        List<String> words = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();
            StringTokenizer tokenizer = new StringTokenizer(document.text());
            int numberOfTokens = 0;

            while (tokenizer.hasMoreElements() && numberOfTokens++ < 100) {
                words.add(tokenizer.nextToken().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return words;
    }
}
