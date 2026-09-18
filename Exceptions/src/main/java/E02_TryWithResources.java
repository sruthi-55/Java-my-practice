import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

// try-with-resources automatically closes resources implementing AutoCloseable
// resources close in reverse declaration order even when the try block throws

public class E02_TryWithResources {
    public static void main(String[] args) {
        try {
            System.out.println(firstLine("Java\nSpring"));	// Java
        } catch (IOException exception) {
            System.out.println("Could not read data: " + exception.getMessage());	// printed only when reading fails
        }

        // multiple resources close in reverse declaration order
        try (TrackedResource first = new TrackedResource("first");
             TrackedResource second = new TrackedResource("second")) {
            System.out.println("using resources");	// using resources
        }
    }

    static String firstLine(String value) throws IOException {
        try (BufferedReader reader = new BufferedReader(new StringReader(value))) {
            return reader.readLine();
        }
    }
}

class TrackedResource implements AutoCloseable {
    private final String name;

    TrackedResource(String name) {
        this.name = name;
    }

    @Override
    public void close() {
        System.out.println("closed " + name);	// closed second, then closed first
    }
}
