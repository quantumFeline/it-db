package org.example.table;

import com.sun.org.apache.bcel.internal.generic.ObjectType;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableHeader {
    List<ColumnHeader> columnHeaderList;

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
            }
            columnHeaderList.add(new ColumnHeader(headerList.get(i), typename));
        }
    }

    public List<String> getHeaderTypes() {
        return columnHeaderList.stream().map(ColumnHeader::getType).collect(Collectors.toList());
    }

    public List<String> getHeaderNames() {
        return columnHeaderList.stream().map(ColumnHeader::getName).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "{" + columnHeaderList + "}";
    }
}
