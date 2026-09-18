import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.UnaryOperator;

// primitive functional interfaces avoid boxing while BiFunction accepts two inputs
// UnaryOperator and BinaryOperator keep input and output types identical

public class G06_FunctionVariants {
    public static void main(String[] args) {
        IntConsumer consumer = System.out::println;
        IntPredicate predicate = number -> number > 0;
        IntSupplier supplier = () -> 21;
        BiFunction<Integer, Integer, String> biFunction = (a, b) -> String.valueOf(a + b);
        UnaryOperator<Integer> square = number -> number * number;
        BinaryOperator<Integer> add = Integer::sum;

        consumer.accept(supplier.getAsInt());	// 21
        System.out.println(predicate.test(1));	// true
        System.out.println(biFunction.apply(20, 22));	// 42
        System.out.println(square.apply(4));	// 16
        System.out.println(add.apply(2, 3));	// 5
    }

    // consumer accepts input without a result, Predicate tests input, Function transforms input and Supplier creates output
    // BiFunction accepts two inputs while unary and binary operators keep input and output types identical
    // primitive specializations such as IntConsumer avoid boxing and method references shorten compatible lambdas
}
