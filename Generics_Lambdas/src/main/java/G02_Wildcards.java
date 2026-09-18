import java.util.ArrayList;
import java.util.List;

// unbounded wildcard accepts an unknown reference type
// upper-bounded wildcard reads values as its bound while lower-bounded wildcard safely accepts values
// PECS means producer extends and consumer super

public class G02_Wildcards {
    public static void main(String[] args) {
        List<Integer> source = List.of(10, 20, 30);
        List<Number> destination = new ArrayList<>();
        copy(source, destination);
        System.out.println(sum(source));	// 60.0
        System.out.println(destination);	// [10, 20, 30]
        printSize(source);
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
