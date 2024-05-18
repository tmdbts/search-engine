package pt.uc.dei.student.tmdbts.search_engine.client;

import pt.uc.dei.student.tmdbts.search_engine.gateway.TopTenSearches;

import java.io.Serializable;
import java.util.ArrayList;

public class Monitor implements Serializable {
    private TopTenSearches topTenSearches;

    private ArrayList<String> activeBarrels;

    private long averageResponseTime;

    public Monitor() {
        this.topTenSearches = null;
        this.activeBarrels = null;
        this.averageResponseTime = 0;
    }

    public Monitor(TopTenSearches topTenSearches, long averageResponseTime) {
        this.topTenSearches = topTenSearches;
        this.averageResponseTime = averageResponseTime;
    }

    public Monitor(ArrayList<String> activeBarrels, long averageResponseTime) {
        this.activeBarrels = activeBarrels;
        this.averageResponseTime = averageResponseTime;
    }

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
