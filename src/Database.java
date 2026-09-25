import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {
    private static final String URL = System.getenv().getOrDefault(
    "DB_URL", "jdbc:mysql://localhost:3306/bank_management?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true"
);
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "");

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initialize() throws SQLException {
        String databaseUrl = URL.substring(0, URL.indexOf("/bank_management"))
            + "/?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
        try (Connection connection = DriverManager.getConnection(databaseUrl, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS bank_management");
        }
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS accounts ("
                    + "account_number INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL,"
                    + "account_type ENUM('Savings', 'Current') NOT NULL, phone VARCHAR(20) NOT NULL,"
                    + "email VARCHAR(150) NOT NULL UNIQUE, address VARCHAR(255) NOT NULL,"
                    + "date_of_birth DATE NOT NULL, balance DECIMAL(15,2) NOT NULL DEFAULT 0,"
                    + "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP) AUTO_INCREMENT = 1000");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS transactions ("
                    + "transaction_id BIGINT PRIMARY KEY AUTO_INCREMENT, account_number INT NOT NULL,"
                    + "transaction_type ENUM('DEPOSIT', 'WITHDRAWAL') NOT NULL, amount DECIMAL(15,2) NOT NULL,"
                    + "balance_after DECIMAL(15,2) NOT NULL, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY (account_number) REFERENCES accounts(account_number))");
        }
    }
}