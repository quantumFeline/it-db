package org.example.server.remote;

import org.example.NetworkConnectorInterface;
import org.example.server.Database;
import org.example.server.QueryProcessor;
import org.example.server.table.ErrorCode;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NetworkConnectorServer implements NetworkConnectorInterface {

    private static final String EXIT = "q";
    private static final Database database = new Database();
    private static final QueryProcessor queryProcessor = new QueryProcessor(database);

    public NetworkConnectorServer() {}

    @Override
    public String ping() {
        return "Hello, world!";
    }

    @Override
    public String sendQuery(String query) throws RemoteException {
        queryProcessor.sendQuery(query);
        String query_answer;
        if(queryProcessor.getSuccessErrorCode() != ErrorCode.SUCCESS_CODE) {
            query_answer = queryProcessor.getLastError();
        } else {
            query_answer = queryProcessor.getLastResult();
        }
        System.out.println(query_answer);
        return query_answer;
    }

    public static void main(String[] args) {

        try {
            NetworkConnectorServer obj = new NetworkConnectorServer();
            NetworkConnectorInterface stub = (NetworkConnectorInterface) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("NCServer", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }
}
