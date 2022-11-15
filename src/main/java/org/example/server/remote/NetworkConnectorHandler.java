package org.example.server.remote;

import org.apache.thrift.TException;
import org.example.NetworkConnectorService;
import org.example.server.Database;
import org.example.server.QueryProcessor;
import org.example.server.table.ErrorCode;

public class NetworkConnectorHandler implements NetworkConnectorService.Iface {

    private static final String EXIT = "q";
    private static final Database database = new Database();
    private static final QueryProcessor queryProcessor = new QueryProcessor(database);

    @Override
    public String ping() throws TException {
        return "Connection established";
    }

    @Override
    public String sendQuery(String query) throws TException {
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
}