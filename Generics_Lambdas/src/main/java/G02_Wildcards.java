import java.util.ArrayList;
import java.util.List;

// unbounded wildcard accepts an unknown reference type
// upper-bounded wildcard reads values as its bound while lower-bounded wildcard safely accepts values
// PECS means producer extends and consumer super
// generics are invariant so List<Integer> is not a subtype of List<Number>
// extends safely reads the bound but cannot add non-null values while super accepts the bound and reads Object

public class G02_Wildcards {
    public static void main(String[] args) {
        List<Integer> source = List.of(10, 20, 30);
        List<Number> destination = new ArrayList<>();
        copy(source, destination);
        System.out.println(sum(source));	// 60.0
        System.out.println(destination);	// [10, 20, 30]
        printSize(source);

        // wildcard capture names an unknown element type inside a generic helper
        List<String> words = new ArrayList<>(List.of("Java", "SQL"));
        swapFirstTwo(words);
        System.out.println(words);	// [SQL, Java]

        // a producer can still be structurally mutable because extends is not an immutability promise
        List<? extends Number> producer = new ArrayList<>(source);
        producer.clear();
        System.out.println(producer.isEmpty());	// true
        List<? super Integer> consumer = new ArrayList<Number>();
        consumer.add(42);
        Object value = consumer.get(0);
        System.out.println(value);	// 42
    }

    static void swapFirstTwo(List<?> values) { swapCaptured(values); }

    private static <T> void swapCaptured(List<T> values) {
        T first = values.get(0);
        values.set(0, values.get(1));
        values.set(1, first);
    }

    static double sum(List<? extends Number> numbers) {
        return numbers.stream().mapToDouble(Number::doubleValue).sum();
    }

    static <T> void copy(List<? extends T> source, List<? super T> destination) {
        destination.addAll(source);
    }

    static void printSize(List<?> values) {
        System.out.println(values.size());	// 3
    }
}
