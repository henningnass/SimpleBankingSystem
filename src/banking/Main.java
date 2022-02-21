package banking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        BankingClient bankingClient = new BankingClient(args[1]);
        bankingClient.start();


    }
}