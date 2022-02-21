package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class OnlineBankingDB {
    public static String bin = "400000";
    private String url = "jdbc:sqlite:./";

    public OnlineBankingDB(String fileName) {
        url = url + fileName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "number TEXT NOT NULL," +
                        "pin TEXT NOT NULL,"    +
                        "balance INTEGER DEFAULT 0);");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getBin() {
        return bin;
    }

    public Account addAccount() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        Account randomAccount = null;

        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution

                boolean validIdFound = false;


                while (!validIdFound) {
                    randomAccount = Account.generateAccount(bin);

                    ResultSet accounts = statement.executeQuery("SELECT * FROM card " +
                            "WHERE number = " + randomAccount.getCardNumber());

                    if (!accounts.next()) {
                        statement.executeUpdate("INSERT INTO card (number, pin, balance) VALUES " +
                        "(" + randomAccount.getCardNumber() + "," +
                                randomAccount.getPin() + "," + randomAccount.getBalance() + ")");
                        validIdFound = true;
                        break;
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return randomAccount;
    }

    public boolean existsAccount(String cardNumber) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        Account account = null;

        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution

                String query = "SELECT * FROM card WHERE number = " + cardNumber;
                ResultSet resultset = statement.executeQuery(query);

                if (resultset.next()) {
                    return true;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public Account getAccount(String cardNumber, String pin) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        Account account = null;

        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution

                String query = "SELECT * FROM card WHERE number = " + cardNumber +
                        " AND pin = " + pin;
                ResultSet resultset = statement.executeQuery(query);

                if (resultset.next()) {
                    String checksum = cardNumber.substring(15,16);
                    String id = cardNumber.substring(6, 15);
                    int balance = resultset.getInt(4);
                    account = new Account(id, OnlineBankingDB.bin, checksum, balance);

                    return account;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public Account doTransfer(String cardNumber, String pin) {
        Scanner scanner = new Scanner(System.in);
        Account account = null;
        System.out.println("Enter card number: ");
        System.out.print("> ");
        String targetCardNumber = scanner.next();

        String checksumTarget = targetCardNumber.substring(15,16);
        String checksumFromLuhnAlgorithm = BankingUtilities.getCheckSum(targetCardNumber.substring(0,15)) + "";

        if (!checksumFromLuhnAlgorithm.equals(checksumTarget)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        } else {
            if (!existsAccount(targetCardNumber)) {
                System.out.println("Such a card does not exist.");
            } else {
                System.out.println("Enter how much money you want to transfer:");
                System.out.print("> ");
                int amount = scanner.nextInt();


                SQLiteDataSource dataSource = new SQLiteDataSource();
                dataSource.setUrl(url);

                try (Connection con = dataSource.getConnection()) {
                    // Statement creation
                    try (Statement statement = con.createStatement()) {
                        // Statement execution

                        String balanceQuery = "SELECT balance FROM card WHERE number = " + cardNumber;

                        ResultSet resultset = statement.executeQuery(balanceQuery);
                        while (resultset.next()) {
                            int balance = resultset.getInt("balance");
                            if (balance < amount) {
                                System.out.println("Not enough money!");
                            } else {
                                String withdrawUpdate = "UPDATE card SET balance = balance - " + amount +
                                        " WHERE number = " + cardNumber;
                                String transferUpdate = "UPDATE card SET balance = balance + " + amount +
                                        " WHERE number = " + targetCardNumber;

                                statement.executeUpdate(withdrawUpdate);
                                statement.executeUpdate(transferUpdate);

                                account = new Account(cardNumber.substring(6, 15), cardNumber.substring(0, 6),
                                        cardNumber.substring(15, 16),
                                        balance - amount);
                                return account;
                            }
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return account;
    }

    public int closeAccount(String cardNumber) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution

                String deleteStatement = "DELETE FROM card WHERE number = " + cardNumber;

                return statement.executeUpdate(deleteStatement);


            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();

            return 0;
        }
    }


    public void addIncome(String cardNumber, int income) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution

                String updateStatement = "UPDATE card SET balance = balance + " + income +
                        " WHERE number = " + cardNumber;

                statement.executeUpdate(updateStatement);


            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
