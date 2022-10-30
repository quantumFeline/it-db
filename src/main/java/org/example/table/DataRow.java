package org.example.table;

import com.sun.org.apache.bcel.internal.generic.ObjectType;

import javax.lang.model.type.UnknownTypeException;
import javax.xml.bind.TypeConstraintException;
import java.util.ArrayList;
import java.util.List;

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

    public List<String> getTypes() {
        List<String> types = new ArrayList<>();
        for (Object element: row) {
            types.add(element.getClass().getName());
        }
        return types;
    }

    @Override
    public String toString() {
        StringBuilder out_b = new StringBuilder("{ ");
        for (Object field: row) {
            out_b.append(field.toString()).append(" ");
        }
        out_b.append("}\n");
        return out_b.toString();
    }
}
