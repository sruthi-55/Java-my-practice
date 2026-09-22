import java.io.IOException;
import java.sql.SQLException;

// propagation unwinds the current call stack until a compatible catch is found
// multiple catches handle different types separately while multi-catch shares one handler

public class E05_CatchAndPropagation {
    public static void main(String[] args) {
        // a failure inside catch skips sibling catches and reaches an enclosing handler
        try {
            try {
                throw new IllegalArgumentException("input failed");
            } catch (IllegalArgumentException exception) {
                throw new IllegalStateException("handler failed", exception);
            } catch (IllegalStateException exception) {
                System.out.println("sibling handler");	// not reached
            }
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());	// handler failed
        }

        // specific catches precede general catches and only the first matching catch runs
        try {
            Integer.parseInt("Java");
        } catch (NumberFormatException exception) {
            System.out.println("invalid number");	// invalid number
        } catch (IllegalArgumentException exception) {
            System.out.println("invalid argument");	// not reached in this example
        }

        // unrelated exception types can share a multi-catch handler
        for (boolean database : new boolean[]{false, true}) {
            try {
                load(database);
            } catch (IOException | SQLException exception) {
                System.out.println(exception.getClass().getSimpleName());	// IOException, then SQLException
            }
        }

        // a catch can rethrow to an outer handler while finally executes during stack unwinding
        try {
            propagate();
        } catch (IOException | SQLException exception) {
            System.out.println(exception.getMessage());	// disk failed
        }
    }

    static void load(boolean database) throws IOException, SQLException {
        if (database) throw new SQLException("database failed");
        throw new IOException("disk failed");
    }

    // precise rethrow infers the checked types when the caught variable is not reassigned
    static void propagate() throws IOException, SQLException {
        try {
            load(false);
        } catch (Exception exception) {
            throw exception;
        } finally {
            System.out.println("unwinding");	// unwinding
        }
    }

    // a parent catch before a child catch is a compile-time error
    // multi-catch alternatives cannot be related by inheritance and its parameter is implicitly final
    // a plain try needs catch or finally; try-with-resources may omit both
    // rethrowing the same exception preserves its original stack trace
    // catching a specific checked type that try cannot throw is normally a compile-time error
    // catch Exception is permitted even when no checked exception is declared by the try body
    // throws declares possible failures but neither throws an exception nor handles it
    // Throwable subclasses cannot be generic classes
}
