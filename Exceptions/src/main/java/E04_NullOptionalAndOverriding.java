import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.Optional;

// Objects.requireNonNull rejects an unexpected null with NullPointerException
// Optional explicitly represents a return value that may be present or absent
// overriding method may narrow checked exceptions and widen access but cannot do the reverse
// Optional.map turns a null result into empty while flatMap requires a non-null Optional result

public class E04_NullOptionalAndOverriding {
    public static void main(String[] args) {
        String name = Objects.requireNonNull("Sruthi", "Name cannot be null");
        Optional<String> nickname = Optional.ofNullable(null);
        System.out.println(name + " " + nickname.orElse("No nickname"));	// Sruthi No nickname

        // requireNonNull rejects invalid null inputs at the boundary
        try {
            Objects.requireNonNull(null, "Name cannot be null");
        } catch (NullPointerException exception) {
            System.out.println(exception.getMessage());	// Name cannot be null
        }

        // Optional represents expected absence; map and filter transform a present value safely
        Optional<String> skill = Optional.of("Java");
        System.out.println(skill.isPresent());	// true
        System.out.println(Optional.empty().isEmpty());	// true
        System.out.println(skill.filter(value -> value.length() > 3).map(String::length).orElse(0));	// 4
        System.out.println(skill.flatMap(value -> Optional.of(value.toUpperCase())).orElse("none"));	// JAVA

        // map treats a missing mapped value as absence while flatMap rejects a broken Optional contract
        System.out.println(skill.map(value -> (String) null).isEmpty());	// true
        try {
            skill.flatMap(value -> null);
        } catch (NullPointerException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NullPointerException
        }
        System.out.println(nickname.or(() -> Optional.of("fallback")).orElseThrow());	// fallback
        nickname.ifPresentOrElse(value -> { }, () -> System.out.println("absent"));	// absent

        // orElse evaluates its argument eagerly even when a value exists
        System.out.println(skill.orElse(fallback()));	// Java
        System.out.println(skill.orElseGet(E04_NullOptionalAndOverriding::fallback));	// Java
        try {
            nickname.orElseThrow(() -> new IllegalStateException("Nickname missing"));
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());	// Nickname missing
        }

        // the reference type determines the checked exceptions that callers must handle
        ParentReader reader = new ChildReader();
        try {
            reader.read();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());	// file missing
        }
        new SafeReader().read();

        // Optional.of rejects null while get rejects an empty Optional
        try {
            Optional.of(null);
        } catch (NullPointerException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NullPointerException
        }
        try {
            Optional.empty().get();
        } catch (NoSuchElementException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NoSuchElementException
        }

        // interface implementations may narrow checked exceptions just like class overrides
        ReadableSource source = new FileSource();
        try {
            source.read();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());	// source missing
        }

        // an implementation may throw an unchecked exception absent from the interface declaration
        Runnable task = () -> { throw new IllegalStateException("task not ready"); };
        try {
            task.run();
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());	// task not ready
        }

        // a checked failure from super prevents the subclass constructor body from executing
        try {
            new CheckedChild();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());	// parent construction failed
        }
    }

    static String fallback() {
        System.out.println("fallback evaluated");	// fallback evaluated
        return "none";
    }

    // Optional.of rejects null and get throws NoSuchElementException when empty
    // prefer Optional for possibly absent return values and avoid unchecked get calls
    // an override may omit checked exceptions or add unchecked exceptions but cannot widen checked exceptions
    // constructors are not overridden and may declare exceptions independently of parent constructors
    // a subclass constructor must declare checked exceptions from super and may declare additional types
    // in Java 21 super must be first and cannot be enclosed in the constructor body's try block
}

interface ReadableSource {
    void read() throws IOException;
}

class FileSource implements ReadableSource {
    @Override
    public void read() throws FileNotFoundException {
        throw new FileNotFoundException("source missing");
    }
}

class CheckedParent {
    CheckedParent() throws IOException {
        throw new IOException("parent construction failed");
    }
}

class CheckedChild extends CheckedParent {
    CheckedChild() throws IOException {
        super();
    }
}

class ParentReader {
    protected void read() throws IOException {
    }
}

class ChildReader extends ParentReader {
    @Override
    public void read() throws FileNotFoundException {
        throw new FileNotFoundException("file missing");
    }
}

class SafeReader extends ParentReader {
    @Override
    public void read() {
        System.out.println("read without checked exception");	// read without checked exception
    }
}
