import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.TreeMap;

// Map keys are unique and get returning null can mean either absence or a mapped null value
// compute and merge returning null remove a mapping while computeIfAbsent returning null stores nothing
// Map.of and Map.copyOf reject null keys and values while Map.of also rejects duplicate keys

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
        mappingRules();
    }

    static void mappingRules() {
        // getOrDefault distinguishes an absent key from an existing null mapping
        Map<String, Integer> map = new HashMap<>();
        map.put("Java", null);
        System.out.println(map.getOrDefault("Java", 0));	// null
        System.out.println(map.containsKey("Java"));	// true
        map.computeIfAbsent("Java", key -> 21);
        map.merge("Java", 1, (oldValue, newValue) -> null);
        System.out.println(map.containsKey("Java"));	// false
        map.computeIfAbsent("SQL", key -> null);
        System.out.println(map.isEmpty());	// true

        // backed views support removal and entry value updates but not arbitrary insertion
        map.put("Java", 21);
        map.entrySet().iterator().next().setValue(22);
        System.out.println(map.get("Java"));	// 22
        map.keySet().remove("Java");
        System.out.println(map.isEmpty());	// true
        try {
            Map.of("Java", 1, "Java", 2);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getClass().getSimpleName());	// IllegalArgumentException
        }

        // access-order LinkedHashMap moves accessed entries to the end and supports LRU-style designs
        LinkedHashMap<String, Integer> access = new LinkedHashMap<>(16, 0.75f, true);
        access.put("Java", 21);
        access.put("SQL", 1);
        access.get("Java");
        System.out.println(access.keySet());	// [SQL, Java]
        System.out.println(access.firstEntry());	// SQL=1
        System.out.println(access.reversed().keySet());	// [Java, SQL]
        System.out.println(access.pollFirstEntry());	// SQL=1

        // navigable maps provide nearest-key queries and backed range views
        TreeMap<Integer, String> sorted = new TreeMap<>(Map.of(10, "A", 20, "B", 30, "C"));
        System.out.println(sorted.lowerKey(20) + " " + sorted.floorKey(20));	// 10 20
        System.out.println(sorted.ceilingKey(21) + " " + sorted.higherKey(20));	// 30 30
        sorted.subMap(10, true, 30, false).clear();
        System.out.println(sorted);	// {30=C}
    }

    // put returns the previous value while putIfAbsent writes only when no non-null mapping exists
    // compute derives a mapping from key and old value while merge combines an old value with a supplied value
    // keySet, values and entrySet are backed collection views of the map
}
