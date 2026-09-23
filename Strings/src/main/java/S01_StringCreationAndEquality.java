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

        // compile-time constant expressions are pooled while runtime concatenation creates a new result
        final String constant = "Ja";
        String variable = "Ja";
        System.out.println("Ja" + "va" == literalOne);	// true
        System.out.println(constant + "va" == literalOne);	// true
        System.out.println(variable + "va" == literalOne);	// false
        final String runtimeFinal = new String("Ja");
        System.out.println(runtimeFinal + "va" == literalOne);	// false

        // intern returns the canonical reference without changing the receiver's identity
        String canonical = heapString.intern();
        System.out.println(heapString == canonical);	// false
        System.out.println(canonical == literalOne);	// true

        // unchanged content may reuse the original object so methods do not always allocate
        System.out.println(literalOne.concat("") == literalOne);	// true
    }

    // immutability means methods never modify the receiver and may return it when no change is needed
    // new String("Java") creates one explicit object but literal creation depends on whether it is already pooled
    // avoid interning unbounded external input because canonicalization can add memory and processing costs
    // immutable values are safe to share but compound operations still require synchronization when shared state changes
    // reference variables may live in stack frames or inside heap objects depending on where they are declared
}
