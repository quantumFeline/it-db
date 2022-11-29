package org.example.server;

import org.example.server.table.DataRow;
import org.example.server.table.ErrorCode;
import org.example.server.table.TableHeader;

import javax.xml.bind.TypeConstraintException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class QueryProcessor {

    String lastResult;
    String lastError;
    ErrorCode successErrorCode = ErrorCode.UNINITIALIZED;
    public static final int SUCCESS_CODE = -1;;
    private static final String SELECT = "SELECT";
    private static final String LIST = "LIST";
    private static final String ADD_TABLE = "ADD_TABLE";
    private static final String ADD_ENTRY = "ADD_ENTRY";
    private static final String INTERSECTION = "INTERSECTION";
    Database database;

    public QueryProcessor(Database database) {
        this.database = database;
    }

    public void sendQuery(String queryLine) {
        successErrorCode = ErrorCode.UNINITIALIZED;
        try {
            String[] query = queryLine.split(" ");
            String command = query[0].toUpperCase().trim();
            String out = "";
            switch (command) {
                case SELECT: {
                    String tableName = query[1];
                    int n_entries = Integer.parseInt(query[2]);
                    List<DataRow> entries = database.getEntries(tableName, n_entries);
                    if (database.getSuccessErrorCode() == ErrorCode.TABLE_NOT_FOUND) {
                        throw new NoSuchElementException();
                    }
                    StringBuilder out_b = new StringBuilder(tableName).append("\n").
                            append(database.getTableHeader(tableName).getHeaderNames()).append("\n");
                    for (DataRow entry: entries) {
                        out_b.append(entry.toString()).append("\n");
                    }
                    out = out_b.toString();
                    break;
                }
                case LIST: {
                    out = database.listTables().toString();
                    break;
                }
                case ADD_TABLE: {
                    String tableName = query[1];
                    TableHeader header = new TableHeader(Arrays.asList(query).subList(2,query.length));
                    database.addTable(tableName, header);
                    break;
                }
                case INTERSECTION: {
                    String tableNameA = query[1];
                    String tableNameB = query[2];
                    String fieldNameA = query[3];
                    String fieldNameB = query[4];
                    out = database.getIntersection(tableNameA, tableNameB, fieldNameA, fieldNameB).toString();
                    break;
                }
                case ADD_ENTRY: {
                    String tableName = query[1];
                    database.addEntry(tableName, Arrays.asList(query).subList(2, query.length));
                    break;
                }
                default:
                    throw new InvalidParameterException();
            }
            out = database.getMessage() + "\n" + out;
            database.clearMessage();
            successErrorCode = ErrorCode.SUCCESS_CODE;
            lastResult = out;
        } catch (ArrayIndexOutOfBoundsException exception) {
            lastError = "Incorrect number of arguments";
            successErrorCode = ErrorCode.NUMBER_ARGUMENTS;
        } catch (InvalidParameterException exception) {
            lastError = "Unknown command";
            successErrorCode = ErrorCode.UNKNOWN_COMMAND;
        } catch (TypeConstraintException exception) {
            lastError = "Unsupported field type";
            successErrorCode = ErrorCode.UNSUPPORTED_FIELD_TYPE;
        } catch (NoSuchElementException exception) {
            lastError = "No such table";
            successErrorCode = ErrorCode.TABLE_NOT_FOUND;
        } catch (RuntimeException exception) {
            lastError = "Query could not have been processed";
            successErrorCode = ErrorCode.OTHER;
        }
    }

    public String getLastResult() {
        return lastResult;
    }

    public String getLastError() {
        return lastError;
    }

    public ErrorCode getSuccessErrorCode() {
        return successErrorCode;
    }
}
