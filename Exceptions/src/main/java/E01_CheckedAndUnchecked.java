import java.io.IOException;

// checked exception must be caught or declared while unchecked exception is not compiler-enforced
// custom checked exception extends Exception and custom unchecked exception extends RuntimeException

public class E01_CheckedAndUnchecked {
    public static void main(String[] args) {
        try {
            readConfiguration("");
        } catch (IOException exception) {
            System.out.println(exception.getMessage());	// Path cannot be blank
        }

        try {
            withdraw(100, 200);
        } catch (InsufficientBalanceException exception) {
            System.out.println(exception.getMessage());	// Insufficient balance
        }

        try {
            validateUser("");
        } catch (InvalidUserException exception) {
            System.out.println(exception.getMessage());	// User cannot be blank
        }
    }

    static void readConfiguration(String path) throws IOException {
        if (path.isBlank()) throw new IOException("Path cannot be blank");
    }

    static void withdraw(int balance, int amount) {
        if (amount > balance) throw new InsufficientBalanceException("Insufficient balance");
    }

    static void validateUser(String name) throws InvalidUserException {
        if (name.isBlank()) throw new InvalidUserException("User cannot be blank");
    }
}

// custom checked exception extends Exception and must be caught or declared
class InvalidUserException extends Exception {
    InvalidUserException(String message) {
        super(message);
    }
}

class InsufficientBalanceException extends RuntimeException {
    InsufficientBalanceException(String message) {
        super(message);
    }
}
