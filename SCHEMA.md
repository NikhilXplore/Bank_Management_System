# Database Schema

The application uses the MySQL database `bank_management`. `Database.initialize()` creates the database and the tables below when they do not already exist.

## `accounts`

One row represents one bank account. New account numbers start at `1000`.

| Column | Type | Constraints and meaning |
| --- | --- | --- |
| `account_number` | `INT` | Primary key; auto-incrementing account identifier, starting at 1000. |
| `name` | `VARCHAR(100)` | Required account holder name. |
| `account_type` | `ENUM('Savings', 'Current')` | Required account type. |
| `phone` | `VARCHAR(20)` | Required phone number. |
| `email` | `VARCHAR(150)` | Required and unique email address. |
| `address` | `VARCHAR(255)` | Required mailing address. |
| `date_of_birth` | `DATE` | Required date of birth. |
| `balance` | `DECIMAL(15,2)` | Required balance; defaults to `0.00`. |
| `created_at` | `TIMESTAMP` | Required creation time; defaults to the current timestamp. |

## `transactions`

One row records a deposit or withdrawal made against an account. Opening funds entered during account creation are stored in `accounts.balance`; they do not create a transaction row.

| Column | Type | Constraints and meaning |
| --- | --- | --- |
| `transaction_id` | `BIGINT` | Primary key; auto-incrementing transaction identifier. |
| `account_number` | `INT` | Required foreign key referencing `accounts.account_number`. |
| `transaction_type` | `ENUM('DEPOSIT', 'WITHDRAWAL')` | Required transaction kind. |
| `amount` | `DECIMAL(15,2)` | Required amount of the transaction. |
| `balance_after` | `DECIMAL(15,2)` | Required account balance after the transaction. |
| `created_at` | `TIMESTAMP` | Required transaction time; defaults to the current timestamp. |

## Relationship

An account can have zero or more transactions. Each transaction belongs to exactly one account through `transactions.account_number`. The foreign key does not specify cascading deletes, so MySQL's default foreign-key behavior prevents deleting an account while transaction rows reference it.