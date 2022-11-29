package org.example;

import org.example.server.Database;
import org.example.server.QueryProcessor;
import org.example.server.table.DataRow;
import org.example.server.table.ErrorCode;
import org.example.server.table.TableHeader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class QueryProcessorTesterWithMocks {

    QueryProcessor queryProcessor;
    Database databaseMock;

    public static void main(String[] args) {
        QueryProcessorTesterWithMocks tester = new QueryProcessorTesterWithMocks();
        tester.setUp();
        tester.testQuerySending();
        System.out.println("Success");
    }

    public void setUp() {
        databaseMock = mock(Database.class);
        queryProcessor = new QueryProcessor(databaseMock);
    }

    public void testQuerySending() {
        List<DataRow> tableContents = new ArrayList<>();
        List<Object> row = new ArrayList<>(Arrays.asList("dog", 5));
        tableContents.add(new DataRow(row));
        String message = "Returned 5 entries for x";

        when(databaseMock.getEntries("x", 5)).thenReturn(tableContents);
        when(databaseMock.getTableHeader("x")).thenReturn(new TableHeader(
                Arrays.asList("species", "string","age","int")));
        when(databaseMock.getMessage()).thenReturn(message);

        queryProcessor.sendQuery("select x 5");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        assertEquals(message +
                "\nx\n[species, age]\n" +
                "{ dog 5 }\n" +
                "\n", queryProcessor.getLastResult());
    }
}
