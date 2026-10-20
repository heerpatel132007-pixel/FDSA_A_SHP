import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public class MiniBank {

    public static void main(String[] args)
            throws Exception {

        Account a = new Account(101);
        Account b = new Account(102);

        TransactionProcessor processor =
                new TransactionProcessor();

        Path logsFolder = Paths.get("logs");

        Files.createDirectories(logsFolder);

        Path logFile =
                logsFolder.resolve("transactions.txt");

        // DEPOSIT AND WITHDRAWAL

        processor.submit(() -> {
            a.deposit(100);
            TransactionLog.append(
                    logFile, "DEPOSIT 100");
        });

        processor.submit(() -> {
            a.deposit(200);
            TransactionLog.append(
                    logFile, "DEPOSIT 200");
        });

        processor.submit(() -> {
            a.withdraw(50);
            TransactionLog.append(
                    logFile, "WITHDRAW 50");
        });

        processor.submit(() -> {
            a.deposit(150);
            TransactionLog.append(
                    logFile, "DEPOSIT 150");
        });

        processor.stop();

        System.out.println("Balance: " + a.balance);

        // TRANSACTION ARRAY

        int[] transactions = new int[3];
        int count = 0;

        synchronized (transactions) {

            transactions[count++] = 100;
            System.out.println("Added: 100");

            transactions[count++] = 200;
            System.out.println("Added: 200");

            transactions[count++] = 300;
            System.out.println("Added: 300");

            transactions.notify();
        }

        // TRANSFER THREAD 1

        Thread t1 = new Thread(() -> {

            Account first;
            Account second;

            if (a.accountNumber < b.accountNumber) {
                first = a;
                second = b;
            } else {
                first = b;
                second = a;
            }

            synchronized (first) {
                synchronized (second) {

                    if (a.balance >= 100) {

                        a.balance -= 100;
                        b.balance += 100;

                        TransactionLog.append(
                                logFile,
                                "TRANSFER_A_TO_B 100");

                        System.out.println(
                                "Transfer A to B: 100");
                    }
                }
            }

        }, "Transfer-1");

        // TRANSFER THREAD 2

        Thread t2 = new Thread(() -> {

            Account first;
            Account second;

            if (a.accountNumber < b.accountNumber) {
                first = a;
                second = b;
            } else {
                first = b;
                second = a;
            }

            synchronized (first) {
                synchronized (second) {

                    if (b.balance >= 200) {

                        b.balance -= 200;
                        a.balance += 200;

                        TransactionLog.append(
                                logFile,
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

        System.out.println(
                "Final A Balance: " + a.balance);

        System.out.println(
                "Final B Balance: " + b.balance);

        // SAVE ACCOUNTS

        Account[] accounts = {a, b};

        Path stateFile = Paths.get("accounts.dat");

        StatePersister.save(accounts, stateFile);

        System.out.println("\nAccounts Saved");

        // LOAD ACCOUNTS

        Account[] loadedAccounts =
                StatePersister.load(stateFile);

        System.out.println("\nReloaded Accounts");

        for (Account account : loadedAccounts) {
            System.out.println(account);
        }

        // GENERATE REPORT

        Path reportFile = Paths.get("report.txt");

        ReportGenerator.generate(
                logsFolder, reportFile);

        System.out.println("\n===== REPORT =====");

        Files.lines(reportFile)
                .forEach(System.out::println);
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


// TRANSACTION LOG

class TransactionLog {

    public static void append(Path file, String line) {

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
                                file,
                                BasicFileAttributes.class);

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