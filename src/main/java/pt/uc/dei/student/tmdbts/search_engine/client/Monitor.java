package pt.uc.dei.student.tmdbts.search_engine.client;

import pt.uc.dei.student.tmdbts.search_engine.gateway.TopTenSearches;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This represents the status monitor of the system
 */
public class Monitor implements Serializable {
    /**
     * Top 10 searches
     */
    private TopTenSearches topTenSearches;

    /**
     * Active barrels
     */
    private ArrayList<String> activeBarrels;

    /**
     * Average response time
     */
    private long averageResponseTime;

    /**
     * Constructor
     */
    public Monitor() {
        this.topTenSearches = null;
        this.activeBarrels = null;
        this.averageResponseTime = 0;
    }

    /**
     * Constructor
     *
     * @param topTenSearches      top 10 searches
     * @param averageResponseTime average response time
     */
    public Monitor(TopTenSearches topTenSearches, long averageResponseTime) {
        this.topTenSearches = topTenSearches;
        this.averageResponseTime = averageResponseTime;
    }

    /**
     * Constructor
     *
     * @param activeBarrels       active barrels
     * @param averageResponseTime average response time
     */
    public Monitor(ArrayList<String> activeBarrels, long averageResponseTime) {
        this.activeBarrels = activeBarrels;
        this.averageResponseTime = averageResponseTime;
    }

    /**
     * Constructor
     *
     * @param topTenSearches      top 10 searches
     * @param activeBarrels       active barrels
     * @param averageResponseTime average response time
     */
    public Monitor(TopTenSearches topTenSearches, ArrayList<String> activeBarrels, long averageResponseTime) {
        this.topTenSearches = topTenSearches;
        this.activeBarrels = activeBarrels;
        System.out.println("HERE " + activeBarrels);
        this.averageResponseTime = averageResponseTime;
    }

    public TopTenSearches getTopTenSearches() {
        return topTenSearches;
    }

    public void setTopTenSearches(TopTenSearches topTenSearches) {
        this.topTenSearches = topTenSearches;
    }

    public ArrayList<String> getActiveBarrels() {
        return activeBarrels;
    }

    public void setActiveBarrels(ArrayList<String> activeBarrels) {
        this.activeBarrels = activeBarrels;
    }

    public long getAverageResponseTime() {
        return averageResponseTime;
    }

    public void setAverageResponseTime(long averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }

    @Override
    public String toString() {
        return "Top 10 searches:\n" +
                topTenSearches.getTop10Searches().toString() +
                "\nActive barrels:\n" +
                activeBarrels.toString() +
                "\nAverage response time: " +
                averageResponseTime;
    }
}
