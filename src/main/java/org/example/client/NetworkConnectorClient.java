package org.example.client;

import org.example.NetworkConnectorInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NetworkConnectorClient {

    NetworkConnectorInterface stub;
    Registry registry = null;
    final String SERVER_NAME = "NCServer";

    public NetworkConnectorClient() {}

    public NetworkConnectorClient(Registry registry) {
        this.registry = registry;
    }

    public void startConnection() throws RemoteException {
        try {
            if (registry == null) {
                registry = LocateRegistry.getRegistry(null);
            }
            stub = (NetworkConnectorInterface) registry.lookup(SERVER_NAME);
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

    public String getServerName() {
        return SERVER_NAME;
    }
}
