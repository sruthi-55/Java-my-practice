import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Stream;

// a lambda may throw checked exceptions only when its functional method permits them
// stream failures propagate from terminal evaluation and stop the current pipeline

public class E08_LambdaAndStreamExceptions {
    public static void main(String[] args) {
        // a custom functional interface can declare a checked exception
        CheckedReader reader = () -> { throw new IOException("read failed"); };
        try {
            reader.read();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());	// read failed
        }

        // Function does not declare IOException so the lambda wraps it while preserving the cause
        try {
            List.of("source").stream().map(value -> {
                try {
                    return reader.read();
                } catch (IOException exception) {
                    throw new UncheckedIOException(exception);
                }
            }).toList();
        } catch (UncheckedIOException exception) {
            System.out.println(exception.getCause().getMessage());	// read failed
        }

        // the first invalid number aborts this sequential pipeline when toList evaluates it
        Stream<Integer> numbers = Stream.of("10", "bad", "30").map(Integer::parseInt);
        try {
            numbers.toList();
        } catch (NumberFormatException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NumberFormatException
        }

        // partial success retains a result or error for every input rather than silently dropping failures
        List<ParseResult> results = Stream.of("10", "bad", "30").map(value -> {
            try {
                return new ParseResult(value, Integer.parseInt(value), null);
            } catch (NumberFormatException exception) {
                return new ParseResult(value, null, "invalid integer");
            }
        }).toList();
        System.out.println(results.stream().filter(result -> result.error() == null).map(ParseResult::value).toList());	// [10, 30]
        System.out.println(results.stream().filter(result -> result.error() != null).map(ParseResult::input).toList());	// [bad]
    }

    record ParseResult(String input, Integer value, String error) {
    }

    @FunctionalInterface
    interface CheckedReader {
        String read() throws IOException;
    }

    // handle per-element failures only when the domain permits partial success and record rejected items
    // parallel streams may process other elements before failure is observed and do not roll back side effects
}
