import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class Account_List {
    private Account_List() {
    }

    public static Account AddAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (name, account_type, phone, email, address, date_of_birth, balance) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.name);
            statement.setString(2, account.AccountType);
            statement.setString(3, account.phoneNumber);
            statement.setString(4, account.EmailID);
            statement.setString(5, account.Address);
            statement.setObject(6, account.DOB);
            statement.setBigDecimal(7, BigDecimal.valueOf(account.Total_amount));
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) account.AccountNumber = keys.getInt(1);
            }
        }
        return account;
    }

    public static Account AccountInfo(int accountNumber) throws SQLException {
        String sql = "SELECT account_number, name, account_type, phone, email, address, date_of_birth, balance "
                + "FROM accounts WHERE account_number = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountNumber);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Account.fromResultSet(result) : null;
            }
        }
    }

    public static boolean validation(int accountNumber) throws SQLException {
        return AccountInfo(accountNumber) != null;
    }

    public static BigDecimal changeBalance(int accountNumber, BigDecimal amount, String type) throws SQLException {
        String update = type.equals("WITHDRAWAL")
                ? "UPDATE accounts SET balance = balance - ? WHERE account_number = ? AND balance >= ?"
                : "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        String insert = "INSERT INTO transactions (account_number, transaction_type, amount, balance_after) "
                + "SELECT account_number, ?, ?, balance FROM accounts WHERE account_number = ?";
        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(update)) {
                statement.setBigDecimal(1, amount);
                statement.setInt(2, accountNumber);
                if (type.equals("WITHDRAWAL")) statement.setBigDecimal(3, amount);
                if (statement.executeUpdate() != 1) {
                    connection.rollback();
                    throw new IllegalArgumentException("Account not found or insufficient funds.");
                }
            }
            try (PreparedStatement statement = connection.prepareStatement(insert)) {
                statement.setString(1, type);
                statement.setBigDecimal(2, amount);
                statement.setInt(3, accountNumber);
                statement.executeUpdate();
            }
            connection.commit();
        }
        return BigDecimal.valueOf(AccountInfo(accountNumber).Total_amount);
    }

    public static List<String> transactionHistory(int accountNumber) throws SQLException {
        List<String> history = new ArrayList<>();
        String sql = "SELECT transaction_type, amount, balance_after, created_at FROM transactions "
                + "WHERE account_number = ? ORDER BY transaction_id DESC";
        try (Connection connection = Database.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountNumber);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    history.add(String.format("%s | %s | amount: %s | balance: %s", result.getTimestamp("created_at"),
                            result.getString("transaction_type"), result.getBigDecimal("amount"),
                            result.getBigDecimal("balance_after")));
                }
            }
        }
        return history;
    }
}
