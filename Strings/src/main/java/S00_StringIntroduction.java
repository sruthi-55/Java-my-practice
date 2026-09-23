// String is a final immutable class representing text as a sequence of UTF-16 code units
// immutability prevents content changes while final on a variable prevents reference reassignment
// StringBuilder is mutable without synchronization and StringBuffer synchronizes its individual operations
// CharSequence is a common interface for reading text and does not promise immutability

public class S00_StringIntroduction {
    public static void main(String[] args) {
        // assigning a new value changes one reference without modifying the shared original
        String original = "Java";
        String alias = original;
        original += " 21";
        System.out.println(original);	// Java 21
        System.out.println(alias);	// Java

        // final references can still refer to mutable objects
        final StringBuilder builder = new StringBuilder("Java");
        builder.append(" 21");
        System.out.println(builder);	// Java 21

        // strings copy input characters and return independent character arrays
        char[] input = {'J', 'a', 'v', 'a'};
        String copied = new String(input);
        input[0] = 'L';
        char[] output = copied.toCharArray();
        output[0] = 'K';
        System.out.println(copied);	// Java

        // Java passes references by value so reassigning a parameter cannot reassign its caller's variable
        reassign(alias);
        System.out.println(alias);	// Java
    }

    private static void reassign(String text) {
        text = "Kotlin";
    }

    // immutability enables stable hash keys and safe content sharing but shared variable updates need coordination
    // final on String prevents subclassing but final alone does not make an arbitrary class immutable
    // compact strings in modern OpenJDK use byte storage with an encoding flag without changing UTF-16 API semantics
    // pool placement and backing storage are JVM implementation details rather than application logic contracts
}
