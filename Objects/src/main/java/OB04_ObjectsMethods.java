import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

// Objects provides null-safe equality, hashing, text conversion, validation and comparison helpers
// deepEquals compares array contents recursively while equals delegates to ordinary object equality

public class OB04_ObjectsMethods {
    public static void main(String[] args) {
        // null-safe helpers avoid dereferencing a missing receiver
        System.out.println(Objects.equals(null, null));	// true
        System.out.println(Objects.equals("Java", null));	// false
        System.out.println(Objects.isNull(null));	// true
        System.out.println(Objects.nonNull("Java"));	// true
        System.out.println(Objects.toString(null));	// null
        System.out.println(Objects.toString(null, "missing"));	// missing
        System.out.println(Arrays.asList("Java", null, "SQL").stream().filter(Objects::nonNull).toList());	// [Java, SQL]

        // array equality is identity-based unless an array-aware helper is used
        int[] first = {1, 2};
        int[] second = {1, 2};
        System.out.println(Objects.equals(first, second));	// false
        System.out.println(Objects.deepEquals(first, second));	// true
        System.out.println(Arrays.deepEquals(new Object[]{first}, new Object[]{second}));	// true
        System.out.println(Arrays.hashCode(first) == Arrays.hashCode(second));	// true

        // record equality still uses array identity rather than recursively comparing array components
        record Payload(int[] values) {}
        System.out.println(new Payload(first).equals(new Payload(second)));	// false

        // deepEquals recurses through arrays but does not inspect arbitrary object fields or list contents recursively
        System.out.println(Objects.deepEquals(java.util.List.of(first), java.util.List.of(second)));	// false

        // identity text bypasses overridden toString and hashCode methods
        Object item = new OB01_ObjectMethods.FixedHash();
        String identity = item.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(item));
        System.out.println(Objects.toIdentityString(item).equals(identity));	// true

        // hash treats arguments as a sequence while hashCode hashes a single nullable object
        System.out.println(Objects.hashCode(null));	// 0
        System.out.println(Objects.hashCode("A"));	// 65
        System.out.println(Objects.hash("A"));	// 96
        System.out.println(Objects.hash("A", 1));	// 2977
        System.out.println(Objects.hash(first) == 31 + first.hashCode());	// true

        // requireNonNull returns the original reference or throws with the supplied message
        String text = "Java";
        System.out.println(Objects.requireNonNull(text) == text);	// true
        try {
            Objects.requireNonNull(null, () -> "name required");
        } catch (NullPointerException exception) {
            System.out.println(exception.getMessage());	// name required
        }

        // ElseGet computes a fallback only when needed and both fallback forms require a non-null result
        System.out.println(Objects.requireNonNullElse(null, "Java"));	// Java
        int[] calls = {0};
        System.out.println(Objects.requireNonNullElseGet(text, () -> { calls[0]++; return "SQL"; }));	// Java
        System.out.println(calls[0]);	// 0
        System.out.println(Objects.requireNonNullElseGet(null, () -> { calls[0]++; return "SQL"; }));	// SQL
        System.out.println(calls[0]);	// 1
        try {
            Objects.requireNonNullElse(null, null);
        } catch (NullPointerException exception) {
            System.out.println("fallback must be non-null");	// fallback must be non-null
        }

        // compare returns zero for identical references and otherwise delegates to the comparator
        System.out.println(Objects.compare("a", "b", Comparator.naturalOrder()));	// -1
        System.out.println(Objects.compare(null, "b", Comparator.nullsFirst(Comparator.<String>naturalOrder())));	// -1

        // range validators return the validated index or start and reject invalid bounds
        System.out.println(Objects.checkIndex(2, 3));	// 2
        System.out.println(Objects.checkFromToIndex(1, 3, 3));	// 1
        System.out.println(Objects.checkFromIndexSize(1, 2, 3));	// 1
        try {
            Objects.checkIndex(3, 3);
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("index must be less than length");	// index must be less than length
        }
    }

    // Objects.hash on a primitive array does not hash its elements so use Arrays.hashCode instead
    // use Arrays.deepHashCode with recursive array equality to keep the equality and hashing contracts aligned
}
