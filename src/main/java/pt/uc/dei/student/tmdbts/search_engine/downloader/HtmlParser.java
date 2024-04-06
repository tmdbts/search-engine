package pt.uc.dei.student.tmdbts.search_engine.downloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class HtmlParser {
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

    private static final Set<Character> INVALID_CHARACTERS = new HashSet<>(Arrays.asList(
            '.', '/', '?', '(', ')', '[', ']', '{', '}', ';', ':', '<', '>', ',', '"', '*'
    ));


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
            e.printStackTrace();
        }

        return words;
    }

    private static String cleanWord(String word) {
        String cleanedWord = word;

        for (char c : INVALID_CHARACTERS) {
            cleanedWord = cleanedWord.replace(c, ' ');
        }

        cleanedWord = cleanedWord.trim();

        return cleanedWord;
    }

    private static boolean isInvalidCharacter(char c) {
        return INVALID_CHARACTERS.contains(c);
    }

    private static boolean isDiscardWord(String word) {
        return DISCARD_WORDS.contains(word);
    }
}
