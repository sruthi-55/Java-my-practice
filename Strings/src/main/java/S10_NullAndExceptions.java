import java.util.Arrays;
import java.util.IllegalFormatConversionException;
import java.util.Objects;

// null means no String reference while empty and blank describe existing String values
// validation should distinguish missing values from values whose contents violate a rule

public class S10_NullAndExceptions {
    public static void main(String[] args) {
        // constant-receiver equality and Objects.equals avoid dereferencing a nullable value
        String missing = null;
        System.out.println("Java".equals(missing));	// false
        System.out.println(Objects.equals(missing, null));	// true
        System.out.println(Objects.toString(missing, "fallback"));	// fallback
        System.out.println(missing == null || missing.isBlank());	// true

        // concatenation and Object conversion render null as text rather than an empty string
        System.out.println("value=" + missing);	// value=null
        System.out.println(String.valueOf((Object) null));	// null
        System.out.println(String.join(",", "Java", null));	// Java,null

        // the char array overload wins for a bare null argument and rejects it
        try {
            String.valueOf(null);
        } catch (NullPointerException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NullPointerException
        }

        // concat requires a non-null argument unlike the + operator
        try {
            "Java".concat(missing);
        } catch (NullPointerException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NullPointerException
        }

        // indexes must lie within the receiver and a substring end is exclusive
        try {
            "Java".charAt(4);
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("index outside text");	// index outside text
        }

        // parsing requires valid numeric syntax and a value within the target type's range
        System.out.println(Integer.parseInt(" 42 ".strip()));	// 42
        System.out.println(Integer.parseInt("ff", 16));	// 255
        try {
            Integer.parseInt("2147483648");
        } catch (NumberFormatException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NumberFormatException
        }

        // format specifiers must match the supplied argument types
        try {
            String.format("%d", "42");
        } catch (IllegalFormatConversionException exception) {
            System.out.println(exception.getClass().getSimpleName());	// IllegalFormatConversionException
        }

        // repetition counts cannot be negative
        try {
            "Java".repeat(-1);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getClass().getSimpleName());	// IllegalArgumentException
        }

        // erasable character arrays reduce secret lifetime but cannot erase copies held elsewhere
        char[] secret = {'p', 'a', 's', 's'};
        Arrays.fill(secret, '\0');
        System.out.println((int) secret[0]);	// 0
    }

    // immutable String secrets cannot be wiped in place and should not be logged or unnecessarily copied
    // avoid catching broad exceptions when a specific validation or conversion failure is expected
}
