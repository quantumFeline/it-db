package org.example.client;

import java.rmi.RemoteException;
import java.util.Scanner;

public class UserPanel {

    private static final String EXIT = "q";

    public static void main(String[] args) {

        System.out.println("Welcome to our database!");

        NetworkConnectorClient client = new NetworkConnectorClient();
        Scanner s = new Scanner(System.in);

        try {
            client.startConnection();
        } catch (RemoteException e) {
            System.out.println("Error connecting to server");
            return;
        }

        while(true) {
            String userInput = s.nextLine();
            if (userInput.equals(EXIT)) {
                System.out.println("Thank you for using our database!");
                break;
            }
            System.out.println(client.sendQuery(userInput));
        }
    }
}
