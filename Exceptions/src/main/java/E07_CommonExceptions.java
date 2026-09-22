import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;

// runtime exceptions often indicate invalid inputs, invalid state or incorrect API usage
// checked I/O exceptions describe failures callers must catch or declare

public class E07_CommonExceptions {
    public static void main(String[] args) throws IOException {
        // throwing a null reference throws NullPointerException instead of a custom failure
        try {
            throw null;
        } catch (NullPointerException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NullPointerException
        }

        // dereferencing null throws NullPointerException
        try {
            String value = null;
            value.length();
        } catch (NullPointerException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NullPointerException
        }

        // accessing an invalid list index throws IndexOutOfBoundsException
        try {
            List.of("Java").get(2);
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("IndexOutOfBoundsException");	// IndexOutOfBoundsException
        }

        // casting an object to an incompatible type throws ClassCastException
        try {
            Object value = "Java";
            Integer number = (Integer) value;
        } catch (ClassCastException exception) {
            System.out.println(exception.getClass().getSimpleName());	// ClassCastException
        }

        // reading a full primitive from insufficient bytes throws EOFException
        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(new byte[0]))) {
            input.readInt();
        } catch (EOFException exception) {
            System.out.println(exception.getClass().getSimpleName());	// EOFException
        }

        // reflective lookup of an unavailable class throws ClassNotFoundException
        try {
            Class.forName("missing.practice.Type");
        } catch (ClassNotFoundException exception) {
            System.out.println(exception.getClass().getSimpleName());	// ClassNotFoundException
        }

        // wait and notify require ownership of the object's monitor
        try {
            new Object().notify();
        } catch (IllegalMonitorStateException exception) {
            System.out.println(exception.getClass().getSimpleName());	// IllegalMonitorStateException
        }

        // a failed static initializer marks the class erroneous for this class loader
        try {
            new BrokenInitialization();
        } catch (ExceptionInInitializerError error) {
            System.out.println(error.getCause().getMessage());	// initialization failed
        }
        try {
            new BrokenInitialization();
        } catch (NoClassDefFoundError error) {
            System.out.println(error.getClass().getSimpleName());	// NoClassDefFoundError
        }
    }

    // these Error catches are isolated demonstrations rather than application recovery patterns
    static class BrokenInitialization {
        static final int VALUE = initialize();

        static int initialize() {
            throw new IllegalStateException("initialization failed");
        }
    }

    // array and String index failures are specialized IndexOutOfBoundsException types
    // ArithmeticException is demonstrated in E00 and NumberFormatException in E03 and E05
    // cursor exceptions are demonstrated in Collections/C04_Cursors
    // NoClassDefFoundError is a linkage failure and can also follow failed class initialization
    // ExceptionInInitializerError can wrap a non-Error failure from static initialization
}
