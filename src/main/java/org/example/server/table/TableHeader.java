package org.example.server.table;

import javax.xml.bind.TypeConstraintException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableHeader {
    List<org.example.server.table.ColumnHeader> columnHeaderList;

    public TableHeader(List<String> headerList) {
        columnHeaderList = new ArrayList<>();
        for (int i = 0; i < headerList.size(); i+= 2) {
            String typename;
            switch(headerList.get(i+1)) {
                case "int":
                    typename = "java.lang.Integer";
                    break;
                case "float":
                    typename = "java.lang.Float";
                    break;
                case "string":
                    typename = "java.lang.String";
                    break;
                default:
                    typename = "";
                    throw new TypeConstraintException("Only support int, float, string");
            }
            columnHeaderList.add(new org.example.server.table.ColumnHeader(headerList.get(i), typename));
        }
    }

    public List<String> getHeaderTypes() {
        return columnHeaderList.stream().map(org.example.server.table.ColumnHeader::getType).collect(Collectors.toList());
    }

    public List<String> getHeaderNames() {
        return columnHeaderList.stream().map(org.example.server.table.ColumnHeader::getName).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "{" + columnHeaderList + "}";
    }
}
