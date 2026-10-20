import java.io.Serializable;

public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    long accountNumber;
    long balance;

    Account(long accountNumber) {
        this(accountNumber, 0);
    }

    Account(long accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    synchronized void deposit(long amount) {
        balance = balance + amount;

        System.out.println(
                Thread.currentThread().getName()
                        + " deposited " + amount);
    }

    synchronized void withdraw(long amount) {
        if (amount <= balance) {
            balance = balance - amount;

            System.out.println(
                    Thread.currentThread().getName()
                            + " withdrew " + amount);
        }
    }

    @Override
    public String toString() {
        return "Account Number = " + accountNumber
                + ", Balance = " + balance;
    }
}