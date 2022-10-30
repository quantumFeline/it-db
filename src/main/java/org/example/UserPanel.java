package org.example;

import java.util.Scanner;

public class UserPanel {

    private static final String EXIT = "q";
    private static final Database database = new Database();
    private static final QueryProcessor queryProcessor = new QueryProcessor(database);

    public static void main(String[] args) {
        System.out.println("Welcome to our database!");
        Scanner s = new Scanner(System.in);

        while(true) {
            String userInput = s.nextLine();
            if (userInput.equals(EXIT)) {
                System.out.println("Thank you for using our database!");
                break;
            }
            queryProcessor.sendQuery(userInput);
            if(queryProcessor.getSuccessErrorCode() != ErrorCode.SUCCESS_CODE) {
                System.out.println(queryProcessor.getLastError());
            } else {
                System.out.println(queryProcessor.getLastResult());
            }
        }
    }
}
