package org.example.client;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.example.NetworkConnectorService;

public class NetworkConnectorClient {

    public NetworkConnectorClient() {}

    TTransport transport;
    NetworkConnectorService.Client client;

    public void startConnection() throws TException {
        transport = new TSocket("localhost", 9090);
        transport.open();

        TProtocol protocol = new  TBinaryProtocol(transport);
        client = new NetworkConnectorService.Client(protocol);
        System.out.println(client.ping());
    }

    public void endConnection() {
        transport.close();
    }

    public String sendQuery(String query) throws TException {
        return client.sendQuery(query);

    }
}
