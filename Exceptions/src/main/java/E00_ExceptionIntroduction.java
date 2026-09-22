// exception is an object representing an abnormal condition that interrupts normal program flow
// Throwable is the root of Exception and Error; RuntimeException extends Exception
// unchecked types include RuntimeException and Error subclasses; all other Throwable types are checked
// checked exceptions occur at runtime but the compiler enforces catching or declaring them
// try contains risky code, catch handles a matching exception and finally normally performs cleanup
// throw raises one exception while throws declares possible checked exceptions to the caller

public class E00_ExceptionIntroduction {
    public static void main(String[] args) {
        // a matching catch handles the failure and execution continues after finally
        try {
            System.out.println(10 / 0);	// throws ArithmeticException before printing
        } catch (ArithmeticException exception) {
            System.out.println(exception.getClass().getSimpleName());	// ArithmeticException
            System.out.println(exception.getMessage());	// / by zero
        } finally {
            System.out.println("cleanup");	// cleanup
        }
        System.out.println("continued");	// continued
    }

    // the JVM searches backward through the call stack for the nearest compatible handler
    // an unhandled exception reaches the default handler, which prints a stack trace and terminates the thread
    // finally may be skipped after abrupt JVM termination or when control never reaches it
    // Error usually signals a serious runtime problem and is not an ordinary recovery mechanism
    // OutOfMemoryError means an allocation failed; StackOverflowError commonly results from excessive recursion
    // catch Exception does not catch Error; avoid broad catch Throwable in application code
    // final restricts modification, finally is a cleanup block and finalize is obsolete object-finalization machinery
}
