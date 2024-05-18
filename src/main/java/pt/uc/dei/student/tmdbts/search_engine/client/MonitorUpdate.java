package pt.uc.dei.student.tmdbts.search_engine.client;

import pt.uc.dei.student.tmdbts.search_engine.gateway.TopTenSearches;

import java.util.ArrayList;

/**
 * This represents an update to the status monitor of the system
 */
public class MonitorUpdate extends Monitor {
    /**
     * Deactivated barrels
     */
    private ArrayList<String> deactivatedBarrelsNames;

    /**
     * Constructor
     */
    public MonitorUpdate() {
        super(null, null, 0);
    }

    /**
     * Constructor
     *
     * @param topTenSearches      top 10 searches
     * @param averageResponseTime average response time
     */
    public MonitorUpdate(TopTenSearches topTenSearches, long averageResponseTime) {
        super(topTenSearches, averageResponseTime);
    }

    /**
     * Constructor
     *
     * @param activeBarrels       active barrels
     * @param averageResponseTime average response time
     */
    public MonitorUpdate(ArrayList<String> activeBarrels, long averageResponseTime) {
        super(activeBarrels, averageResponseTime);
    }

    /**
     * Constructor
     *
     * @param topTenSearches      top 10 searches
     * @param activeBarrels       active barrels
     * @param averageResponseTime average response time
     */
    public MonitorUpdate(TopTenSearches topTenSearches, ArrayList<String> activeBarrels, long averageResponseTime) {
        super(topTenSearches, activeBarrels, averageResponseTime);
    }

    /**
     * Constructor
     *
     * @param topTenSearches          top 10 searches
     * @param activeBarrels           active barrels
     * @param averageResponseTime     average response time
     * @param deactivatedBarrelsNames deactivated barrels
     */
    public MonitorUpdate(TopTenSearches topTenSearches, ArrayList<String> activeBarrels, long averageResponseTime, ArrayList<String> deactivatedBarrelsNames) {
        super(topTenSearches, activeBarrels, averageResponseTime);
        this.deactivatedBarrelsNames = deactivatedBarrelsNames;
    }

    /**
     * Update active barrels
     */
    private void updateActiveBarrels(Monitor monitor) {
        for (String barrelName : getActiveBarrels()) {
            if (!monitor.getActiveBarrels().contains(barrelName)) {
                monitor.getActiveBarrels().add(barrelName);
            }

        }
    }

    /**
     * Update deactivated barrels
     */
    private void updateDeactivatedBarrels(Monitor monitor) {
        for (String barrelName : deactivatedBarrelsNames) {
            monitor.getActiveBarrels().remove(barrelName);
        }
    }

    /**
     * Update top ten searches
     */
    private void updateTopTenSearches(Monitor monitor) {
        Monitor newMonitor = new Monitor(monitor.getTopTenSearches(), monitor.getActiveBarrels(), monitor.getAverageResponseTime());

        for (Integer key : this.getTopTenSearches().getTop10Searches().keySet()) {
            newMonitor.getTopTenSearches().getTop10Searches().put(key, this.getTopTenSearches().getTop10Searches().get(key));
        }
    }

    /**
     * Update monitor
     *
     * @param monitor monitor
     */
    public void updateMonitor(Monitor monitor) {
        if (!getActiveBarrels().isEmpty()) {
            updateActiveBarrels(monitor);
        }

        if (deactivatedBarrelsNames != null && !deactivatedBarrelsNames.isEmpty()) {
            updateDeactivatedBarrels(monitor);
        }

        if (getTopTenSearches() != null) {
            updateTopTenSearches(monitor);
        }

        if (getAverageResponseTime() != 0)
            monitor.setAverageResponseTime(getAverageResponseTime());
    }
}
