// static member belongs to the class and is shared across every instance
// static block runs once when the class is initialized
// singleton restricts construction so one shared instance is exposed

public class O01_StaticAndSingleton {
    public static void main(String[] args) {
        System.out.println("Objects created: " + Employee.getCount());	// Objects created: 0
        Employee first = new Employee("Sruthi");
        Employee second = new Employee("Mora");
        System.out.println(first.name() + ", " + second.name());	// Sruthi, Mora
        System.out.println("Objects created: " + Employee.getCount());	// Objects created: 2
        System.out.println(AppConfig.getInstance() == AppConfig.getInstance());	// true
    }
}

record Employee(String name) {
    private static int count;

    // a static block runs once when the class is initialized
    static {
        System.out.println("Employee class initialized");	// Employee class initialized
    }

    Employee {
        count++;
    }

    static int getCount() {
        return count;
    }
}

final class AppConfig {
    private AppConfig() {
    }

    // the holder idiom creates a lazy and thread-safe singleton
    private static class Holder {
        private static final AppConfig INSTANCE = new AppConfig();
    }

    static AppConfig getInstance() {
        return Holder.INSTANCE;
    }
}
