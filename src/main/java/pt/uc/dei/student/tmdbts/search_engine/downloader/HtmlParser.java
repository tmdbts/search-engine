package pt.uc.dei.student.tmdbts.search_engine.downloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * HTML Parser class
 */
public class HtmlParser {
    /**
     * Words to be ignored. These are common words that do not add value to the search.
     */
    private static final HashSet<String> DISCARD_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "the", "and", "are", "or", "but", "for", "nor", "yet", "so",
            "in", "on", "at", "by", "to", "from", "into", "onto", "of", "off",
            "over", "under", "with", "within", "without", "between", "among",
            "through", "during", "before", "after", "since", "until", "while",
            "about", "above", "below", "beside", "beneath", "behind", "between",
            "beyond", "down", "except", "inside", "instead", "like", "near", "next",
            "against", "along", "amid", "around", "above", "below", "beside",
            "between", "beyond", "through", "throughout", "till", "toward",
            "towards", "under", "underneath", "until", "unto", "upon", "via",
            "whether", "with", "within", "without", "would", "could", "should",
            "might", "must", "shall", "will", "can", "not", "do", "did", "does",
            "done", "doing", "had", "has", "have", "having", "here", "there",
            "where", "when", "how", "why", "who", "whom", "whose", "which",
            "what", "this", "that", "these", "those"
    ));

    /**
     * Characters to be ignored. These are characters that are not part of words.
     */
    private static final Set<Character> INVALID_CHARACTERS = new HashSet<>(Arrays.asList(
            '.', '/', '?', '(', ')', '[', ']', '{', '}', ';', ':', '<', '>', ',', '"', '*'
    ));

    /**
     * Get URLs from a given URL
     * <p>
     * This method will fetch the URLs from a given website. It will return a list of URLs.
     * <p>
     * This method uses the Jsoup library to fetch the URLs.
     * <p>
     * The URLs are fetched from the "a" tags with the "href" attribute.
     * <p>
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
            System.out.println("Error fetching URL: " + e.getMessage());
        }

        return urls;
    }

    /**
     * Get the title and description of a website
     * <p>
     * This method will fetch the title and description of a website. It will return a list with the title and description respectively.
     * <p>
     * This method uses the Jsoup library to fetch the title and description.
     * <p>
     * The title is fetched from the "title" tag. The description is fetched from the "meta" tag with the "name" attribute set to "description".
     *
     * @param url URL of the website
     * @return List with the title and description respectively
     */
    public static ArrayList<String> getHead(String url) {
        ArrayList<String> head = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();

            head.add(document.title());

            Elements description = document.select("meta[name=description]");
            for (Element element : description) {
                String theDescription = element.attr("content");
                head.add(theDescription);
            }
        } catch (IOException e) {
            System.out.println("Error fetching URL: " + e.getMessage());
        }

        return head;
    }

    /**
     * Get the words from a website
     * <p>
     * This method will fetch the words from a website. It will return a list of words.
     * <p>
     * The words are cleaned from invalid characters and common words. The words are also converted to lowercase.
     * <p>
     * This method uses the Jsoup library to fetch the words.
     *
     * @param url URL of the website
     * @return List of words
     */
    public static ArrayList<String> getWords(String url) {
        ArrayList<String> words = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();
            StringTokenizer tokenizer = new StringTokenizer(document.text());

            while (tokenizer.hasMoreElements()) {
                String current = tokenizer.nextToken().toLowerCase();

                if (current.length() < 3 || isDiscardWord(current)) continue;

                words.add(cleanWord(current));
            }

        } catch (IOException e) {
            System.out.println("Error fetching URL: " + e.getMessage());
        }

        return words;
    }

    /**
     * Clean a word from invalid characters.
     *
     * @param word Word to clean
     * @return Cleaned word
     */
    private static String cleanWord(String word) {
        String cleanedWord = word;

        for (char c : INVALID_CHARACTERS) {
            cleanedWord = cleanedWord.replace(c, ' ');
        }

        cleanedWord = cleanedWord.trim();

        return cleanedWord;
    }

    /**
     * Check if a word is part of the discard list
     *
     * @param word Word to check
     * @return True if the word is part of the discard list, false otherwise
     */
    private static boolean isDiscardWord(String word) {
        return DISCARD_WORDS.contains(word);
    }
}
