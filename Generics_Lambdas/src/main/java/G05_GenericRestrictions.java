import java.util.List;

// type erasure removes most generic type information after compilation
// generic restrictions prevent primitive type arguments, new T(), static T fields and generic-array creation

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
    }

    // generics provide compile-time type safety and accept reference types rather than primitives
    // generic exception classes cannot extend Throwable and erased method signatures cannot create overload ambiguity
    // raw Object loses compile-time type safety and requires casts, so parameterized types are preferred
}
