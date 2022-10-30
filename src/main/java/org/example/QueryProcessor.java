package org.example;

import org.example.table.DataRow;
import org.example.table.TableHeader;

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
    Database database;

    QueryProcessor(Database database) {
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
                    if (database.getSuccessErrorCode() == Database.NO_SUCH_TABLE_CODE) {
                        throw new NoSuchElementException();
                    }
                    StringBuilder out_b = new StringBuilder(tableName);
                    out_b.append(database.getTableHeader(tableName)).append("\n");
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
                    out = "Table added successfully";
                    break;
                } case ADD_ENTRY: {
                    String tableName = query[1];
                    List<String> headerTypes = database.getTableHeader(tableName).getHeaderTypes();
                    database.addEntry(tableName, new DataRow(headerTypes, Arrays.asList(query).subList(2, query.length)));
                    out = "Entry added successfully";
                    break;
                }
                default:
                    throw new InvalidParameterException();
            }
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
