import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

// Objects.requireNonNull rejects an unexpected null with NullPointerException
// Optional explicitly represents a return value that may be present or absent
// overriding method may narrow checked exceptions and widen access but cannot do the reverse

public class E04_NullOptionalAndOverriding {
    public static void main(String[] args) {
        String name = Objects.requireNonNull("Sruthi", "Name cannot be null");
        Optional<String> nickname = Optional.ofNullable(null);
        System.out.println(name + " " + nickname.orElse("No nickname"));	// Sruthi No nickname
    }

    // a checked exception must be caught or declared while an unchecked RuntimeException is not compiler-enforced
    // throw creates an exception occurrence while throws declares possible checked exceptions in a method signature
    // the JVM searches backward through the call stack for a compatible handler and otherwise prints a stack trace
    // Throwable exposes getMessage, toString, printStackTrace, getCause and initCause
    // finally normally runs for cleanup but can be skipped by abrupt JVM termination or non-termination
}

class ParentReader {
    protected void read() throws IOException {
    }
}

class ChildReader extends ParentReader {
    @Override
    public void read() throws FileNotFoundException {
    }
}
