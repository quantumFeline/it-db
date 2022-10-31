package org.example.table;

import org.example.Database;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class DataTable {
    String tableName;
    TableHeader header;
    List <DataRow> entries;

    public DataTable(String name, TableHeader header) {
        this.tableName = name;
        this.header = header;
        entries = new ArrayList<>();
    }

    public void addEntry(DataRow entry) {
        if (!verifyEntryFormat(entry)) {
            throw new InvalidParameterException();
        }
        entries.add(entry);
    }

    public List<DataRow> getEntries(int n_entries) {
        return entries.size() < n_entries? entries : entries.subList(0, n_entries);
    }

    public int getSize() {
        return entries.size();
    }

    protected boolean verifyEntryFormat(DataRow entry) {
        return entry.getTypes().equals(header.getHeaderTypes());
    }

    @Override
    public String toString() {
        return "Table " + tableName + ": " + header ;
    }

    public TableHeader getHeader() {
        return header;
    }

    public Object getField(int row_i, String field) {
        DataRow row = entries.get(row_i);
        for (int i = 0; i < header.getHeaderNames().size(); i++) {
            if (header.getHeaderNames().get(i).equals(field)) {
                return row.getRow().get(i);
            }
        }
        return new Object();
    }
}
