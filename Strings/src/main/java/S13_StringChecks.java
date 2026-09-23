import java.nio.charset.StandardCharsets;
import java.util.Objects;

// explicit checks fail without requiring the -ea option and protect examples against regressions

public class S13_StringChecks {
    public static void main(String[] args) {
        // empty inputs and supplementary characters exercise common algorithm boundaries
        check("", S12_StringInterviewProblems.reverse(""));
        check("😀a", S12_StringInterviewProblems.reverse("a😀"));
        check(true, S12_StringInterviewProblems.isPalindrome(""));
        check(false, S12_StringInterviewProblems.isPalindrome("Java"));
        check(true, S12_StringInterviewProblems.areAnagrams("😀a", "a😀"));
        check(false, S12_StringInterviewProblems.areAnagrams("a", "aa"));
        check(-1, S12_StringInterviewProblems.firstUnique("aabb"));
        check(0x1F600, S12_StringInterviewProblems.firstUnique("a😀a"));
        check("", S12_StringInterviewProblems.removeDuplicates(""));
        check(0, S12_StringInterviewProblems.longestUniqueLength(""));
        check(2, S12_StringInterviewProblems.longestUniqueLength("abba"));
        check(true, S12_StringInterviewProblems.isRotation("", ""));
        check(false, S12_StringInterviewProblems.isRotation("abc", "acb"));

        // splitting, equality and byte conversion have different contracts from identity and display
        check(4, "a,b,,".split(",", -1).length);
        check(2, "a,b,,".split(",").length);
        check(true, "Java".equals(new String("Java")));
        check(false, "Java" == new String("Java"));
        check("é😀", new String("é😀".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));

        // algorithm inputs reject null instead of silently interpreting absence as empty text
        try {
            S12_StringInterviewProblems.reverse(null);
            throw new AssertionError("null input was accepted");
        } catch (NullPointerException expected) {
            check(null, expected.getMessage());
        }
        System.out.println("String checks passed");	// String checks passed
    }

    private static void check(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
