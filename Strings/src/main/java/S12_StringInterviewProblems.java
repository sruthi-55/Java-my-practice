import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

// code-point-based algorithms handle supplementary characters without splitting surrogate pairs
// interview solutions should state their null policy and whether case, spaces and normalization matter

public class S12_StringInterviewProblems {
    public static void main(String[] args) {
        // reversal preserves code points but not every multi-code-point grapheme cluster
        System.out.println(reverse("A😀B"));	// B😀A

        // palindrome comparison ignores non-alphanumeric code points and uses root-locale lowercase
        System.out.println(isPalindrome("A man, a plan, a canal: Panama"));	// true

        // anagram comparison here is case-sensitive and includes spaces without Unicode normalization
        System.out.println(areAnagrams("listen", "silent"));	// true
        System.out.println(areAnagrams("aab", "abb"));	// false

        // encounter-order counts identify the first unique code point
        int unique = firstUnique("swiss");
        System.out.println(new String(Character.toChars(unique)));	// w

        // distinct preserves encounter order while removing repeated code points
        System.out.println(removeDuplicates("banana😀😀"));	// ban😀

        // a sliding window advances past the last duplicate rather than restarting each substring
        System.out.println(longestUniqueLength("abcabcbb"));	// 3
        System.out.println(longestUniqueLength("😀a😀b"));	// 3

        // substring search over a doubled value identifies rotations of equal-length strings
        System.out.println(isRotation("abcd", "cdab"));	// true
    }

    // reversing n code points takes O(n) time and O(n) additional space
    static String reverse(String text) {
        int[] points = Objects.requireNonNull(text).codePoints().toArray();
        StringBuilder result = new StringBuilder(text.length());
        for (int i = points.length - 1; i >= 0; i--) {
            result.appendCodePoint(points[i]);
        }
        return result.toString();
    }

    // two pointers compare the filtered text in O(n) time with O(n) preprocessing space
    static boolean isPalindrome(String text) {
        int[] points = Objects.requireNonNull(text).toLowerCase(Locale.ROOT).codePoints()
                .filter(Character::isLetterOrDigit).toArray();
        for (int left = 0, right = points.length - 1; left < right; left++, right--) {
            if (points[left] != points[right]) {
                return false;
            }
        }
        return true;
    }

    // frequency maps give expected O(n + m) time and O(k) space for k distinct code points
    static boolean areAnagrams(String first, String second) {
        return frequencies(first).equals(frequencies(second));
    }

    static Map<Integer, Integer> frequencies(String text) {
        Map<Integer, Integer> counts = new LinkedHashMap<>();
        Objects.requireNonNull(text).codePoints().forEach(point -> counts.merge(point, 1, Integer::sum));
        return counts;
    }

    // -1 represents absence because it is not a valid Unicode code point
    static int firstUnique(String text) {
        return frequencies(text).entrySet().stream().filter(entry -> entry.getValue() == 1)
                .mapToInt(Map.Entry::getKey).findFirst().orElse(-1);
    }

    static String removeDuplicates(String text) {
        StringBuilder result = new StringBuilder();
        Objects.requireNonNull(text).codePoints().distinct().forEachOrdered(result::appendCodePoint);
        return result.toString();
    }

    // last-seen positions give expected O(n) time with O(n) code-point array and map space
    static int longestUniqueLength(String text) {
        int[] points = Objects.requireNonNull(text).codePoints().toArray();
        Map<Integer, Integer> lastSeen = new HashMap<>();
        int left = 0;
        int longest = 0;
        for (int right = 0; right < points.length; right++) {
            Integer previous = lastSeen.put(points[right], right);
            if (previous != null) {
                left = Math.max(left, previous + 1);
            }
            longest = Math.max(longest, right - left + 1);
        }
        return longest;
    }

    // rotation uses UTF-16 substring semantics and allocates O(n) space for the doubled text
    static boolean isRotation(String first, String second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        return first.length() == second.length() && (first + first).contains(second);
    }
}
