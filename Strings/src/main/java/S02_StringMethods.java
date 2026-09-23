import java.util.Arrays;
import java.util.Locale;

// String methods never modify the original String because String is immutable
// indexes are zero-based and substring end indexes are exclusive

public class S02_StringMethods {
    public static void main(String[] args) {
        String value = "  Hello Sruthi  ";

        System.out.println(value.length());	// 16
        System.out.println(value.charAt(2));	// H
        System.out.println("[" + value.substring(8) + "]");	// [Sruthi  ]
        System.out.println(value.substring(2, 7));	// Hello
        System.out.println(value.concat(" Mora"));	//   Hello Sruthi   Mora

        System.out.println(value.indexOf('o'));	// 6
        System.out.println(value.indexOf('x'));	// -1
        System.out.println(value.indexOf("Sruthi"));	// 8
        System.out.println(value.lastIndexOf('i'));	// 13

        System.out.println(value.equals("  Hello Sruthi  "));	// true
        System.out.println(value.equalsIgnoreCase("  HELLO SRUTHI  "));	// true
        System.out.println("Java".compareTo("Java"));	// 0
        System.out.println(Integer.signum("Java".compareTo("Kotlin")));	// -1
        System.out.println("Java".compareToIgnoreCase("JAVA"));	// 0

        System.out.println("[" + value.toLowerCase(Locale.ROOT) + "]");	// [  hello sruthi  ]
        System.out.println("[" + value.toUpperCase(Locale.ROOT) + "]");	// [  HELLO SRUTHI  ]
        System.out.println(value.trim());	// Hello Sruthi
        System.out.println(value.strip());	// Hello Sruthi
        System.out.println("[" + value.replace("Hello", "Hi") + "]");	// [  Hi Sruthi  ]
        System.out.println(value.contains("Sruthi"));	// true
        System.out.println(value.startsWith("  He"));	// true
        System.out.println(value.endsWith("  "));	// true
        System.out.println(value.isBlank());	// false
        System.out.println("".isEmpty());	// true

        System.out.println(Arrays.toString("Java,SQL,Git".split(",")));	// [Java, SQL, Git]
        System.out.println(String.join("-", "Java", "SQL", "Git"));	// Java-SQL-Git
        System.out.println(String.valueOf(42));	// 42
        System.out.println(Arrays.toString("Java".toCharArray()));	// [J, a, v, a]

        // region and offset searches avoid allocating substrings for comparisons
        System.out.println("banana".indexOf("an", 2));	// 3
        System.out.println("banana".lastIndexOf("an", 2));	// 1
        System.out.println("Java Developer".startsWith("Developer", 5));	// true
        System.out.println("Java".regionMatches(true, 0, "JAVA!", 0, 4));	// true
        System.out.println("Java".subSequence(1, 3));	// av
        System.out.println("[" + "Java".substring(4) + "]");	// []
        char[] destination = new char[2];
        "Java".getChars(1, 3, destination, 0);
        System.out.println(Arrays.toString(destination));	// [a, v]

        // trim removes characters up to U+0020 while strip follows Character.isWhitespace
        String unicodeSpace = "\u2003Java\u2003";
        System.out.println(unicodeSpace.trim().length());	// 6
        System.out.println(unicodeSpace.strip());	// Java
        System.out.println("  Java  ".stripLeading().length());	// 6
        System.out.println("  Java  ".stripTrailing().length());	// 6
        System.out.println(" \t".isEmpty());	// false
        System.out.println(" \t".isBlank());	// true
        System.out.println("\u00A0".isBlank());	// false
    }

    // charAt throws StringIndexOutOfBoundsException when the index is outside the String
    // compareTo returns a negative value, zero or a positive value using lexicographic ordering
}
