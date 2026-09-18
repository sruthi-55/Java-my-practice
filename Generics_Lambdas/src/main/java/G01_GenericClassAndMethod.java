// generic class parameterizes stored values while generic method declares its own type parameter
// bounded type parameter restricts accepted types and exposes the bound's methods

public class G01_GenericClassAndMethod {
    public static void main(String[] args) {
        Box<String> box = new Box<>("Java");
        System.out.println(box.get());	// Java
        System.out.println(max(10, 20));	// 20
    }

    static <T extends Comparable<T>> T max(T first, T second) {
        return first.compareTo(second) >= 0 ? first : second;
    }
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
