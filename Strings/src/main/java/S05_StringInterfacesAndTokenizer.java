import java.io.Serializable;
import java.util.StringTokenizer;

// String implements CharSequence, Comparable<String> and Serializable
// CharSequence exposes character access while Comparable provides natural lexicographic ordering
// Serializable marks objects whose state can be converted to a byte stream
// String also implements Constable and ConstantDesc for JVM constant descriptions
// StringTokenizer is a legacy delimiter-based tokenizer and split is preferred for most new code

public class S05_StringInterfacesAndTokenizer {
    public static void main(String[] args) {
        String text = "Java,SQL,Git";
        CharSequence sequence = text;
        Comparable<String> comparable = text;
        Serializable serializable = text;

        System.out.println(sequence.charAt(0));	// J
        System.out.println(Integer.signum(comparable.compareTo("Kotlin")));	// -1
        System.out.println(serializable.getClass().getSimpleName());	// String

        StringTokenizer tokenizer = new StringTokenizer(text, ",");
        while (tokenizer.hasMoreTokens()) {
            System.out.println(tokenizer.nextToken());	// Java then SQL then Git
        }

        // tokenizer delimiters are individual characters and empty tokens are discarded
        StringTokenizer fields = new StringTokenizer("Java,,SQL;Git", ",;");
        System.out.println(fields.countTokens());	// 3
        System.out.println(fields.nextToken());	// Java
        System.out.println(fields.countTokens());	// 2
        StringTokenizer delimiters = new StringTokenizer("A,B", ",", true);
        System.out.println(delimiters.countTokens());	// 3

        // CharSequence can expose mutable text and does not define content-based equals
        CharSequence mutable = new StringBuilder("Java");
        System.out.println("Java".equals(mutable));	// false
        System.out.println("Java".contentEquals(mutable));	// true
        System.out.println("Java".describeConstable().orElseThrow());	// Java
    }
}
