import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// behavior checks verify contracts such as equality, invariants, substitutability and defensive copying
// explicit AssertionError checks run even when the JVM assertion flag is disabled

public class O18_OOPChecks {
    public static void main(String[] args) {
        Account account = new Account("Sruthi", 100);
        try {
            account.deposit(-1);
            throw new AssertionError("invalid deposit accepted");
        } catch (IllegalArgumentException expected) {
            check(account.balance() == 100, "balance changed after rejection");
        }

        User first = new User(1, "Sruthi");
        User second = new User(1, "Sruthi");
        check(first.equals(first), "reflexivity");
        check(first.equals(second) && second.equals(first), "symmetry");
        check(!first.equals(null), "null equality");
        check(first.hashCode() == second.hashCode(), "hash contract");
        check(new HashSet<>(List.of(first, second)).size() == 1, "duplicate logical key");

        List<String> source = new ArrayList<>(List.of("Java"));
        Profile profile = new Profile("Sruthi", source);
        source.add("SQL");
        check(profile.skills().equals(List.of("Java")), "defensive copy");

        O12_OverloadingAndOverriding.Base base = new O12_OverloadingAndOverriding.Child();
        check(base.copy() instanceof O12_OverloadingAndOverriding.Child, "covariant dispatch");
        check(new Combined().label().equals("left+right"), "default conflict resolution");
        check(O15_RecordsAndSealedTypes.area(new O15_RecordsAndSealedTypes.Rectangle(2, 4)) == 8, "sealed dispatch");
        System.out.println("OOP checks passed");	// OOP checks passed
    }

    static void check(boolean condition, String name) {
        if (!condition) throw new AssertionError(name);
    }
}
