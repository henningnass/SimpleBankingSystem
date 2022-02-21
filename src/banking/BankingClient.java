package banking;

import java.util.Scanner;

public class BankingClient {


    private OnlineBankingDB banking;

    public BankingClient(String fileName) {
        banking = new OnlineBankingDB(fileName);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        int itemNumber = 1;
        Account account = null;
        boolean insideCreationMenu = true;
        boolean insideCheckBalanceMenu = false;
        while (itemNumber != 0) {
            if (insideCreationMenu) {
                printCreateAndLogMenu();
            } else if (insideCheckBalanceMenu) {
                printCheckBalanceMenu();
            } else {
                // do nothing
            }
            itemNumber = scanner.nextInt();
            if (insideCreationMenu) {
                switch (itemNumber) {
                    case 1:
                        account = banking.addAccount();
                        System.out.println();
                        System.out.println("Your card has been created");
                        account.printAccountShort();
                        System.out.println();
                        break;
                    case 2:
                        account = logging();
                        if (account != null) {
                            insideCheckBalanceMenu = true;
                            insideCreationMenu = false;
                            System.out.println();
                            System.out.println("You have successfully logged in!");
                            System.out.println();
                        } else {
                            System.out.println();
                            System.out.println("Wrong card number or PIN!");
                            System.out.println();
                        }
                        break;
                    case 0: break;
                    default:
                        System.out.println();
                        System.out.println("Wrong input");
                        System.out.println();
                        break;
                }
            } else if (insideCheckBalanceMenu) {
                switch (itemNumber) {
                    case 1:
                        System.out.println();
                        System.out.println("Balance: " + account.getBalance());
                        System.out.println();
                        break;
                    case 2:
                        System.out.println();
                        System.out.println("Enter income: ");
                        System.out.print("> ");
                        int income = scanner.nextInt();
                        scanner.nextLine();
                        banking.addIncome(account.getCardNumber(), income);
                        account.setBalance(account.getBalance() + income);
                        System.out.println("Income was added!");
                        break;
                    case 3:
                        System.out.println("Transfer");
                        Account updatedAccount = banking.doTransfer(account.getCardNumber(), account.getPin());
                        if (updatedAccount != null) {
                            account = updatedAccount;
                        }
                        break;
                    case 4:
                        if (banking.closeAccount(account.getCardNumber()) > 0) {
                            System.out.println("The account has been closed!");
                            insideCheckBalanceMenu = false;
                            insideCreationMenu = true;
                        }
                        break;
                    case 5:
                        insideCheckBalanceMenu = false;
                        insideCreationMenu = true;
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Wrong input");
                        break;
                }
            } else {
                // do nothing
            }

        }


        System.out.println("Bye!");
    }

    private void printCreateAndLogMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
        System.out.print("> ");
    }

    private void printCheckBalanceMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
        System.out.print("> ");
    }

    private Account logging() {

        System.out.println("Enter your card number:");
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        String cardNumber = scanner.next();
        scanner.nextLine();
        System.out.println("Enter your PIN:");
        System.out.print("> ");
        String pin = scanner.next();
        scanner.nextLine();

        Account account = banking.getAccount(cardNumber, pin);


        return account;

    }
}
