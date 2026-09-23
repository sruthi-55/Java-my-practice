// Java always passes a copy of the argument value including a copied object reference
// mutation through the copied reference is visible while parameter reassignment is not

public class O07_PassByValue {
    public static void main(String[] args) {
        MutableName name = new MutableName("Sruthi");
        mutate(name);
        System.out.println(name.value);	// Mora
        reassign(name);
        System.out.println(name.value);	// Mora
    }

    static void mutate(MutableName name) {
        name.value = "Mora";
    }

    static void reassign(MutableName name) {
        name = new MutableName("Java");
    }
}

class MutableName {
    String value;

    MutableName(String value) {
        this.value = value;
    }
}
