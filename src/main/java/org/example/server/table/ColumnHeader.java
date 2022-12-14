package org.example.server.table;

public class ColumnHeader {
    String name;
    String type;

    ColumnHeader(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[" + name + "(" + type + ")]";
    }
}
