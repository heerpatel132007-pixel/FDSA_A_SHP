package minibank;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransactionProcessor {

    ExecutorService pool =
            Executors.newFixedThreadPool(4);

    private ConcurrentHashMap<String, Account> sharedAccounts =
            new ConcurrentHashMap<>();

    public void addAccount(Account account) {
        sharedAccounts.put(
                String.valueOf(account.getAccountNumber()),
                account);
    }

    public Account getAccount(String accountNumber) {
        return sharedAccounts.get(accountNumber);
    }

    public ConcurrentHashMap<String, Account> getSharedAccounts() {
        return sharedAccounts;
    }

    void submit(Runnable task) {
        pool.execute(task);
    }

    void stop() throws InterruptedException {
        pool.shutdown();

        if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
            pool.shutdownNow();
            pool.awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}