// exception chaining wraps a lower-level cause with meaningful domain context
// getCause retrieves the original exception preserved by a chaining constructor

public class E03_ExceptionChaining {
    public static void main(String[] args) {
        try {
            parseAge("twenty");
        } catch (InvalidUserDataException exception) {
            System.out.println(exception.getMessage());	// Age must be a number
            System.out.println(exception.getCause().getClass().getSimpleName());	// NumberFormatException
        }
    }

    static int parseAge(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            // exception chaining preserves the original cause while adding domain context
            throw new InvalidUserDataException("Age must be a number", exception);
        }
    }
}

class InvalidUserDataException extends RuntimeException {
    InvalidUserDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
