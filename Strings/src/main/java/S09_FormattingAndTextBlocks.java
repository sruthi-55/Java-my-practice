import java.util.Locale;

// text blocks are ordinary String values with multiline source syntax and incidental indentation removal
// formatting inserts values using conversion specifiers while formatted uses the default formatting locale
// %s formats text, %d formats an integer, %f formats floating-point values and %% emits a literal percent sign
// width pads to a minimum size, 0 selects numeric zero-padding and .precision controls decimal places for %f
// argument indexes such as %2$s select a supplied argument explicitly and %n uses the platform line separator

public class S09_FormattingAndTextBlocks {
    public static void main(String[] args) {
        // explicit locales keep numeric formatting reproducible
        System.out.println(String.format(Locale.ROOT, "%s %04d %.2f", "Java", 21, 3.5));	// Java 0021 3.50
        System.out.println("%s-%d".formatted("Java", 21));	// Java-21
        System.out.println(String.format(Locale.ROOT, "%2$s %1$s %%", "Java", "Hello"));	// Hello Java %

        // an own-line closing delimiter leaves a trailing newline in the text block
        String block = """
                Java
                  SQL
                """;
        System.out.println(block.replace("\n", "|"));	// Java|  SQL|
        System.out.println(block.lines().toList());	// [Java,   SQL]
        System.out.println("Java\r\nSQL\n".lines().count());	// 2

        // a line-continuation escape joins source lines without adding a newline
        String joined = """
                Java \
                SQL""";
        System.out.println(joined);	// Java SQL
        String trailingSpace = """
                Java\s""";
        System.out.println(trailingSpace.length());	// 5

        // indentation and escape processing are also available as explicit String operations
        System.out.println("Java\nSQL".indent(2).replace("\n", "|"));	//   Java|  SQL|
        System.out.println("  Java\n  SQL".stripIndent().replace("\n", "|"));	// Java|SQL
        System.out.println("Java\\nSQL".translateEscapes().lines().toList());	// [Java, SQL]
        System.out.println(" Java ".transform(String::strip).transform(String::length));	// 4
    }

    // text blocks do not interpolate variables and Java 21 string templates are preview features excluded here
    // %n uses the platform line separator while \n is a line feed
}
