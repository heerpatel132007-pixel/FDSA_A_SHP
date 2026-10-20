package minibank;

import java.io.Serializable;



public class Account implements Serializable, Comparable<Account> {

    private static final long serialVersionUID = 1L;

    long accountNumber;
    long balance;

    public Account(long accountNumber) {
        this(accountNumber, 0);
    }

    public Account(long accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public long getBalance() {
        return balance;
    }

    synchronized void deposit(long amount) {
        balance += amount;

        System.out.println(
                Thread.currentThread().getName()
                        + " deposited " + amount);
    }

    synchronized void withdraw(long amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;

            System.out.println(
                    Thread.currentThread().getName()
                            + " withdrew " + amount);
        } else {
            System.out.println("Insufficient balance");
        }
    }

    // Compare accounts by account number
    @Override
    public int compareTo(Account other) {
        return Long.compare(accountNumber, other.accountNumber);
    }

    @Override
    public String toString() {
        return "Account Number = " + accountNumber
                + ", Balance = " + balance;
    }
}