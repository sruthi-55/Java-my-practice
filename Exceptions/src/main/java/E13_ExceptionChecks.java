import java.io.IOException;

// exception tests check failure type, diagnostics, cleanup and state rather than only successful execution
// these checks use core Java and throw AssertionError explicitly so they work without the -ea flag

public class E13_ExceptionChecks {
    public static void main(String[] args) {
        // verify invalid input throws the expected type and preserves the original balance
        int[] balance = {100};
        IllegalArgumentException failure = expect(IllegalArgumentException.class,
                () -> E12_StateAndLockSafety.withdraw(balance, 150));
        check("insufficient balance".equals(failure.getMessage()), "wrong message");
        check(balance[0] == 100, "balance changed after rejection");
        System.out.println("type, message and state verified");	// type, message and state verified

        // verify wrapping retains the original cause
        InvalidUserDataException wrapped = expect(InvalidUserDataException.class,
                () -> E03_ExceptionChaining.parseAge("bad"));
        check(wrapped.getCause() instanceof NumberFormatException, "cause lost");
        System.out.println("cause verified");	// cause verified

        // verify automatic closure and suppression without replacing the body failure
        boolean[] closed = {false};
        IOException primary = expect(IOException.class, () -> {
            try (AutoCloseable resource = () -> {
                closed[0] = true;
                throw new IOException("close failed");
            }) {
                throw new IOException("body failed");
            }
        });
        check(closed[0], "resource leaked");
        check("body failed".equals(primary.getMessage()), "primary failure lost");
        check(primary.getSuppressed().length == 1, "close failure lost");
        check("close failed".equals(primary.getSuppressed()[0].getMessage()), "wrong suppressed failure");
        System.out.println("cleanup and suppression verified");	// cleanup and suppression verified
    }

    // broad catching is limited to this test helper so unexpected exceptions fail the check
    static <T extends Exception> T expect(Class<T> type, CheckedAction action) {
        try {
            action.run();
        } catch (Exception exception) {
            if (type.isInstance(exception)) return type.cast(exception);
            throw new AssertionError("unexpected exception type", exception);
        }
        throw new AssertionError("expected " + type.getSimpleName());
    }

    static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    @FunctionalInterface
    interface CheckedAction {
        void run() throws Exception;
    }
}
