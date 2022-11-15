package org.example.client;

import org.apache.thrift.TException;
import java.util.Scanner;

public class UserPanel {

    private static final String EXIT = "q";

    public static void main(String[] args) {

        System.out.println("Welcome to our database!");

        NetworkConnectorClient client = new NetworkConnectorClient();
        Scanner s = new Scanner(System.in);

        try {
            client.startConnection();
        } catch (TException e) {
            System.out.println("Error connecting to server");
            return;
        }

        while(true) {
            String userInput = s.nextLine();
            if (userInput.equals(EXIT)) {
                System.out.println("Thank you for using our database!");
                break;
            }
            try {
                System.out.println(client.sendQuery(userInput));
            } catch (TException e) {
                System.out.println("Connection exception");
                e.printStackTrace();
            }
        }
    }
}
