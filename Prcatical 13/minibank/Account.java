package minibank;

import java.io.Serializable;

public class Account implements Serializable {

    @Id
    private long accountNumber;

    private double balance;

    public Account(long accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Account Number: " + accountNumber
                + ", Balance: " + balance;
    }
}