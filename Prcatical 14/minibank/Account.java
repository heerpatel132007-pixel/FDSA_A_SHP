package minibank;

import java.io.Serializable;

public class Account implements Serializable, Comparable<Account> {

    private static final long serialVersionUID = 1L;

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

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized void deposit(double amount) {
        Validator.validateAmount(amount);
        balance += amount;
    }

    public synchronized void withdraw(double amount) {
        Validator.validateAmount(amount);

        if (amount > balance) {
            throw new InsufficientFundsException(
                    "Insufficient balance in account " + accountNumber);
        }

        balance -= amount;
    }

    @Override
    public int compareTo(Account otherAccount) {
        return Long.compare(
                this.accountNumber,
                otherAccount.accountNumber);
    }

    @Override
    public String toString() {
        return "Account Number: " + accountNumber
                + ", Balance: "
                + String.format("%.2f", getBalance());
    }
}