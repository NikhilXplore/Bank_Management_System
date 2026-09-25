import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Database.initialize();
        } catch (SQLException e) {
            System.err.println("Unable to connect to MySQL: " + e.getMessage());
            System.err.println("Set DB_USER and DB_PASSWORD, then run the application again.");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            boolean run = true;
            while (run) {
                printMenu();
                String choice = scanner.nextLine().trim();
                try {
                    switch (choice) {
                        case "1" -> createAccount(scanner);
                        case "2" -> Account.Deposit(scanner);
                        case "3" -> Account.Withdraw(scanner);
                        case "4" -> showBalance(scanner);
                        case "5" -> showHistory(scanner);
                        case "6" -> run = false;
                        default -> System.out.println("Invalid option.");
                    }
                } catch (IllegalArgumentException | SQLException e) {
                    System.out.println("Operation failed: " + e.getMessage());
                }
                System.out.println();
            }
        }
        System.out.println("Exiting the system. Goodbye!");
    }

    private static void printMenu() {
        System.out.println("\n=== Bank Management System ===");
        System.out.println("1. Create account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Check balance");
        System.out.println("5. Transaction history");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private static void createAccount(Scanner scanner) throws SQLException {
        Account account = Account_service.CreateAccount(scanner);
        Account_List.AddAccount(account);
        System.out.println("Account created successfully. Your account number is " + account.AccountNumber + ".");
    }

    private static void showBalance(Scanner scanner) throws SQLException {
        System.out.print("Enter account number: ");
        int accountNumber = Integer.parseInt(scanner.nextLine());
        Account account = Account_List.AccountInfo(accountNumber);
        if (account == null) {
            System.out.println("No account found.");
            return;
        }
        System.out.println("Account holder: " + account.name);
        System.out.println("Current balance: " + BigDecimal.valueOf(account.Total_amount));
    }

    private static void showHistory(Scanner scanner) throws SQLException {
        System.out.print("Enter account number: ");
        int accountNumber = Integer.parseInt(scanner.nextLine());
        List<String> history = Account_List.transactionHistory(accountNumber);
        if (history.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        history.forEach(System.out::println);
    }
}
