package org.example.server;

import org.example.server.table.DataRow;
import org.example.server.table.DataTable;
import org.example.server.table.ErrorCode;
import org.example.server.table.TableHeader;

import java.util.*;
import java.util.stream.Collectors;

public class Database {
    Map<String, DataTable> tables = new TreeMap<>();
    private ErrorCode successErrorCode = ErrorCode.UNINITIALIZED;
    private String message = "";

    void addTable(String tableName, TableHeader header) {
        tables.put(tableName, new DataTable(tableName, header));
        message = "Table added successfully";
        successErrorCode = ErrorCode.SUCCESS_CODE;
    }

    void addEntry(String tableName, DataRow entry) {
        if (tableAbsent(tableName)) {
            successErrorCode = ErrorCode.TABLE_NOT_FOUND;
            return;
        }

        tables.get(tableName).addEntry(entry);
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
        return tables.get(tableName).getHeader();
    }

    List<String> listTables() {
        List<String> tables_list = tables.values().stream().map(DataTable::toString).collect(Collectors.toList());
        successErrorCode = ErrorCode.SUCCESS_CODE;
        message = "Tables list:\n";
        return tables_list;
    }

    List<DataRow> getEntries(String tableName, int n_entries) {
        if (tableAbsent(tableName)) {
            successErrorCode = ErrorCode.TABLE_NOT_FOUND;
            return new ArrayList<>();
        }

        successErrorCode = ErrorCode.SUCCESS_CODE;
        return tables.get(tableName).getEntries(n_entries);
    }

    List<DataRow> getIntersection(String tableNameA, String tableNameB, String keyFrom, String keyTo) {
        if (tableAbsent(tableNameA) || tableAbsent(tableNameB)) {
            successErrorCode = ErrorCode.TABLE_NOT_FOUND;
            return new ArrayList<>();
        }

        List<DataRow> intersection = new ArrayList<>();
        List<DataRow> dataA = getEntries(tableNameA, Integer.MAX_VALUE);
        List<DataRow> dataB = getEntries(tableNameB, Integer.MAX_VALUE);
        if (dataA.isEmpty() || dataB.isEmpty()) {
            successErrorCode = ErrorCode.SUCCESS_CODE;
            message = "One or more the tables are ampty; no intersection calculated";
            return new ArrayList<>();
        }
        for (int ia = 0; ia < tables.get(tableNameA).getSize(); ia++) {
            for (int ib = 0; ib < tables.get(tableNameA).getSize(); ib++) {
                if (getField(tableNameA, ia, keyFrom).equals(getField(tableNameB, ib, keyTo))) {
                    List<Object> rowA = getRow(tableNameA, ia).getRow();
                    rowA.addAll(getRow(tableNameB, ib).getRow());
                    intersection.add(new DataRow(rowA));
                }
            }
        }
        message = "Intersection found successfully";
        return intersection;
    }

    boolean tableAbsent(String tableName) {
        return !tables.containsKey(tableName);
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

    private Object getField(String tableName, int row_i, String field) {
        if (tableAbsent(tableName)) {
            return new Object();
        }
        return tables.get(tableName).getField(row_i, field);
    }

    private DataRow getRow(String tableName, int row_i) {
        if (tableAbsent(tableName)) {
            return new DataRow(new ArrayList<>());
        }
        return tables.get(tableName).getEntries(Integer.MAX_VALUE).get(row_i);
    }
}
