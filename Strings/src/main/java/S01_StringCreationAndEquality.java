import java.nio.charset.StandardCharsets;

// String is an immutable sequence of UTF-16 code units
// string literals are reused from the heap-based string pool while new String creates a distinct object
// == compares primitive values or object identities while equals compares String contents
// intern returns the pooled reference for an equal String value

public class S01_StringCreationAndEquality {
    public static void main(String[] args) {
        String literalOne = "Java";
        String literalTwo = "Java";
        String heapString = new String("Java");

        System.out.println(literalOne == literalTwo);	// true
        System.out.println(literalOne == heapString);	// false
        System.out.println(literalOne.equals(heapString));	// true
        System.out.println(literalOne == heapString.intern());	// true

        System.out.println(new String(new char[]{'J', 'a', 'v', 'a'}));	// Java
        System.out.println(new String("Java".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));	// Java
        System.out.println(new String(new StringBuilder("Java")));	// Java
        System.out.println(new String(new StringBuffer("Java")));	// Java

        String unchanged = literalOne.concat(" 21");
        System.out.println(literalOne);	// Java
        System.out.println(unchanged);	// Java 21
    }

    // immutability means each apparent modification returns a new String instead of changing the original
    // immutable values are safe to share but compound operations still require synchronization when shared state changes
    // reference variables may live in stack frames or inside heap objects depending on where they are declared
}
