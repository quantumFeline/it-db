package org.example.server.remote;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.example.NetworkConnectorService;

public class NetworkConnectorServer {

    public static NetworkConnectorHandler handler;

    public static NetworkConnectorService.Processor<NetworkConnectorHandler> processor;

    public static void main(String [] args) {
        try {
            handler = new NetworkConnectorHandler();
            processor = new NetworkConnectorService.Processor<>(handler);

            Runnable simple = () -> simple(processor);
            new Thread(simple).start();
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }

    public static void simple(NetworkConnectorService.Processor<NetworkConnectorHandler> processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

            System.out.println("Starting the simple server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}