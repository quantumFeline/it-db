package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NetworkConnectorInterface extends Remote {
    String ping() throws RemoteException;

    String sendQuery(String query) throws RemoteException;
}
