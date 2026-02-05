import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

class BankAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accountNumber;
    private String accountHolder;
    private double balance;

    public BankAccount(String accountNumber, String accountHolder, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolder() { return accountHolder; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: ₹" + amount);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: ₹" + amount);
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }

    @Override
    public String toString() {
        return "Account Number: " + accountNumber +
               ", Account Holder: " + accountHolder +
               ", Balance: ₹" + balance;
    }
}

public class BankAccountManagement {
    private static HashMap<String, BankAccount> accounts = new HashMap<>();
    private static final String FILE_NAME = "accounts.dat";
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadAccounts(); // Load accounts from file at start

        while (true) {
            System.out.println("\n--- Bank Account Management ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Balance");
            System.out.println("5. View All Accounts");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> deposit();
                case 3 -> withdraw();
                case 4 -> viewBalance();
                case 5 -> viewAllAccounts();
                case 6 -> {
                    saveAccounts(); // Save accounts to file before exiting
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void createAccount() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        if (accounts.containsKey(accNo)) {
            System.out.println("Account already exists!");
            return;
        }
        System.out.print("Enter Account Holder Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Initial Deposit: ₹");
        double initialDeposit = sc.nextDouble();
        sc.nextLine(); // consume newline
        BankAccount account = new BankAccount(accNo, name, initialDeposit);
        accounts.put(accNo, account);
        System.out.println("Account created successfully!");
        saveAccounts(); // Save immediately after creating account
    }

    private static void deposit() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        BankAccount account = accounts.get(accNo);
        if (account != null) {
            System.out.print("Enter amount to deposit: ₹");
            double amount = sc.nextDouble();
            sc.nextLine();
            account.deposit(amount);
            saveAccounts();
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void withdraw() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        BankAccount account = accounts.get(accNo);
        if (account != null) {
            System.out.print("Enter amount to withdraw: ₹");
            double amount = sc.nextDouble();
            sc.nextLine();
            account.withdraw(amount);
            saveAccounts();
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void viewBalance() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        BankAccount account = accounts.get(accNo);
        if (account != null) {
            System.out.println(account);
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void viewAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            accounts.values().forEach(System.out::println);
        }
    }

    // Load accounts from file
    @SuppressWarnings("unchecked")
    private static void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (HashMap<String, BankAccount>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found is fine on first run
            accounts = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
            accounts = new HashMap<>();
        }
    }

    // Save accounts to file
    private static void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }
}
