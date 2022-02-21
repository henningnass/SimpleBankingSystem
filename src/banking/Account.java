package banking;

import java.util.Date;
import java.util.Random;

public class Account {
    private String id;
    private String checksum;
    private int balance;
    private String pin;
    final private String bin;

    public Account(String id, String bin, String checksum, int balance) {
        this.id = id;
        this.checksum = checksum;
        this.balance = balance;
        this.bin = bin;
    }



    public String getBin() {
        return bin;
    }

    public boolean setId(String id) {
        String regex = "[0-9]{9}";
        if (id.matches(regex)) {
            this.id = id;
            return true;
        }
        return false;
    }

    public String getId() {
        return this.id;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
    this.balance = balance;
    }

    public boolean setPin(String pin) {
        String regex = "[0-9]{4}";
        if(pin.matches(regex)) {
            this.pin = pin;
            return true;
        }
        return false;
    }



    public static Account generateAccount(String bin) {
        String pin = BankingUtilities.generateRandomSeries(4);

        String id = BankingUtilities.generateRandomSeries(9);
        int checksum = BankingUtilities.getCheckSum(bin + id);

        Account account = new Account(id, bin, checksum + "", 0);
        account.setPin(pin);
        account.setBalance(0);
        return account;
    }

    public void printAccountShort() {
        System.out.println("Your card number:");
        System.out.println(getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(this.getPin());
    }

    public String getCardNumber() {
        return (this.bin + this.getId() + this.checksum);
    }

}
