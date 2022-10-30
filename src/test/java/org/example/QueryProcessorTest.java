package org.example;

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
        assertEquals(queryProcessor.getLastResult(), "Table added successfully");
    }

    @Test
    public void tableAddUnknownType() {
        queryProcessor.sendQuery("add_table a x monkey");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        assertEquals(queryProcessor.getLastResult(), "Table added successfully");
    }


    @Test
    public void entryAdd() {
        queryProcessor.sendQuery("add_table a x int");
        queryProcessor.sendQuery("add_entry a 5");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        assertEquals(queryProcessor.getLastResult(), "Entry added successfully");
    }

    @Test
    public void viewEntries() {
        queryProcessor.sendQuery("add_table a x int");
        queryProcessor.sendQuery("add_entry a 5");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        assertEquals(queryProcessor.getLastResult(), "Entry added successfully");
        queryProcessor.sendQuery("select a 10");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        System.out.println(queryProcessor.getLastResult());
    }

    @Test
    public void viewEntriesNoSuchTable() {
        queryProcessor.sendQuery("add_table a x int");
        queryProcessor.sendQuery("add_entry a 5");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.SUCCESS_CODE);
        assertEquals(queryProcessor.getLastResult(), "Entry added successfully");
        queryProcessor.sendQuery("select b 10");
        assertEquals(queryProcessor.getSuccessErrorCode(), ErrorCode.TABLE_NOT_FOUND);
    }
}

// TODO: Get rid of magic constants for success/error codes and supported data types.
// TODO: Use returning error codes instead of throwing exceptions wherever applicable.
// TODO: Serialization and deserialization.
// TODO: More detailed test cases.
// TODO: Header editing.
// TODO: Table intersection operation.
// TODO: Cover other classes with tests.