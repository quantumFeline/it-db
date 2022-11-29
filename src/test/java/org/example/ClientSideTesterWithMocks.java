package org.example;

import org.example.client.NetworkConnectorClient;
import org.example.server.remote.NetworkConnectorServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ClientSideTesterWithMocks {

    NetworkConnectorClient client;
    NetworkConnectorServer mockServer;
    Registry mockRegistry;

    final String QUERY = "Hello";
    final String RESPONSE = "world";

    public static void main(String[] args) throws NotBoundException, RemoteException {
        ClientSideTesterWithMocks tester = new ClientSideTesterWithMocks();
        tester.setUp();
        tester.testConnectivity();
        System.out.println("Success");
    }

    public void setUp() throws NotBoundException, RemoteException {

        mockServer = mock(NetworkConnectorServer.class);
        when(mockServer.ping()).thenReturn("Hello, world!");
        when(mockServer.sendQuery(QUERY)).thenReturn(RESPONSE);

        mockRegistry = mock(Registry.class);
        client = new NetworkConnectorClient(mockRegistry);
        when(mockRegistry.lookup(client.getServerName())).thenReturn(mockServer);
    }

    public void testConnectivity() throws RemoteException {
        client.startConnection();
        assertEquals(client.sendQuery(QUERY), RESPONSE);
    }
}
