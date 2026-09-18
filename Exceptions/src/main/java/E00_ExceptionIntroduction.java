// exception is an object representing an abnormal condition that interrupts normal program flow
// checked exceptions are compiler-enforced while unchecked exceptions extend RuntimeException
// try contains risky code, catch handles a matching exception and finally normally performs cleanup
// throw raises one exception while throws declares possible checked exceptions to the caller

public class E00_ExceptionIntroduction {
    public static void main(String[] args) {
        try {
            System.out.println(10 / 0);	// throws ArithmeticException before printing
        } catch (ArithmeticException exception) {
            System.out.println(exception.getClass().getSimpleName());	// ArithmeticException
            System.out.println(exception.getMessage());	// / by zero
        } finally {
            System.out.println("cleanup");	// cleanup
        }
    }

    // the JVM searches backward through the call stack for the nearest compatible handler
    // an unhandled exception reaches the default handler, which prints a stack trace and terminates the thread
    // finally may be skipped after abrupt JVM termination or when control never reaches it
}
