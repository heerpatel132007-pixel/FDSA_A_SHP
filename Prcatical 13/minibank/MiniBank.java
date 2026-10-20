package minibank;

public class MiniBank {

    public static void main(String[] args) {

      
        Account account1 = new Account(101, 5000);
        Account account2 = new Account(102, 3000);
        Account account3 = new Account(103, 7000);

      
        Repository<Account> accountRepository =
                new Repository<>();

        
        accountRepository.saveAll(
                account1, account2, account3);

        System.out.println("===== ACCOUNTS SAVED =====");

        
        for (Account account : accountRepository.findAll()) {
            System.out.println(account);
        }

        
        System.out.println("\n===== SEARCH ACCOUNT =====");

        Account result = accountRepository.findById("102");

        System.out.println(result);

      
        System.out.println("\n===== DELETE ACCOUNT =====");

        System.out.println(
                "Account deleted: "
                        + accountRepository.delete("101"));

        
        System.out.println("\n===== REMAINING ACCOUNTS =====");

        for (Account account : accountRepository.findAll()) {
            System.out.println(account);
        }

       
        System.out.println("\n===== GENERIC AVERAGE =====");

        Integer[] values = {10, 20, 30, 40, 50};

        System.out.println(
                "Average: " + Repository.average(values));
    }
}