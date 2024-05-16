package pt.uc.dei.student.tmdbts.search_engine.gateway;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GatewayCallback extends Remote {
    void notifyNewDataAvailable(String name, String message) throws RemoteException;
}
