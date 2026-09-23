import java.util.ArrayList;
import java.util.Arrays;

// getClass returns the runtime Class object and cannot be overridden
// toString provides diagnostic text while identityHashCode bypasses an overridden hashCode

public class OB01_ObjectMethods {
    public static void main(String[] args) {
        // class literals and getClass describe the same loaded runtime type
        Object text = "Java";
        System.out.println(text.getClass() == String.class);	// true
        System.out.println(text.getClass().getName());	// java.lang.String
        System.out.println(text.getClass().getSuperclass() == Object.class);	// true
        System.out.println(String.class.isInstance(text));	// true
        System.out.println(Object.class.isAssignableFrom(String.class));	// true
        System.out.println(new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass());	// true

        // default toString uses the class name and overridden hashCode rather than a memory address
        FixedHash item = new FixedHash();
        System.out.println(item);	// OB01_ObjectMethods$FixedHash@2a
        System.out.println(item.hashCode());	// 42
        System.out.println(System.identityHashCode(item) == System.identityHashCode(item));	// true

        // custom diagnostic output should expose useful state without secrets
        System.out.println(new Label("Java"));	// Label[Java]
        System.out.println(Arrays.toString(new int[]{1, 2}));	// [1, 2]
        System.out.println(Arrays.deepToString(new int[][]{{1}, {2}}));	// [[1], [2]]
    }

    static final class FixedHash {
        @Override
        public int hashCode() {
            return 42;
        }
    }

    record Label(String value) {
        @Override
        public String toString() {
            return "Label[" + value + "]";
        }
    }

    // identity hashes can collide and are neither unique IDs nor stable values across JVM executions
    // arrays inherit identity-based toString so Arrays helpers are needed for readable contents
    // a loaded class is identified by its defining class loader and name rather than its name alone
    // generic type arguments are erased so getClass cannot distinguish ArrayList<String> from ArrayList<Integer>
}
