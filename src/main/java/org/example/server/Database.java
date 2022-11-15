package org.example.server;

import com.mongodb.Block;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;
import org.example.server.table.DataRow;
import org.example.server.table.ErrorCode;
import org.example.server.table.TableHeader;

import java.util.*;
import java.util.function.Consumer;

public class Database {
    MongoDatabase database;
    Map<String, TableHeader> headers = new TreeMap<>();
    private ErrorCode successErrorCode = ErrorCode.UNINITIALIZED;
    private String message = "";

    public Database() {
        // Creating a Mongo client
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "dbpets",
                "notAVerySecretPassword".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        database = mongo.getDatabase("dbpets");
        System.out.println("Credentials ::"+ credential);
    }

    void addTable(String tableName, TableHeader header) {
        try {
            database.createCollection(tableName);
        } catch (RuntimeException e) {
            System.out.println("Table already exists.");
        }
        headers.put(tableName, header);
        message = "Table added successfully";
        successErrorCode = ErrorCode.SUCCESS_CODE;
    }

    void addEntry(String tableName, DataRow entry) {
        if (tableAbsent(tableName)) {
            successErrorCode = ErrorCode.TABLE_NOT_FOUND;
            return;
        }
        String data_row = entry.toString(getTableHeader(tableName));
        Document document = org.bson.Document.parse(data_row);
        MongoCollection<org.bson.Document> c = database.getCollection(tableName);
        c.insertOne(document);
        message = "Entry added successfully";
        successErrorCode = ErrorCode.SUCCESS_CODE;
    }

    public void addEntry(String tableName, List<String> entry) {
        List<String> headerTypes = getTableHeader(tableName).getHeaderTypes();
        addEntry(tableName, new DataRow(headerTypes, entry));
    }

    TableHeader getTableHeader(String tableName) {
        if (tableAbsent(tableName)) {
            successErrorCode = ErrorCode.TABLE_NOT_FOUND;
            return null;
        }
        successErrorCode = ErrorCode.SUCCESS_CODE;
        return headers.get(tableName);
    }

    List<String> listTables() {
        MongoIterable<String> tables = database.listCollectionNames();
        List<String> tables_list = new ArrayList<>();
        tables.forEach((Consumer<? super String>) tables_list::add);
        successErrorCode = ErrorCode.SUCCESS_CODE;
        message = "Tables list:\n";
        return tables_list;
    }

    List<DataRow> getEntries(String tableName, int n_entries) {
        if (tableAbsent(tableName)) {
            successErrorCode = ErrorCode.TABLE_NOT_FOUND;
            return new ArrayList<>();
        }

        MongoCollection<Document> collection = database.getCollection(tableName);
        TableHeader header = getTableHeader(tableName);
        List<DataRow> entries = new ArrayList<>();
        for(Document element: collection.find()) {
            entries.add(fromDocument(element, header));
            if (entries.size() >= n_entries) {
                break;
            }
            //System.out.println(element.toJson());
        }

        successErrorCode = ErrorCode.SUCCESS_CODE;
        return entries;
    }

    private DataRow fromDocument(Document document, TableHeader header) {
        List<Object> row = new ArrayList<>();
        for (String fieldName : header.getHeaderNames()) {
            row.add(document.get(fieldName));
        }
        return new DataRow(row);
    }

    List<DataRow> getIntersection(String tableNameA, String tableNameB, String keyFrom, String keyTo) {
        if (tableAbsent(tableNameA) || tableAbsent(tableNameB)) {
            successErrorCode = ErrorCode.TABLE_NOT_FOUND;
            return new ArrayList<>();
        }

        List<DataRow> intersection = new ArrayList<>();
        MongoCollection<org.bson.Document> dataA = database.getCollection(tableNameA);
        MongoCollection<org.bson.Document> dataB = database.getCollection(tableNameB);
        if (dataA.countDocuments() == 0 || dataB.countDocuments() == 0) {
            successErrorCode = ErrorCode.SUCCESS_CODE;
            message = "One or more the tables are empty; no intersection calculated";
            return new ArrayList<>();
        }
        for (Document entryA : dataA.find()) {
            for (Document entryB : dataB.find()) {
                if (!entryA.containsKey(keyFrom) || !entryB.containsKey(keyTo)) continue;
                if (entryA.get(keyFrom).equals(entryB.get(keyTo))) {
                    List<Object> rowA = fromDocument(entryA, headers.get(tableNameA)).getRow();
                    rowA.addAll(fromDocument(entryA, headers.get(tableNameA)).getRow());
                    intersection.add(new DataRow(rowA));
                }
            }
        }
        message = "Intersection found successfully";
        return intersection;
    }

    boolean tableAbsent(String tableName) {
        return !headers.containsKey(tableName);
    }

    public ErrorCode getSuccessErrorCode() {
        return successErrorCode;
    }

    public String getMessage() {
        return message;
    }

    public void clearMessage() {
        message = "";
    }
}
