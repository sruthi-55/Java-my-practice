import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Scanner;

// System.in is the standard input stream and System.out is the standard output stream
// Scanner parses primitive values and strings while BufferedReader efficiently reads character streams
// Java has eight primitive types: boolean, char, byte, short, int, long, float and double

public class B02_SystemInputAndDataTypes {
    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner("10 2.5 Java interview\n")) {
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
    }

    // System is a final java.lang class and out is its static PrintStream field
    // identifiers name program elements while reserved keywords cannot be used as identifiers
}
