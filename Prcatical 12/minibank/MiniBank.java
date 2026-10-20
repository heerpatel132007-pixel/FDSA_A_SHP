package minibank;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MiniBank {

    public static void main(String[] args) throws Exception {

        // CREATE ACCOUNTS
        Account a = new Account(101);
        Account b = new Account(102);
        Account c = new Account(103, 600);
        Account d = new Account(104, 200);

        // ACCOUNT STORE

        AccountStore store = new AccountStore();

        store.add(a);
        store.add(b);
        store.add(c);
        store.add(d);

        System.out.println("===== ALL ACCOUNTS =====");

        for (Account account : store.getAll()) {
            System.out.println(account);
        }

        // SEARCH ACCOUNT

        System.out.println("\n===== SEARCH ACCOUNT =====");

        System.out.println(store.get("103"));

        // SORT BY BALANCE

        System.out.println("\n===== SORTED BY BALANCE =====");

        for (Account account : store.sortedByBalance()) {
            System.out.println(account);
        }

        // CONCURRENT HASH MAP

        TransactionProcessor processor =
                new TransactionProcessor();

        processor.addAccount(a);
        processor.addAccount(b);
        processor.addAccount(c);
        processor.addAccount(d);

        ConcurrentHashMap<String, Account> sharedAccounts =
                processor.getSharedAccounts();

        System.out.println("\nShared accounts: "
                + sharedAccounts.size());

        // LOG FOLDER

        Path logsFolder = Paths.get("logs");
        Files.createDirectories(logsFolder);

        Path logFile = logsFolder.resolve("transactions.txt");

        // DEPOSITS AND WITHDRAWAL

        processor.submit(() -> {
            a.deposit(100);
            TransactionLog.append(logFile, "DEPOSIT 100");
            store.addTransaction("DEPOSIT 100");
        });

        processor.submit(() -> {
            a.deposit(200);
            TransactionLog.append(logFile, "DEPOSIT 200");
            store.addTransaction("DEPOSIT 200");
        });

        processor.submit(() -> {
            a.withdraw(50);
            TransactionLog.append(logFile, "WITHDRAW 50");
            store.addTransaction("WITHDRAW 50");
        });

        processor.submit(() -> {
            a.deposit(150);
            TransactionLog.append(logFile, "DEPOSIT 150");
            store.addTransaction("DEPOSIT 150");
        });

        processor.stop();

        System.out.println("\nBalance: " + a.getBalance());

        // TRANSACTION ARRAY

        int[] transactions = {100, 200, 300};

        System.out.println("\n===== TRANSACTION ARRAY =====");

        for (int amount : transactions) {
            System.out.println("Added: " + amount);
        }

        // TRANSFER THREAD 1

        Thread t1 = new Thread(() -> {

            Account first = a.getAccountNumber()
                    < b.getAccountNumber() ? a : b;

            Account second = first == a ? b : a;

            synchronized (first) {
                synchronized (second) {

                    if (a.getBalance() >= 100) {

                        a.withdraw(100);
                        b.deposit(100);

                        TransactionLog.append(
                                logFile, "TRANSFER_A_TO_B 100");

                        store.addTransaction(
                                "TRANSFER_A_TO_B 100");

                        System.out.println(
                                "Transfer A to B: 100");
                    }
                }
            }

        }, "Transfer-1");

        // TRANSFER THREAD 2

        Thread t2 = new Thread(() -> {

            Account first = a.getAccountNumber()
                    < b.getAccountNumber() ? a : b;

            Account second = first == a ? b : a;

            synchronized (first) {
                synchronized (second) {

                    if (b.getBalance() >= 200) {

                        b.withdraw(200);
                        a.deposit(200);

                        TransactionLog.append(
                                logFile, "TRANSFER_B_TO_A 200");

                        store.addTransaction(
                                "TRANSFER_B_TO_A 200");

                        System.out.println(
                                "Transfer B to A: 200");
                    }
                }
            }

        }, "Transfer-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        // FINAL BALANCES

        System.out.println("\n===== FINAL BALANCES =====");

        System.out.println("Final A Balance: " + a.getBalance());
        System.out.println("Final B Balance: " + b.getBalance());

        // TRANSACTION HISTORY

        System.out.println("\n===== TRANSACTION HISTORY =====");

        for (String transaction : store.getTransactionHistory()) {
            System.out.println(transaction);
        }

        // SAVE ACCOUNTS

        Account[] accounts =
                store.getAll().toArray(new Account[0]);

        Path stateFile = Paths.get("accounts.dat");

        StatePersister.save(accounts, stateFile);

        System.out.println("\nAccounts Saved");

        // LOAD ACCOUNTS

        Account[] loadedAccounts =
                StatePersister.load(stateFile);

        System.out.println("\n===== RELOADED ACCOUNTS =====");

        for (Account account : loadedAccounts) {
            System.out.println(account);
        }

        // GENERATE REPORT

        Path reportFile = Paths.get("report.txt");

        ReportGenerator.generate(logsFolder, reportFile);

        System.out.println("\n===== REPORT =====");

        Files.lines(reportFile).forEach(System.out::println);
    }
}


// ACCOUNT STORE

class AccountStore {

    private HashMap<String, Account> accounts =
            new HashMap<>();

    private ArrayList<String> transactionHistory =
            new ArrayList<>();

    public void add(Account account) {
        accounts.put(
                String.valueOf(account.getAccountNumber()),
                account);
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public synchronized void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public synchronized ArrayList<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> sortedAccounts = getAll();

        sortedAccounts.sort(
                Comparator.comparingLong(Account::getBalance));

        return sortedAccounts;
    }
}


// TRANSACTION LOG

class TransactionLog {

    public static synchronized void append(Path file, String line) {
        try {
            Files.write(
                    file,
                    Arrays.asList(line),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


// STATE PERSISTER

class StatePersister {

    public static void save(Account[] accounts, Path file) {
        try (ObjectOutputStream output =
                     new ObjectOutputStream(
                             Files.newOutputStream(file))) {

            output.writeObject(accounts);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Account[] load(Path file) {
        try (ObjectInputStream input =
                     new ObjectInputStream(
                             Files.newInputStream(file))) {

            return (Account[]) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new Account[0];
    }
}


// REPORT GENERATOR

class ReportGenerator {

    public static void generate(
            Path logsFolder, Path reportFile)
            throws IOException {

        long totalDeposits = 0;
        long totalWithdrawals = 0;

        StringBuilder report = new StringBuilder();

        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(logsFolder)) {

            for (Path file : stream) {

                if (!Files.isRegularFile(file)) {
                    continue;
                }

                BasicFileAttributes attributes =
                        Files.readAttributes(
                                file, BasicFileAttributes.class);

                report.append("File: ")
                        .append(file.getFileName())
                        .append("\n");

                report.append("Size: ")
                        .append(attributes.size())
                        .append(" bytes\n");

                report.append("Last Modified: ")
                        .append(attributes.lastModifiedTime())
                        .append("\n");

                try (BufferedReader reader =
                             Files.newBufferedReader(file)) {

                    String line;

                    while ((line = reader.readLine()) != null) {

                        String[] parts = line.split(" ");

                        if (parts.length < 2) {
                            continue;
                        }

                        long amount;

                        try {
                            amount = Long.parseLong(parts[1]);
                        } catch (NumberFormatException e) {
                            continue;
                        }

                        if (line.startsWith("DEPOSIT")) {
                            totalDeposits += amount;
                        }

                        if (line.startsWith("WITHDRAW")) {
                            totalWithdrawals += amount;
                        }
                    }
                }

                report.append("-------------------\n");
            }
        }

        report.append("\nTOTAL DEPOSITS : ")
                .append(totalDeposits)
                .append("\n");

        report.append("TOTAL WITHDRAWALS : ")
                .append(totalWithdrawals)
                .append("\n");

        report.append("NET AMOUNT : ")
                .append(totalDeposits - totalWithdrawals)
                .append("\n");

        Files.writeString(
                reportFile,
                report.toString(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}