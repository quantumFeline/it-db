package org.example.client;

import org.example.NetworkConnectorInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NetworkConnectorClient {

    NetworkConnectorInterface stub;

    public NetworkConnectorClient() {}

    public void startConnection() throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(null);
            stub = (NetworkConnectorInterface) registry.lookup("NCServer");
            System.out.println(stub.ping());

        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }

    public String sendQuery(String query) {
        try {
            return stub.sendQuery(query);
        } catch (RemoteException e) {
            System.out.println("Exception when sending query: " + e);
            e.printStackTrace();
            return "Error";
        }
    }
}
