import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

// type erasure removes most generic type information after compilation
// generic restrictions prevent primitive type arguments, new T(), static T fields and generic-array creation
// raw types bypass generic checks and can cause heap pollution that fails at a later compiler-inserted cast
// reifiable types such as List<?> can be checked at runtime while List<String> cannot be used in instanceof

public class G05_GenericRestrictions<T> {
    private final T value;

    G05_GenericRestrictions(T value) {
        this.value = value;
    }

    public static void main(String[] args) {
        G05_GenericRestrictions<Integer> holder = new G05_GenericRestrictions<>(10);
        System.out.println(holder.value);	// 10
        System.out.println(List.of(1, 2) instanceof List<?>);	// true

        // type erasure gives differently parameterized lists the same runtime class
        List<String> words = List.of("Java");
        List<Integer> numbers = List.of(21);
        System.out.println(words.getClass() == numbers.getClass());	// true

        // Object requires a cast while a parameterized type preserves the element type
        Object rawValue = "Java";
        String castValue = (String) rawValue;
        System.out.println(castValue);	// Java
        heapPollution();

        // arrays are covariant and enforce their component type at runtime unlike invariant generic lists
        Object[] array = new String[1];
        try {
            array[0] = 21;
        } catch (ArrayStoreException exception) {
            System.out.println(exception.getClass().getSimpleName());	// ArrayStoreException
        }

        // a compiler-generated bridge preserves polymorphic dispatch after a generic return type is erased
        Box<String> specialized = new StringBox();
        System.out.println(specialized.get());	// Java
        System.out.println(Arrays.stream(StringBox.class.getDeclaredMethods()).anyMatch(method -> method.isBridge()));	// true

        // safe generic varargs only read elements and never expose or corrupt the compiler-created array
        System.out.println(copyValues("Java", "SQL"));	// [Java, SQL]
    }

    static final class StringBox extends Box<String> {
        StringBox() { super("Java"); }
        @Override
        String get() { return super.get(); }
    }

    @SafeVarargs
    static <E> List<E> copyValues(E... values) {
        List<E> result = new ArrayList<>();
        for (E value : values) result.add(value);
        return result;
    }

    // raw access is deliberately isolated to demonstrate an unsafe legacy boundary
    @SuppressWarnings({"rawtypes", "unchecked"})
    static void heapPollution() {
        List<String> words = new ArrayList<>();
        List raw = words;
        raw.add(21);
        try {
            String word = words.get(0);
        } catch (ClassCastException exception) {
            System.out.println(exception.getClass().getSimpleName());	// ClassCastException
        }
    }

    // generics provide compile-time type safety and accept reference types rather than primitives
    // generic exception classes cannot extend Throwable and erased method signatures cannot create overload ambiguity
    // Object is a concrete type rather than a raw generic type and reading it as a subtype requires a cast
    // erasure replaces an unbounded parameter with Object and a bounded parameter with its leftmost bound
    // generic varargs can expose non-reifiable arrays and @SafeVarargs is a promise of safe implementation rather than a runtime guard
}
