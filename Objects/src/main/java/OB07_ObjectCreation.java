import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

// new and reflective constructors initialize class instances while factories may return new or existing objects
// ordinary Serializable deserialization restores state without running that serializable class's constructor

public class OB07_ObjectCreation {
    public static void main(String[] args) throws ReflectiveOperationException, IOException {
        // constructor reflection invokes the selected constructor just like ordinary creation
        Entry direct = new Entry("Java");
        Entry reflected = Entry.class.getDeclaredConstructor(String.class).newInstance("SQL");
        System.out.println(direct.name);	// Java
        System.out.println(reflected.name);	// SQL
        System.out.println(Entry.constructions);	// 2

        // a factory need not allocate a distinct instance on every call
        System.out.println(Entry.defaultEntry() == Entry.defaultEntry());	// true

        // in-memory trusted serialization restores fields but not transient or static instance state
        direct.session = "temporary";
        byte[] bytes;
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream();
             ObjectOutputStream output = new ObjectOutputStream(buffer)) {
            output.writeObject(direct);
            output.flush();
            bytes = buffer.toByteArray();
        }
        try (ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            Entry restored = (Entry) input.readObject();
            System.out.println(restored == direct);	// false
            System.out.println(restored.name);	// Java
            System.out.println(restored.session);	// null
            System.out.println(Entry.constructions);	// 3
        }
    }

    static final class Entry implements Serializable {
        private static final long serialVersionUID = 1L;
        static int constructions;
        final String name;
        transient String session;
        Entry(String name) {
            this.name = name;
            constructions++;
        }
        static Entry defaultEntry() { return DefaultHolder.INSTANCE; }
        private static final class DefaultHolder {
            static final Entry INSTANCE = new Entry("default");
        }
    }

    // clone is another allocation mechanism demonstrated in OB03_CloningAndCopies
    // normal Serializable deserialization calls the first non-serializable superclass's accessible no-arg constructor
    // record deserialization uses the canonical constructor and Externalizable has different construction rules
    // never deserialize untrusted bytes without a carefully designed security boundary and input filters
    // use getDeclaredConstructor().newInstance() rather than deprecated Class.newInstance()
    // reflective constructor failures are wrapped in InvocationTargetException and access checks still apply
}
