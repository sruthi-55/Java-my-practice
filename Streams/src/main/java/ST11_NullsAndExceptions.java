import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

// Optional represents missing terminal results while null elements must be handled before dereferencing
// checked failures inside standard stream lambdas must be handled or translated to unchecked exceptions

public class ST11_NullsAndExceptions {
    public static void main(String[] args) {
        System.out.println(Arrays.asList("Java", null, "SQL").stream().filter(Objects::nonNull).map(String::length).toList());	// [4, 3]
        System.out.println(Stream.of(Optional.of("Java"), Optional.<String>empty()).flatMap(Optional::stream).toList());	// [Java]
        System.out.println(Stream.<Integer>empty().max(Integer::compare).orElse(0));	// 0

        // findFirst cannot represent a selected null inside Optional
        try {
            Stream.of((String) null).findFirst();
        } catch (NullPointerException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NullPointerException
        }

        // a failed conversion aborts the pipeline and no completed result list is returned
        try {
            Stream.of("1", "bad", "3").map(Integer::parseInt).toList();
        } catch (NumberFormatException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NumberFormatException
        }

        // preserve the checked cause when translating failure for a Function-compatible mapper
        try {
            Stream.of("source").map(source -> {
                try {
                    return read(source);
                } catch (IOException exception) {
                    throw new UncheckedIOException(exception);
                }
            }).toList();
        } catch (UncheckedIOException exception) {
            System.out.println(exception.getCause().getMessage());	// read failed
        }

        // retain failed inputs explicitly when the domain permits partial success
        List<Parsed> results = Stream.of("1", "bad", "3").map(text -> {
            try {
                return new Parsed(text, Integer.parseInt(text), null);
            } catch (NumberFormatException exception) {
                return new Parsed(text, null, "invalid number");
            }
        }).toList();
        System.out.println(results.stream().filter(result -> result.error() == null).map(Parsed::value).toList());	// [1, 3]
        System.out.println(results.stream().filter(result -> result.error() != null).map(Parsed::input).toList());	// [bad]
    }

    static String read(String source) throws IOException {
        throw new IOException("read failed");
    }

    record Parsed(String input, Integer value, String error) { }

    // empty Optional and a failed computation express different outcomes and should not be silently conflated
}
