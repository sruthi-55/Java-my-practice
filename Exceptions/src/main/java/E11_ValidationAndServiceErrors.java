import java.io.IOException;
import java.util.Objects;

// validation rejects invalid input before work begins and service exceptions describe domain failures
// a boundary converts internal failures to safe public errors while diagnostics retain the original cause

public class E11_ValidationAndServiceErrors {
    public static void main(String[] args) {
        // constructor validation prevents creation of an invalid object
        try {
            new Order(-1);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());	// quantity must be positive
        }

        // wrap an infrastructure failure once where it crosses into the service layer
        try {
            placeOrder(new Order(2));
        } catch (OrderException exception) {
            System.out.println(exception.code());	// ORDER_STORAGE_FAILED
            System.out.println(exception.getCause().getClass().getSimpleName());	// IOException
            System.out.println(publicMessage(exception));	// Order could not be saved
        }

        // assertions check internal assumptions and must not replace always-enabled input validation
        int total = new Order(2).quantity() * 10;
        assert total > 0 : "total must be positive";
        System.out.println(total);	// 20

        // a failed assertion throws AssertionError when assertions are enabled with -ea
        try {
            assert false : "internal assumption failed";
        } catch (AssertionError error) {
            System.out.println(error.getMessage());	// internal assumption failed when run with -ea
        }
    }

    record Order(int quantity) {
        Order {
            if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive");
        }
    }

    static void placeOrder(Order order) {
        Objects.requireNonNull(order, "order");
        try {
            save(order);
        } catch (IOException exception) {
            throw new OrderException("ORDER_STORAGE_FAILED", "Saving order failed", exception);
        }
    }

    static void save(Order order) throws IOException {
        throw new IOException("storage unavailable");
    }

    static String publicMessage(OrderException exception) {
        return "ORDER_STORAGE_FAILED".equals(exception.code()) ? "Order could not be saved" : "Request failed";
    }

    static class OrderException extends RuntimeException {
        private final String code;

        OrderException(String code, String message, Throwable cause) {
            super(message, cause);
            this.code = code;
        }

        String code() {
            return code;
        }
    }

    // choose checked exceptions when callers should explicitly handle a recoverable contract failure
    // choose unchecked exceptions for invalid use or failures handled at an application boundary
    // production logging should include the exception object and correlation id at the handling boundary
    // do not log passwords, tokens or internal stack traces in public error responses
    // do not swallow failures, return misleading success or log the same failure at every layer
    // retries require transient failures, bounded attempts and an idempotent operation
    // enable assertions with java -ea; assert throws AssertionError only when enabled
}
