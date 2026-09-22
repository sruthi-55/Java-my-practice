import java.util.ArrayList;
import java.util.List;

// immutable objects prevent state mutation and confinement gives mutable data a single owner
// safe publication exposes a fully initialized object through a happens-before mechanism

public class M12_ThreadSafeDesign {
    public static void main(String[] args) throws InterruptedException {
        List<String> original = new ArrayList<>(List.of("Java"));
        Profile profile = new Profile(original);
        original.add("SQL");
        Thread reader = new Thread(() -> System.out.println(profile.skills()));	// [Java]
        reader.start();
        reader.join();
        System.out.println(Holder.INSTANCE == Holder.INSTANCE);	// true
        System.out.println(Config.instance() == Config.instance());	// true
        System.out.println(square(4));	// 16
    }

    // the defensive copy makes this record immutable because its elements are also immutable
    record Profile(List<String> skills) {
        Profile {
            skills = List.copyOf(skills);
        }
    }

    static class Holder {
        static final Profile INSTANCE = new Profile(List.of("Java"));
    }

    // volatile is essential to correct double-checked locking and the holder idiom is usually simpler
    static class Config {
        private static volatile Config instance;
        private Config() { }

        static Config instance() {
            Config result = instance;
            if (result == null) {
                synchronized (Config.class) {
                    result = instance;
                    if (result == null) instance = result = new Config();
                }
            }
            return result;
        }
    }

    // stateless code with only local primitive values has no shared mutable state
    static int square(int value) {
        return value * value;
    }

    // properly constructed final fields have initialization guarantees but final references do not freeze objects
    // never publish this or start a thread using this before the constructor completes
    // thread-safe code supports concurrent calls while reentrant code also tolerates nested calls before completion
    // static initialization, locks, volatile references and concurrent collections support safe publication
}
