package pt.uc.dei.student.tmdbts.search_engine.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    void updateMonitor(MonitorUpdate monitorUpdate) throws RemoteException;
}
