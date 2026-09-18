// abstract class can contain state, constructors, abstract methods and concrete methods
// interface defines a contract and supports abstract, default, static and private methods
// class extends one class but can implement multiple interfaces

public class O03_AbstractClassAndInterface {
    public static void main(String[] args) {
        EmailNotification notification = new EmailNotification("user@example.com");
        notification.send("Interview at 10 AM");
        notification.audit();
        notification.retry();
        Auditable.about();
    }
}

abstract class Notification {
    private final String recipient;

    Notification(String recipient) {
        this.recipient = recipient;
    }

    String recipient() {
        return recipient;
    }

    abstract void send(String message);
}

interface Auditable {
    void audit();

    // default methods let interfaces add behavior without breaking implementors
    default String eventName() {
        return normalize("notification_sent");
    }

    static void about() {
        System.out.println("audit contract");	// audit contract
    }

    private String normalize(String value) {
        return value.toUpperCase();
    }
}

interface Retryable {
    default void retry() {
        System.out.println("retrying");	// retrying
    }
}

class EmailNotification extends Notification implements Auditable, Retryable {
    EmailNotification(String recipient) {
        super(recipient);
    }

    @Override
    void send(String message) {
        System.out.println("Email to " + recipient() + ": " + message);	// Email to user@example.com: Interview at 10 AM
    }

    @Override
    public void audit() {
        System.out.println(eventName());	// NOTIFICATION_SENT
    }
}
