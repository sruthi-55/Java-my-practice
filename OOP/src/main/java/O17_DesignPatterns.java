import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// design patterns name reusable collaborations rather than language features
// strategy and dependency injection are demonstrated in O16 while the holder singleton is in O03

public class O17_DesignPatterns {
    public static void main(String[] args) {
        // a simple factory hides concrete construction behind a common contract
        Message message = create("plain");
        System.out.println(message.text());	// Java

        // a decorator preserves the contract while adding behavior around an existing object
        Message decorated = new BracketMessage(message);
        System.out.println(decorated.text());	// [Java]

        // an adapter translates an incompatible legacy API into the expected contract
        LegacyMessage legacy = new LegacyMessage();
        Message adapted = legacy::oldText;
        System.out.println(adapted.text());	// legacy

        Request request = new Request.Builder("/users").timeout(5).build();
        System.out.println(request);	// Request[path=/users, timeout=5]

        // observers are notified through a callback contract and can unsubscribe
        Events events = new Events();
        Consumer<String> listener = value -> System.out.println(value);	// saved
        events.subscribe(listener);
        events.publish("saved");
        events.unsubscribe(listener);
        events.publish("not delivered");
    }

    interface Message { String text(); }

    static Message create(String kind) {
        if (!"plain".equals(kind)) throw new IllegalArgumentException("unknown kind");
        return () -> "Java";
    }

    record BracketMessage(Message delegate) implements Message {
        @Override
        public String text() { return "[" + delegate.text() + "]"; }
    }

    static class LegacyMessage {
        String oldText() { return "legacy"; }
    }

    record Request(String path, int timeout) {
        Request {
            if (path == null || path.isBlank() || timeout <= 0) throw new IllegalArgumentException("invalid request");
        }

        static class Builder {
            private final String path;
            private int timeout = 1;
            Builder(String path) { this.path = path; }
            Builder timeout(int seconds) { timeout = seconds; return this; }
            Request build() { return new Request(path, timeout); }
        }
    }

    static class Events {
        private final List<Consumer<String>> listeners = new ArrayList<>();
        void subscribe(Consumer<String> listener) { listeners.add(listener); }
        void unsubscribe(Consumer<String> listener) { listeners.remove(listener); }
        void publish(String event) { List.copyOf(listeners).forEach(listener -> listener.accept(event)); }
    }

    // this observer example is single-threaded and does not promise concurrent subscription safety
    // prefer the simplest suitable design rather than introducing patterns without a concrete need
}
