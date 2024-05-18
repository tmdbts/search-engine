package pt.uc.dei.student.tmdbts.search_engine.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * This represents a Hacker News item.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record HackerNews(
        Integer id, // The item's unique id.
        Boolean deleted, // true if the item is deleted.
        String type, // The type of item. One of "job", "story", "comment", "poll", or "pollopt".
        String by, // The username of the item's author.
        Long time, // Creation date of the item, in Unix Time.
        String text, // The comment, story or poll text. HTML.
        Boolean dead, // true if the item is dead.
        String parent, // The comment's parent: either another comment or the relevant story.
        Integer poll, // The pollopt's associated poll.
        List kids, // The ids of the item's comments, in ranked display order.
        String url, // The URL of the story.
        Integer score, // The story's score, or the votes for a pollopt.
        String title, // The title of the story, poll or job. HTML.
        List parts, // A list of related pollopts, in display order.
        Integer descendants // In the case of stories or polls, the total comment count.
) {
}
