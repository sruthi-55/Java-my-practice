import java.util.Arrays;

// String methods never modify the original String because String is immutable
// indexes are zero-based and substring end indexes are exclusive

public class S02_StringMethods {
    public static void main(String[] args) {
        String value = "  Hello Sruthi  ";

        System.out.println(value.length());	// 16
        System.out.println(value.charAt(2));	// H
        System.out.println(value.substring(8));	// Sruthi  
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

        System.out.println(value.toLowerCase());	//   hello sruthi  
        System.out.println(value.toUpperCase());	//   HELLO SRUTHI  
        System.out.println(value.trim());	// Hello Sruthi
        System.out.println(value.strip());	// Hello Sruthi
        System.out.println(value.replace("Hello", "Hi"));	//   Hi Sruthi  
        System.out.println(value.contains("Sruthi"));	// true
        System.out.println(value.startsWith("  He"));	// true
        System.out.println(value.endsWith("  "));	// true
        System.out.println(value.isBlank());	// false
        System.out.println("".isEmpty());	// true

        System.out.println(Arrays.toString("Java,SQL,Git".split(",")));	// [Java, SQL, Git]
        System.out.println(String.join("-", "Java", "SQL", "Git"));	// Java-SQL-Git
        System.out.println(String.valueOf(42));	// 42
        System.out.println(Arrays.toString("Java".toCharArray()));	// [J, a, v, a]
    }

    // charAt throws StringIndexOutOfBoundsException when the index is outside the String
    // compareTo returns a negative value, zero or a positive value using lexicographic ordering
}
