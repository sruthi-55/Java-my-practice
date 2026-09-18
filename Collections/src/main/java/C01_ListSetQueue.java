import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class C01_ListSetQueue {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(List.of("Java", "SQL", "Java"));
        Set<String> set = new HashSet<>(list);
        Queue<String> queue = new ArrayDeque<>(list);

        System.out.println(list.get(1));	// SQL
        System.out.println(set);	// [Java, SQL] in unspecified order
        System.out.println(queue.poll());	// Java
    }
}
