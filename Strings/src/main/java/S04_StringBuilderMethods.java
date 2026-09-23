// StringBuilder is a mutable unsynchronized character sequence suited to single-threaded mutation
// StringBuilder normally performs better than StringBuffer when synchronization is unnecessary

public class S04_StringBuilderMethods {
    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder("Java");
        builder.append(" Interview").insert(4, " Developer");
        System.out.println(builder);	// Java Developer Interview
        builder.replace(5, 14, "Backend");
        System.out.println(builder);	// Java Backend Interview
        builder.delete(4, 12);
        System.out.println(builder);	// Java Interview
        System.out.println(builder.capacity());	// 42
        System.out.println(builder.reverse());	// weivretnI avaJ

        // toString creates a content snapshot unaffected by later builder changes
        StringBuilder text = new StringBuilder("Java");
        String snapshot = text.toString();
        text.setCharAt(0, 'L');
        System.out.println(snapshot);	// Java
        System.out.println(text);	// Lava

        // length counts current code units while capacity is available storage before growth
        text.setLength(2);
        System.out.println(text);	// La
        text.setLength(4);
        System.out.println((int) text.charAt(3));	// 0
        text.setLength(0);
        text.appendCodePoint(0x1F600).append(" Java");
        System.out.println(text);	// 😀 Java
        System.out.println(text.indexOf("Java"));	// 3
        System.out.println(text.lastIndexOf("a"));	// 6
        text.deleteCharAt(2);
        System.out.println(text.substring(2));	// Java

        // builder equality uses identity even though compareTo compares characters
        StringBuilder other = new StringBuilder(text);
        System.out.println(text.equals(other));	// false
        System.out.println(text.compareTo(other));	// 0
        System.out.println(text.toString().contentEquals(other));	// true

        // reverse preserves valid surrogate pairs but does not preserve every grapheme cluster
        System.out.println(new StringBuilder("A😀B").reverse());	// B😀A
    }

    // StringBuilder supports the same main mutation methods as StringBuffer without synchronized method overhead
}
