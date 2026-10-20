package minibank;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.CopyOnWriteArrayList;



class BankException extends RuntimeException {

    public BankException(String message) {
        super(message);
    }
}

class AccountNotFoundException extends BankException {

    public AccountNotFoundException(String message) {
        super(message);
    }
}

class InsufficientFundsException extends BankException {

    public InsufficientFundsException(String message) {
        super(message);
    }
}

class InvalidAmountException extends BankException {

    public InvalidAmountException(String message) {
        super(message);
    }
}


class Validator {

    public static void validateAmount(double amount) {

        if (!Double.isFinite(amount) || amount <= 0) {
            throw new InvalidAmountException(
                    "Amount must be greater than zero.");
        }
    }

    public static void validateAccountNumber(long accountNumber) {

        if (accountNumber <= 0) {
            throw new IllegalArgumentException(
                    "Account number must be positive.");
        }
    }
}


class BankService {

    private final Repository<Account> repository =
            new Repository<>();

    private final List<String> transactionHistory =
            new CopyOnWriteArrayList<>();

    private final Object transferLock = new Object();

    private final String accountFile = "accounts.dat";
    private final String transactionFile = "transactions.txt";

    public BankService() {
        loadAccounts();
        loadTransactionHistory();
    }

   
    public synchronized void openAccount(
            long accountNumber, double initialBalance) {

        Validator.validateAccountNumber(accountNumber);

        if (initialBalance < 0 || !Double.isFinite(initialBalance)) {
            throw new InvalidAmountException(
                    "Initial balance cannot be negative.");
        }

        if (repository.findById(String.valueOf(accountNumber)) != null) {
            throw new BankException(
                    "Account " + accountNumber + " already exists.");
        }

        Account account = new Account(accountNumber, initialBalance);

        repository.save(account);

        recordTransaction(
                "Account opened [" + accountNumber + "] with balance "
                        + initialBalance);

        System.out.println("Account opened successfully!");
        System.out.println(account);
    }

   

    public void deposit(long accountNumber, double amount) {

        Account account = getAccount(accountNumber);

        account.deposit(amount);

        recordTransaction(
                "Deposit: " + amount + " into [" + accountNumber + "]");

        System.out.println("Deposit successful!");
        System.out.println("Updated balance: " + account.getBalance());
    }


    public void withdraw(long accountNumber, double amount) {

        Account account = getAccount(accountNumber);

        account.withdraw(amount);

        recordTransaction(
                "Withdrawal: " + amount + " from [" + accountNumber + "]");

        System.out.println("Withdrawal successful!");
        System.out.println("Updated balance: " + account.getBalance());
    }

   
    public void transfer(
            long sourceNumber, long destinationNumber, double amount) {

        Validator.validateAmount(amount);

        if (sourceNumber == destinationNumber) {
            throw new BankException(
                    "Source and destination accounts must be different.");
        }

        synchronized (transferLock) {

            Account source = getAccount(sourceNumber);
            Account destination = getAccount(destinationNumber);

            source.withdraw(amount);

            try {
                destination.deposit(amount);
            } catch (RuntimeException exception) {
                source.deposit(amount);
                throw exception;
            }

            recordTransaction(
                    "Transfer: " + amount + " from [" + sourceNumber
                            + "] to [" + destinationNumber + "]");

            System.out.println("Transfer successful!");
            System.out.println(
                    "Source balance: " + source.getBalance());
            System.out.println(
                    "Destination balance: " + destination.getBalance());
        }
    }

   

    public Account getAccount(long accountNumber) {

        Account account =
                repository.findById(String.valueOf(accountNumber));

        if (account == null) {
            throw new AccountNotFoundException(
                    "Account " + accountNumber + " not found.");
        }

        return account;
    }

  

    public void statement(long accountNumber) {

        getAccount(accountNumber);

        System.out.println("\nACCOUNT STATEMENT");
        System.out.println("------------------------------");
        System.out.println("Account Number: " + accountNumber);

        boolean found = false;

        for (String transaction : transactionHistory) {

            if (transaction.contains("[" + accountNumber + "]")) {
                System.out.println(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found.");
        }

        System.out.println("------------------------------");
    }

   
    public void listAccounts() {

        List<Account> accounts =
                new ArrayList<>(repository.findAll());

        Collections.sort(accounts);

        System.out.println("\nALL ACCOUNTS");
        System.out.println("------------------------------");

        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
        }

        for (Account account : accounts) {
            System.out.println(account);
        }

        System.out.println("Total accounts: " + accounts.size());
    }

   

    public void searchAccount(long accountNumber) {

        Account account = getAccount(accountNumber);

        System.out.println("Account found!");
        System.out.println(account);
    }

   

    public void deleteAccount(long accountNumber) {

        Account account = getAccount(accountNumber);

        if (account.getBalance() != 0) {
            throw new BankException(
                    "Account cannot be deleted because its balance is not zero.");
        }

        repository.delete(String.valueOf(accountNumber));

        recordTransaction(
                "Account deleted [" + accountNumber + "]");

        System.out.println("Account deleted successfully.");
    }

    

    public void calculateAverageBalance() {

        Collection<Account> accounts = repository.findAll();

        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        double total = 0;

        for (Account account : accounts) {
            total += account.getBalance();
        }

        double average = total / accounts.size();

        System.out.printf(
                "Average account balance: %.2f%n", average);
    }

    

    public void saveAccounts() {

        try (ObjectOutputStream output =
                     new ObjectOutputStream(
                             new FileOutputStream(accountFile))) {

            ArrayList<Account> accounts =
                    new ArrayList<>(repository.findAll());

            output.writeObject(accounts);

            System.out.println("Accounts saved successfully.");

        } catch (IOException exception) {
            System.out.println(
                    "Error saving accounts: " + exception.getMessage());
        }
    }

    
    @SuppressWarnings("unchecked")
    public void loadAccounts() {

        File file = new File(accountFile);

        if (!file.exists()) {

            repository.saveAll(
                    new Account(101, 5000),
                    new Account(102, 3000),
                    new Account(103, 7000));

            System.out.println(
                    "Default accounts loaded: 101, 102, 103.");

            return;
        }

        try (ObjectInputStream input =
                     new ObjectInputStream(
                             new FileInputStream(file))) {

            ArrayList<Account> accounts =
                    (ArrayList<Account>) input.readObject();

            repository.clear();

            for (Account account : accounts) {
                repository.save(account);
            }

            System.out.println(
                    "Saved accounts loaded successfully.");

        } catch (IOException | ClassNotFoundException
                 | ClassCastException exception) {

            System.out.println(
                    "Could not load saved accounts: "
                            + exception.getMessage());

            repository.clear();

            repository.saveAll(
                    new Account(101, 5000),
                    new Account(102, 3000),
                    new Account(103, 7000));
        }
    }

   
    private void recordTransaction(String transaction) {

        transactionHistory.add(transaction);

        try (FileWriter writer =
                     new FileWriter(transactionFile, true)) {

            writer.write(transaction + System.lineSeparator());

        } catch (IOException exception) {
            System.out.println(
                    "Could not save transaction history.");
        }
    }

   

    private void loadTransactionHistory() {

        File file = new File(transactionFile);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                transactionHistory.add(line);
            }

        } catch (IOException exception) {
            System.out.println(
                    "Could not load transaction history.");
        }
    }

    

    public void generateReport() {

        Collection<Account> accounts = repository.findAll();

        double totalBalance = 0;
        double highestBalance = 0;
        double lowestBalance = Double.MAX_VALUE;

        Account highestAccount = null;
        Account lowestAccount = null;

        for (Account account : accounts) {

            double balance = account.getBalance();

            totalBalance += balance;

            if (balance > highestBalance || highestAccount == null) {
                highestBalance = balance;
                highestAccount = account;
            }

            if (balance < lowestBalance || lowestAccount == null) {
                lowestBalance = balance;
                lowestAccount = account;
            }
        }

        double average = accounts.isEmpty()
                ? 0
                : totalBalance / accounts.size();

        StringBuilder report = new StringBuilder();

        report.append("\n========== MINIBANK REPORT ==========\n");
        report.append("Total accounts: ").append(accounts.size()).append("\n");
        report.append(String.format(
                "Total balance: %.2f%n", totalBalance));
        report.append(String.format(
                "Average balance: %.2f%n", average));

        if (highestAccount != null) {
            report.append("Highest balance account: ")
                    .append(highestAccount.getAccountNumber())
                    .append(" (")
                    .append(String.format("%.2f", highestBalance))
                    .append(")\n");

            report.append("Lowest balance account: ")
                    .append(lowestAccount.getAccountNumber())
                    .append(" (")
                    .append(String.format("%.2f", lowestBalance))
                    .append(")\n");
        }

        report.append("=====================================\n");

        System.out.println(report);

        try (FileWriter writer = new FileWriter("report.txt")) {
            writer.write(report.toString());

            System.out.println("Report saved to report.txt.");

        } catch (IOException exception) {
            System.out.println("Could not save report.");
        }
    }

   

    public void batchDeposits() {

        Scanner scanner = new Scanner(System.in);

        System.out.print("How many batch deposits? ");
        int numberOfDeposits = scanner.nextInt();

        if (numberOfDeposits <= 0 || numberOfDeposits > 1000) {
            System.out.println(
                    "Enter a number between 1 and 1000.");
            return;
        }

        List<long[]> depositAccounts = new ArrayList<>();
        List<Double> depositAmounts = new ArrayList<>();

        for (int i = 0; i < numberOfDeposits; i++) {

            System.out.println("\nDeposit " + (i + 1));

            System.out.print("Account number: ");
            long accountNumber = scanner.nextLong();

            System.out.print("Deposit amount: ");
            double amount = scanner.nextDouble();

            depositAccounts.add(new long[]{accountNumber});
            depositAmounts.add(amount);
        }

        TransactionProcessor processor = new TransactionProcessor(this);

        processor.processBatch(depositAccounts, depositAmounts);
    }

    public Collection<Account> getAllAccounts() {
        return repository.findAll();
    }
}


class TransactionProcessor {

    private final BankService bankService;

    public TransactionProcessor(BankService bankService) {
        this.bankService = bankService;
    }

    public void processBatch(
            List<long[]> accountNumbers,
            List<Double> amounts) {

        ExecutorService executor =
                Executors.newFixedThreadPool(4);

        List<Future<String>> results = new ArrayList<>();

        try {
            for (int i = 0; i < accountNumbers.size(); i++) {

                final long accountNumber = accountNumbers.get(i)[0];
                final double amount = amounts.get(i);

                Future<String> result = executor.submit(() -> {

                    bankService.deposit(accountNumber, amount);

                    return "Deposit of " + amount
                            + " into account " + accountNumber
                            + " completed by "
                            + Thread.currentThread().getName();
                });

                results.add(result);
            }

            System.out.println("\nBATCH DEPOSIT RESULTS");
            System.out.println("------------------------------");

            for (Future<String> result : results) {

                try {
                    System.out.println(result.get());

                } catch (ExecutionException exception) {
                    System.out.println(
                            "Batch operation failed: "
                                    + exception.getCause().getMessage());
                }
            }

            System.out.println("------------------------------");

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();

            System.out.println("Batch operation interrupted.");

        } finally {
            executor.shutdown();
        }
    }
}


public class MiniBank {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        BankService bankService = new BankService();

        boolean running = true;

        while (running) {

            System.out.println("\n====================================");
            System.out.println("           MINIBANK SYSTEM");
            System.out.println("====================================");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Account Statement");
            System.out.println("6. List Accounts");
            System.out.println("7. Batch Deposits");
            System.out.println("8. Generate Report");
            System.out.println("9. Search Account");
            System.out.println("10. Delete Account");
            System.out.println("11. Calculate Average Balance");
            System.out.println("12. Save and Exit");

            System.out.print("Enter choice: ");

            try {
                int choice = scanner.nextInt();

                switch (choice) {

                    case 1: {
                        System.out.print("New account number: ");
                        long number = scanner.nextLong();

                        System.out.print("Initial balance: ");
                        double balance = scanner.nextDouble();

                        bankService.openAccount(number, balance);
                        break;
                    }

                    case 2: {
                        System.out.print("Account number: ");
                        long number = scanner.nextLong();

                        System.out.print("Deposit amount: ");
                        double amount = scanner.nextDouble();

                        bankService.deposit(number, amount);
                        break;
                    }

                    case 3: {
                        System.out.print("Account number: ");
                        long number = scanner.nextLong();

                        System.out.print("Withdrawal amount: ");
                        double amount = scanner.nextDouble();

                        bankService.withdraw(number, amount);
                        break;
                    }

                    case 4: {
                        System.out.print("Source account number: ");
                        long source = scanner.nextLong();

                        System.out.print("Destination account number: ");
                        long destination = scanner.nextLong();

                        System.out.print("Transfer amount: ");
                        double amount = scanner.nextDouble();

                        bankService.transfer(
                                source, destination, amount);
                        break;
                    }

                    case 5: {
                        System.out.print("Account number: ");
                        long number = scanner.nextLong();

                        bankService.statement(number);
                        break;
                    }

                    case 6:
                        bankService.listAccounts();
                        break;

                    case 7:
                        bankService.batchDeposits();
                        break;

                    case 8:
                        bankService.generateReport();
                        break;

                    case 9: {
                        System.out.print("Account number: ");
                        long number = scanner.nextLong();

                        bankService.searchAccount(number);
                        break;
                    }

                    case 10: {
                        System.out.print("Account number: ");
                        long number = scanner.nextLong();

                        bankService.deleteAccount(number);
                        break;
                    }

                    case 11:
                        bankService.calculateAverageBalance();
                        break;

                    case 12:
                        bankService.saveAccounts();

                        System.out.println(
                                "Thank you for using MiniBank!");

                        running = false;
                        break;

                    default:
                        System.out.println(
                                "Invalid choice. Please try again.");
                }

            } catch (BankException | IllegalArgumentException exception) {

                System.out.println(
                        "Operation failed: " + exception.getMessage());

            } catch (InputMismatchException exception) {

                System.out.println(
                        "Invalid input. Please enter the correct data type.");

                scanner.nextLine();

            } finally {

                System.out.println("Menu operation completed.");
            }
        }

        scanner.close();
    }
} 