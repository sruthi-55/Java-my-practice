import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

// concatenation combines values into text and + is evaluated from left to right
// StringJoiner builds delimiter-separated text with optional prefixes and suffixes

public class S06_ConcatenationAndJoining {
    public static void main(String[] args) {
        // numeric addition continues until a String operand makes the operation concatenation
        System.out.println(1 + 2 + "Java");	// 3Java
        System.out.println("Java" + 1 + 2);	// Java12
        System.out.println("Java" + (1 + 2));	// Java3
        System.out.println('A' + 'B');	// 131
        System.out.println("" + 'A' + 'B');	// AB

        // repeated immutable accumulation copies growing prefixes and can take quadratic total work
        String repeated = "";
        StringBuilder builder = new StringBuilder(6);
        for (String part : List.of("a", "bb", "ccc")) {
            repeated += part;
            builder.append(part);
        }
        System.out.println(repeated);	// abbccc
        System.out.println(builder);	// abbccc

        // joiners avoid special-case delimiter removal at the end of loops
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        joiner.setEmptyValue("none");
        System.out.println(joiner);	// none
        joiner.add("Java").add("SQL");
        joiner.merge(new StringJoiner(", ").add("Git"));
        System.out.println(joiner);	// [Java, SQL, Git]
        System.out.println(List.of("Java", "SQL").stream().collect(Collectors.joining(" | ")));	// Java | SQL
        System.out.println("ab".repeat(3));	// ababab
    }

    // fixed + expressions are readable and the compiler may optimize them without using StringBuilder
    // pre-sizing a builder reduces resizing but excessive capacity wastes memory
    // use proper benchmarks rather than one-shot timings to compare text-building performance
}
