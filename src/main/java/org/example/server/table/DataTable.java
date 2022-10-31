package org.example.server.table;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class DataTable {
    String tableName;
    TableHeader header;
    List <org.example.server.table.DataRow> entries;

    public DataTable(String name, org.example.server.table.TableHeader header) {
        this.tableName = name;
        this.header = header;
        entries = new ArrayList<>();
    }

    public void addEntry(org.example.server.table.DataRow entry) {
        if (!verifyEntryFormat(entry)) {
            throw new InvalidParameterException();
        }
        entries.add(entry);
    }

    public List<org.example.server.table.DataRow> getEntries(int n_entries) {
        return entries.size() < n_entries? entries : entries.subList(0, n_entries);
    }

    public int getSize() {
        return entries.size();
    }

    protected boolean verifyEntryFormat(org.example.server.table.DataRow entry) {
        return entry.getTypes().equals(header.getHeaderTypes());
    }

    @Override
    public String toString() {
        return "Table " + tableName + ": " + header ;
    }

    public org.example.server.table.TableHeader getHeader() {
        return header;
    }

    public Object getField(int row_i, String field) {
        org.example.server.table.DataRow row = entries.get(row_i);
        for (int i = 0; i < header.getHeaderNames().size(); i++) {
            if (header.getHeaderNames().get(i).equals(field)) {
                return row.getRow().get(i);
            }
        }
        return new Object();
    }
}
