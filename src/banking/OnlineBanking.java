package banking;

import java.util.ArrayList;
import java.util.List;

public class OnlineBanking {

    public static String bin;
    private List<Account> accounts = new ArrayList<>();

    public OnlineBanking() {
        bin = "400000";
    }

    public static String getBin() {
        return bin;
    }

    public Account addAccount() {
        boolean validIdFound = false;
        String id;
        Account randomAccount = null;

        while (!validIdFound) {
            validIdFound = true;
            randomAccount = Account.generateAccount(bin);
            for (Account account : accounts) {
                if (account.getId().equals(randomAccount.getId())) {
                    validIdFound = false;
                    break;
                }
            }
        }

        accounts.add(randomAccount);
        return randomAccount;
    }

    public Account getAccount(String cardNumber, String pin) {
        for (Account account : accounts) {
            if (account.getCardNumber().equals(cardNumber) && account.getPin().equals(pin)) {
                return account;
            }
        }
        return null;
    }

}
