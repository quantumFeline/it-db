package org.example;

import org.example.server.Database;
import org.example.server.table.ErrorCode;
import org.example.server.QueryProcessor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryProcessorTest {

    QueryProcessor queryProcessor;
    Database database;

    @Before
    public void init() {
        database = new Database();
        queryProcessor = new QueryProcessor(database);
    }

    @Test
    public void unknownCommand() {
        queryProcessor.sendQuery("kitten a 4");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.UNKNOWN_COMMAND);
        assertEquals(queryProcessor.getLastError(), "Unknown command");
    }

    @Test
    public void tableAdd() {
        queryProcessor.sendQuery("add_table a x int");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        assertEquals(queryProcessor.getLastResult(), "Table added successfully\n");
    }

    @Test
    public void testIntersection() {
        queryProcessor.sendQuery("add_table a number int species string");
        queryProcessor.sendQuery("add_table b number int age float owner string");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        queryProcessor.sendQuery("add_entry a 4 kitten");
        queryProcessor.sendQuery("add_entry a 6 puppy");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        queryProcessor.sendQuery("add_entry b 4 0.15 Olga");
        queryProcessor.sendQuery("add_entry b 15 1.15 Anatoliy");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        queryProcessor.sendQuery("intersection a b number number");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        System.out.println(queryProcessor.getLastResult());
    }

    @Test
    public void tableAddUnknownType() {
        queryProcessor.sendQuery("add_table a x monkey");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.UNSUPPORTED_FIELD_TYPE);
        assertEquals(queryProcessor.getLastError(), "Unsupported field type");
    }


    @Test
    public void entryAdd() {
        queryProcessor.sendQuery("add_table a x int");
        queryProcessor.sendQuery("add_entry a 5");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        assertEquals(queryProcessor.getLastResult(), "Entry added successfully\n");
    }

    @Test
    public void viewEntries() {
        queryProcessor.sendQuery("add_table a x int");
        queryProcessor.sendQuery("add_entry a 5");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        assertEquals(queryProcessor.getLastResult(), "Entry added successfully\n");
        queryProcessor.sendQuery("select a 10");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        System.out.println(queryProcessor.getLastResult());
    }

    @Test
    public void viewEntriesNoSuchTable() {
        queryProcessor.sendQuery("add_table a x int");
        queryProcessor.sendQuery("add_entry a 5");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        assertEquals(queryProcessor.getLastResult(), "Entry added successfully\n");
        queryProcessor.sendQuery("select b 10");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.TABLE_NOT_FOUND);
    }
}

// Get rid of magic constants for success/error codes <- done
// TODO: and supported data types.
// TODO: Use returning error codes instead of throwing exceptions wherever applicable. <- in progress
// TODO: Serialization and deserialization.
// TODO: More detailed test cases.
// TODO: Header editing.
// Table intersection operation. <- done
// TODO: Cover other classes with tests.