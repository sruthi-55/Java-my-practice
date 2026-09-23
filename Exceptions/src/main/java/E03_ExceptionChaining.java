import java.io.PrintWriter;
import java.io.StringWriter;

// exception chaining wraps a lower-level cause with meaningful domain context
// getCause retrieves the original exception preserved by a chaining constructor
// a cause explains why an operation failed while suppressed exceptions record secondary failures
// initCause can initialize the cause once and a cause-taking constructor initializes it even when given null

public class E03_ExceptionChaining {
    public static void main(String[] args) {
        // custom exceptions explicitly forward the constructor forms needed by callers
        IllegalArgumentException cause = new IllegalArgumentException("invalid age");
        System.out.println(new InvalidUserDataException().getMessage());	// null
        System.out.println(new InvalidUserDataException("invalid user").getMessage());	// invalid user
        System.out.println(new InvalidUserDataException(cause).getMessage());	// java.lang.IllegalArgumentException: invalid age
        System.out.println(new InvalidUserDataException("invalid user", cause).getCause() == cause);	// true

        try {
            parseAge("twenty");
        } catch (InvalidUserDataException exception) {
            System.out.println(exception.getMessage());	// Age must be a number
            System.out.println(exception.getCause().getClass().getSimpleName());	// NumberFormatException
        }

        // initCause attaches a cause once when no cause constructor has initialized it
        Exception failure = new Exception("load failed");
        failure.initCause(new IllegalArgumentException("invalid id"));
        failure.addSuppressed(new IllegalStateException("cleanup failed"));
        System.out.println(failure.getMessage());	// load failed
        System.out.println(failure.getLocalizedMessage());	// load failed
        System.out.println(failure.toString());	// java.lang.Exception: load failed
        System.out.println(failure.getCause().getMessage());	// invalid id
        System.out.println(failure.getSuppressed()[0].getMessage());	// cleanup failed

        // stack frames identify the creation site unless the trace is explicitly refreshed or replaced
        StackTraceElement frame = failure.getStackTrace()[0];
        System.out.println(frame.getMethodName());	// main
        System.out.println(frame.getFileName());	// E03_ExceptionChaining.java

        // printStackTrace includes the type, frames, causes and suppressed failures
        StringWriter trace = new StringWriter();
        failure.printStackTrace(new PrintWriter(trace));
        System.out.println(trace.toString().contains("Caused by:"));	// true
        System.out.println(trace.toString().contains("Suppressed:"));	// true

        // a cause cannot be overwritten after it has been initialized
        try {
            failure.initCause(new Exception("replacement"));
        } catch (IllegalStateException exception) {
            System.out.println(exception.getClass().getSimpleName());	// IllegalStateException
        }

        // the protected four-argument constructor can disable suppression and stack-trace collection
        RuntimeException lightweight = new ConfigurableFailure();
        lightweight.addSuppressed(new Exception("cleanup"));
        System.out.println(lightweight.getSuppressed().length);	// 0
        System.out.println(lightweight.getStackTrace().length);	// 0
    }

    static int parseAge(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            // exception chaining preserves the original cause while adding domain context
            throw new InvalidUserDataException("Age must be a number", exception);
        }
    }

    // disabling diagnostics is specialized behavior and should not be the default for application failures
    static final class ConfigurableFailure extends RuntimeException {
        ConfigurableFailure() { super("controlled failure", null, false, false); }
    }
}

class InvalidUserDataException extends RuntimeException {
    InvalidUserDataException() {
        super();
    }

    InvalidUserDataException(String message) {
        super(message);
    }

    InvalidUserDataException(Throwable cause) {
        super(cause);
    }

    InvalidUserDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
