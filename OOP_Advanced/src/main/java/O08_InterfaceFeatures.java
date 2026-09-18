import java.io.Serializable;
import java.rmi.Remote;

// interface fields are public static final and abstract methods are public by default
// Java 8 added default and static methods while Java 9 added private interface methods
// marker interface declares no methods and communicates a capability such as Serializable

public class O08_InterfaceFeatures {
    public static void main(String[] args) {
        Processor processor = value -> System.out.println(value);	// supplied value
        processor.process("Java");
        processor.print("default");
        Processor.about();
        System.out.println(Processor.VERSION);	// 1
        System.out.println(new SerializableValue() instanceof Serializable);	// true
    }
}

@FunctionalInterface
interface Processor extends ParentProcessor {
    int VERSION = 1;

    void process(String value);

    default void print(String value) {
        log(value);
    }

    static void about() {
        System.out.println("Processor");	// Processor
    }

    private void log(String value) {
        System.out.println(value);	// supplied value
    }

    interface Nested {
        void run();
    }
}

interface ParentProcessor {
}

interface RemoteService extends Remote {
}

class SerializableValue implements Serializable {
}

// interface static methods belong to the interface and are not inherited by implementing classes
// an interface extends interfaces while a class implements interfaces and can implement several of them
// a top-level interface is public or package-private and a nested interface is implicitly static
// marker interfaces such as Serializable, Cloneable and Remote communicate capabilities without declaring methods
