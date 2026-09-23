// generic class parameterizes stored values while generic method declares its own type parameter
// bounded type parameter restricts accepted types and exposes the bound's methods

public class G01_GenericClassAndMethod {
    public static void main(String[] args) {
        Box<String> box = new Box<>("Java");
        System.out.println(box.get());	// Java
        System.out.println(max(10, 20));	// 20

        // a type satisfying both bounds exposes numeric conversion and comparison behavior
        System.out.println(largerAsDouble(10, 21));	// 21.0
    }

    static <T extends Number & Comparable<? super T>> double largerAsDouble(T first, T second) {
        return (first.compareTo(second) >= 0 ? first : second).doubleValue();
    }

    // a super bound also accepts types that inherit comparison against a parent type
    static <T extends Comparable<? super T>> T max(T first, T second) {
        return first.compareTo(second) >= 0 ? first : second;
    }

    // multiple bounds use & with a class bound first and any interface bounds after it
    // static generic methods declare their own type parameters independently of the containing class
}

class Box<T> {
    private final T value;

    Box(T value) {
        this.value = value;
    }

    T get() {
        return value;
    }
}
