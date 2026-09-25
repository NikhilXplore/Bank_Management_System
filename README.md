# Bank Management System

Console-based Java application for managing bank accounts and recording deposits and withdrawals. MySQL stores account details, balances, and transaction history between runs.

## Features

- Create Savings and Current accounts
- Deposit funds and withdraw funds, with insufficient-funds checks
- Check an account's balance
- View an account's deposit and withdrawal history
- Create the database and tables automatically at startup
- Commit balance changes and their transaction records in a single database transaction

Authentication, account updates/deletion, transfers, loans, and customer support are not currently implemented.

## Requirements

- Java 17 or newer
- MySQL 8.x running locally
- A MySQL user with permission to create the `bank_management` database and tables

The MySQL JDBC driver is included at `lib/mysql-connector-j-9.4.0.jar`.

## Build and run

From the project root, set the MySQL credentials and compile and run the application in PowerShell:

```powershell
$env:DB_USER = "root"
$env:DB_PASSWORD = "your-password"
javac -cp "lib\mysql-connector-j-9.4.0.jar" -d bin src\*.java
java -cp "bin;lib\mysql-connector-j-9.4.0.jar" Main
```

The application reads its connection settings from `DB_URL`, `DB_USER`, and `DB_PASSWORD`. `DB_URL` defaults to `jdbc:mysql://localhost:3306/bank_management?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true`; the default user is `root` and the default password is empty. Set credentials appropriate for your local MySQL installation before running.

On startup, the application creates the database and tables if they do not already exist. See [SCHEMA.md](SCHEMA.md) for the table definitions and relationships.
