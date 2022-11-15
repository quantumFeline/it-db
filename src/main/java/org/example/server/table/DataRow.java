package org.example.server.table;

import javax.xml.bind.TypeConstraintException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class DataRow {
    List<Object> row;

    public DataRow(List<Object> row) {
        this.row = row;
    }

    public DataRow(List<String> headerTypes, List<String> args) {
        row = new ArrayList<>();
        assert args.size() == headerTypes.size();
        for (int i = 0; i < args.size(); i++) {
            switch (headerTypes.get(i)) {
                case "java.lang.Integer":
                    row.add(Integer.parseInt(args.get(i)));
                    break;
                case "java.lang.Float":
                    row.add(Float.parseFloat(args.get(i)));
                    break;
                case "java.lang.String":
                    row.add(args.get(i));
                    break;
                default:
                    throw new TypeConstraintException("only int, float, string are supported");
            }
        }
    }

    public List<Object> getRow() {
        return row;
    }

    public String toString(TableHeader header) {
        StringBuilder out_b = new StringBuilder("{ ");
        ListIterator<String> headerIterator = header.getHeaderNames().listIterator();
        ListIterator<String> headerTypesIterator = header.getHeaderTypes().listIterator();
        for (Object field: row) {
            out_b.append("\"").append(headerIterator.next()).append("\"");
            out_b.append(": ");
            if (headerTypesIterator.next().equals("java.lang.String")) {
                out_b.append("\"").append(field).append("\"");
            } else {
                out_b.append(field);
            }
            if (headerIterator.hasNext()) {
                out_b.append(", ");
            }
        }
        out_b.append("}\n");
        return out_b.toString();
    }
}
