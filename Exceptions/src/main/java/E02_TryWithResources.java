import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

// try-with-resources automatically closes resources implementing AutoCloseable
// resources close in reverse declaration order even when the try block throws
// AutoCloseable.close may throw Exception; Closeable.close declares IOException and must tolerate repeated closure

public class E02_TryWithResources {
    public static void main(String[] args) throws IOException {
        fileFailures();
        closeContracts();
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

        // Java 9 permits an existing final or effectively final variable as a resource
        BufferedReader reader = new BufferedReader(new StringReader("existing resource"));
        try (reader) {
            System.out.println(reader.readLine());	// existing resource
        } catch (IOException exception) {
            throw new IllegalStateException("Reading failed", exception);
        }

        // the body failure stays primary and close failures become suppressed in closing order
        try (FailingResource first = new FailingResource("first");
             FailingResource second = new FailingResource("second")) {
            throw new IOException("body failed");
        } catch (IOException exception) {
            System.out.println(exception.getMessage());	// body failed
            for (Throwable suppressed : exception.getSuppressed()) {
                System.out.println(suppressed.getMessage());	// second close failed, then first close failed
            }
        }

        // a close failure is primary when the body completes normally
        try (FailingResource resource = new FailingResource("only")) {
        } catch (IOException exception) {
            System.out.println(exception.getMessage());	// only close failed
            System.out.println(exception.getSuppressed().length);	// 0
        }

        // earlier resources close when a later resource cannot be initialized
        try (TrackedResource first = new TrackedResource("first");
             TrackedResource second = openFailingResource()) {
        } catch (IOException exception) {
            System.out.println(exception.getMessage());	// open failed
        }
    }

    static TrackedResource openFailingResource() throws IOException {
        throw new IOException("open failed");
    }

    // missing files produce different IOException subtypes in classic I/O and NIO
    static void fileFailures() throws IOException {
        Path directory = Files.createTempDirectory("java-exception-demo-");
        Path missing = directory.resolve("missing.txt");
        try {
            try (FileInputStream input = new FileInputStream(missing.toFile())) {
                input.read();
            } catch (FileNotFoundException exception) {
                System.out.println(exception.getClass().getSimpleName());	// FileNotFoundException
            }
            try (BufferedReader reader = Files.newBufferedReader(missing)) {
                reader.readLine();
            } catch (NoSuchFileException exception) {
                System.out.println(exception.getClass().getSimpleName());	// NoSuchFileException
            }
        } finally {
            Files.delete(directory);
        }
    }

    // Closeable extends AutoCloseable and a closed BufferedReader tolerates another close
    static void closeContracts() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader("Java"));
        Closeable resource = reader;
        resource.close();
        resource.close();
        try {
            reader.readLine();
        } catch (IOException exception) {
            System.out.println(exception.getClass().getSimpleName());	// IOException
        }

        // AutoCloseable permits a broader checked exception and does not require idempotent close
        AutoCloseable failing = () -> { throw new Exception("close failed"); };
        try (failing) {
        } catch (Exception exception) {
            System.out.println(exception.getMessage());	// close failed
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
        System.out.println("closed " + name);	// closed second or closed first
    }
}

class FailingResource implements AutoCloseable {
    private final String name;

    FailingResource(String name) {
        this.name = name;
    }

    @Override
    public void close() throws IOException {
        throw new IOException(name + " close failed");
    }
}
