import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Scanner;
import java.util.Locale;
import java.util.Arrays;
import java.math.BigDecimal;

// System.in is the standard input stream and System.out is the standard output stream
// Scanner parses primitive values and strings while BufferedReader efficiently reads character streams
// Java has eight primitive types: boolean, char, byte, short, int, long, float and double
// byte, short, int and long are signed 8, 16, 32 and 64-bit integers while char is an unsigned 16-bit UTF-16 unit
// float and double use binary floating-point and boolean has only true and false without numeric conversion
// arithmetic promotes byte, short and char to int and narrowing conversions may discard information

public class B02_SystemInputAndDataTypes {
    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner("10 2.5 Java interview\n")) {
            scanner.useLocale(Locale.ROOT);
            int count = scanner.nextInt();
            double score = scanner.nextDouble();
            String word = scanner.next();
            String rest = scanner.nextLine().trim();
            System.out.println(count + " " + score + " " + word + " " + rest);	// 10 2.5 Java interview
        }

        try (BufferedReader reader = new BufferedReader(new StringReader("buffered input"))) {
            System.out.println(reader.readLine());	// buffered input
        }

        boolean flag = true;
        char letter = 'J';
        byte small = 1;
        short medium = 2;
        int number = 3;
        long large = 4L;
        float decimal = 5.0F;
        double precise = 6.0;
        System.out.println(flag + " " + letter + " " + small + " " + medium + " " + number + " " + large + " " + decimal + " " + precise);	// true J 1 2 3 4 5.0 6.0
        numericRules();
        arrayRules();
    }

    static void numericRules() {
        // integral division truncates toward zero while a floating operand changes the arithmetic
        System.out.println(5 / 2);	// 2
        System.out.println(5 / 2.0);	// 2.5
        System.out.println((byte) 130);	// -126
        byte value = 127;
        value += 1;
        System.out.println(value);	// -128
        // value = value + 1 needs an explicit cast because ordinary addition produces int

        // integer overflow wraps while exact arithmetic methods detect it
        System.out.println(Integer.MAX_VALUE + 1);	// -2147483648
        try {
            Math.addExact(Integer.MAX_VALUE, 1);
        } catch (ArithmeticException exception) {
            System.out.println(exception.getClass().getSimpleName());	// ArithmeticException
        }

        // binary decimals may round and floating-point division by zero does not throw ArithmeticException
        System.out.println(0.1 + 0.2 == 0.3);	// false
        System.out.println(new BigDecimal("0.1").add(new BigDecimal("0.2")));	// 0.3
        System.out.println(1.0 / 0.0);	// Infinity
        System.out.println(Double.isNaN(0.0 / 0.0));	// true

        // short-circuit boolean operators skip the right operand when its result is unnecessary
        int[] calls = {0};
        boolean skipped = false && ++calls[0] > 0;
        System.out.println(calls[0]);	// 0
        boolean evaluated = false & ++calls[0] > 0;
        System.out.println(calls[0]);	// 1
        System.out.println(-8 >> 1);	// -4
        System.out.println(-8 >>> 1);	// 2147483644
        System.out.println(1 << 32);	// 1
        // int shift distances use their low five bits while long shift distances use their low six bits
    }

    static void arrayRules() {
        // arrays have fixed length and copyOf creates separate storage with default-filled extra positions
        int[] original = {1, 2};
        int[] copy = Arrays.copyOf(original, 3);
        copy[0] = 9;
        System.out.println(Arrays.toString(original));	// [1, 2]
        System.out.println(Arrays.toString(copy));	// [9, 2, 0]
        int[][] jagged = {{1}, {2, 3}};
        System.out.println(jagged[1].length);	// 2

        // var infers a fixed local type from an initializer and does not enable dynamic typing
        var text = "Java";
        System.out.println(text.length());	// 4
        // var cannot replace field or parameter types and cannot infer a type from null alone
    }

    // System is a final java.lang class and out is its static PrintStream field
    // identifiers name program elements while reserved keywords cannot be used as identifiers
}
