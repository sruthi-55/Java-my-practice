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
        System.out.println(builder.capacity());	// at least 20
        System.out.println(builder.reverse());	// weivretnI avaJ
    }

    // StringBuilder supports the same main mutation methods as StringBuffer without synchronized method overhead
}
