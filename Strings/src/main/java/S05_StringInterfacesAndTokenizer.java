import java.io.Serializable;
import java.util.StringTokenizer;

// String implements CharSequence, Comparable<String> and Serializable
// CharSequence exposes character access while Comparable provides natural lexicographic ordering
// Serializable marks objects whose state can be converted to a byte stream
// StringTokenizer is a legacy delimiter-based tokenizer and split is preferred for most new code

public class S05_StringInterfacesAndTokenizer {
    public static void main(String[] args) {
        String text = "Java,SQL,Spring";
        CharSequence sequence = text;
        Comparable<String> comparable = text;
        Serializable serializable = text;

        System.out.println(sequence.charAt(0));	// J
        System.out.println(Integer.signum(comparable.compareTo("Kotlin")));	// -1
        System.out.println(serializable.getClass().getSimpleName());	// String

        StringTokenizer tokenizer = new StringTokenizer(text, ",");
        while (tokenizer.hasMoreTokens()) {
            System.out.println(tokenizer.nextToken());	// Java then SQL then Spring
        }
    }
}
