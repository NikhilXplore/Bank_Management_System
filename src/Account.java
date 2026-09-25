import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class Account {
    // this class is to create a new account.

    // intializations
    int AccountNumber;
    String name;
    int Initial_Deposit;
    String AccountType;
    String phoneNumber;
    String EmailID; 
    String Address;
    LocalDate DOB;

    int Total_amount;

    // constructor
    Account(String name, int Deposit, int AccountType, String phone, String Email, String Address, LocalDate Dob) {
        this(0, name, Deposit, AccountType == 1 ? "Savings" : "Current", phone, Email, Address, Dob);
    }

    private Account(int accountNumber, String name, int deposit, String accountType, String phone, String email,
            String address, LocalDate dob) {
        this.AccountNumber = accountNumber;
        this.name = name;
        this.Initial_Deposit = deposit;
        this.AccountType = accountType;
        this.phoneNumber = phone;
        this.EmailID = email;
        this.Address = address;
        this.DOB = dob;
        this.Total_amount = deposit;
    }

    static Account fromResultSet(ResultSet result) throws SQLException {
        Account account = new Account(result.getInt("account_number"), result.getString("name"),
                result.getBigDecimal("balance").intValue(), result.getString("account_type"),
                result.getString("phone"), result.getString("email"), result.getString("address"),
                result.getDate("date_of_birth").toLocalDate());
        account.Total_amount = result.getBigDecimal("balance").intValue();
        return account;
    }

    // Deposite
    public static void Deposit(Scanner sc) throws SQLException {
        System.out.println("Enter your Account Number: ");
        int AN = sc.nextInt();

        // validation
        while (!Account_List.validation(AN)) {
            System.out.println(
                    "There is no account with this account number. Please check your account number and try again. ");
            System.out.println("Enter your Account Number: ");
            AN = sc.nextInt();
        }

        Account ac = Account_List.AccountInfo(AN);
        String name = ac.name;
        System.out.println("Welcome " + name);

        System.out.println("Please enter the deposite amount: ");
        int amount = sc.nextInt();
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero.");
        BigDecimal balance = Account_List.changeBalance(AN, BigDecimal.valueOf(amount), "DEPOSIT");
        System.out.println(amount + " has been deposited. Current balance: " + balance);

    }

    //Withdrawl
    public static void Withdraw(Scanner sc) throws SQLException {
        System.out.println("Enter Your Account Numbe: " );
        int an = sc.nextInt();

        // validating an
        while (!Account_List.validation(an)) {
            System.out.println("No account found. Enter a valid account number: ");
            an = sc.nextInt();
        }
        System.out.println("Enter withdrawal amount: ");
        int amount = sc.nextInt();
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero.");
        BigDecimal balance = Account_List.changeBalance(an, BigDecimal.valueOf(amount), "WITHDRAWAL");
        System.out.println(amount + " has been withdrawn. Current balance: " + balance);
    }
}
