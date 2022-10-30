package org.example;

import org.example.table.ColumnHeader;
import org.example.table.DataRow;
import org.example.table.DataTable;
import org.example.table.TableHeader;

import java.util.*;
import java.util.stream.Collectors;

public class Database {
    Map<String, DataTable> tables = new TreeMap<>();
    private ErrorCode successErrorCode = ErrorCode.UNINITIALIZED;

    void addTable(String tableName, TableHeader header) {
        tables.put(tableName, new DataTable(tableName, header));
        successErrorCode = ErrorCode.SUCCESS_CODE;
    }

    void addEntry(String tableName, DataRow entry) {
        if (!tables.containsKey(tableName)) {
            successErrorCode = ErrorCode.TABLE_NOT_FOUND;
            return;
        }

        tables.get(tableName).addEntry(entry);
        successErrorCode = ErrorCode.SUCCESS_CODE;
    }

    public void addEntry(String tableName, List<Object> entry) {
        addEntry(tableName, new DataRow(entry));
    }

    TableHeader getTableHeader(String tableName) {
        if (!tables.containsKey(tableName)) {
            successErrorCode = ErrorCode.TABLE_NOT_FOUND;
            return null;
        }

        successErrorCode = ErrorCode.SUCCESS_CODE;
        return tables.get(tableName).getHeader();
    }

    List<String> listTables() {
        List<String> tables_list = tables.values().stream().map(DataTable::toString).collect(Collectors.toList());
        successErrorCode = ErrorCode.SUCCESS_CODE;
        return tables_list;
    }

    List<DataRow> getEntries(String tableName, int n_entries) {
        if (!tables.containsKey(tableName)) {
            successErrorCode = ErrorCode.TABLE_NOT_FOUND;
            return new ArrayList<>();
        }

        successErrorCode = ErrorCode.SUCCESS_CODE;
        return tables.get(tableName).getEntries(n_entries);
    }

    public ErrorCode getSuccessErrorCode() {
        return successErrorCode;
    }
}
