package pt.uc.dei.student.tmdbts.search_engine.client;

import pt.uc.dei.student.tmdbts.search_engine.gateway.TopTenSearches;

import java.util.ArrayList;

public class MonitorUpdate extends Monitor {
    private ArrayList<String> deactivatedBarrelsNames;

    public MonitorUpdate() {
        super(null, null, 0);
    }

    public MonitorUpdate(TopTenSearches topTenSearches, long averageResponseTime) {
        super(topTenSearches, averageResponseTime);
    }

    public MonitorUpdate(ArrayList<String> activeBarrels, long averageResponseTime) {
        super(activeBarrels, averageResponseTime);
    }

    public MonitorUpdate(TopTenSearches topTenSearches, ArrayList<String> activeBarrels, long averageResponseTime) {
        super(topTenSearches, activeBarrels, averageResponseTime);
    }

    public MonitorUpdate(TopTenSearches topTenSearches, ArrayList<String> activeBarrels, long averageResponseTime, ArrayList<String> deactivatedBarrelsNames) {
        super(topTenSearches, activeBarrels, averageResponseTime);
        this.deactivatedBarrelsNames = deactivatedBarrelsNames;
    }

    private void updateActiveBarrels(Monitor monitor) {
        for (String barrelName : getActiveBarrels()) {
            if (!monitor.getActiveBarrels().contains(barrelName)) {
                monitor.getActiveBarrels().add(barrelName);
            }

        }
    }

    private void updateDeactivatedBarrels(Monitor monitor) {
        for (String barrelName : deactivatedBarrelsNames) {
            monitor.getActiveBarrels().remove(barrelName);
        }
    }

    private void updateTopTenSearches(Monitor monitor) {
        Monitor newMonitor = new Monitor(monitor.getTopTenSearches(), monitor.getActiveBarrels(), monitor.getAverageResponseTime());

        for (Integer key : this.getTopTenSearches().getTop10Searches().keySet()) {
            newMonitor.getTopTenSearches().getTop10Searches().put(key, this.getTopTenSearches().getTop10Searches().get(key));
        }
    }

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
