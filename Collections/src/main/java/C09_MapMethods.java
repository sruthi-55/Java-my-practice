import java.util.HashMap;
import java.util.Map;

public class C09_MapMethods {
    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();

        System.out.println("put previous: " + scores.put("Java", 70));	// put previous: null

        scores.put("SQL", 80);
        scores.putAll(Map.of("Git", 75, "Spring", 85));

        System.out.println("putIfAbsent existing: " + scores.putIfAbsent("Java", 100));	// putIfAbsent existing: 70
        System.out.println("get: " + scores.get("Java"));	// get: 70
        System.out.println("getOrDefault: " + scores.getOrDefault("Docker", 0));	// getOrDefault: 0
        System.out.println("containsKey: " + scores.containsKey("SQL"));	// containsKey: true
        System.out.println("containsValue: " + scores.containsValue(85));	// containsValue: true

        scores.computeIfAbsent("Maven", key -> key.length() * 10);
        scores.computeIfPresent("Java", (key, value) -> value + 5);
        scores.compute("Git", (key, value) -> value == null ? 0 : value + 5);
        scores.merge("Java", 10, Integer::sum);
        scores.replace("SQL", 80, 90);
        scores.replaceAll((skill, score) -> Math.min(score, 100));

        System.out.println("after compute/merge: " + scores);	// five updated mappings in unspecified order

        scores.forEach((skill, score) -> System.out.println(skill + "=" + score));	// each mapping on its own line in unspecified order
        System.out.println("keys: " + scores.keySet());	// all five keys in unspecified order
        System.out.println("values: " + scores.values());	// all five values in key iteration order
        System.out.println("entries: " + scores.entrySet());	// all five entries in unspecified order
        // all the above 3 return a view of the map
        // so, any changes to them are reflected in the original map

        System.out.println("remove pair: " + scores.remove("Spring", 85));	// remove pair: true
        System.out.println("remove key: " + scores.remove("Maven"));	// remove key: 50
        System.out.println("size/empty: " + scores.size() + "/" + scores.isEmpty());	// size/empty: 3/false

        scores.clear();

        System.out.println("after clear: " + scores);	// after clear: {}
    }

    // put returns the previous value while putIfAbsent writes only when no non-null mapping exists
    // compute derives a mapping from key and old value while merge combines an old value with a supplied value
    // keySet, values and entrySet are backed collection views of the map
}
