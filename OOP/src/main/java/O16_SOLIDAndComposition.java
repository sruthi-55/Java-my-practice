// SOLID groups five design principles that help objects change without breaking their collaborators
// high cohesion keeps one class focused while low coupling reduces dependency on implementation details

public class O16_SOLIDAndComposition {
    public static void main(String[] args) {
        // constructor injection supplies an abstraction and permits substituting implementations
        Checkout standard = new Checkout(new StandardPrice(), receipt -> "saved " + receipt);
        Checkout discount = new Checkout(new DiscountPrice(), receipt -> "saved " + receipt);
        System.out.println(standard.buy(100));	// saved 100
        System.out.println(discount.buy(100));	// saved 90

        // segregated capabilities avoid forcing a read-only object to implement unsupported writes
        Reader reader = () -> "Java";
        System.out.println(reader.read());	// Java
    }

    // single responsibility separates price calculation, persistence and orchestration
    interface PricePolicy { int price(int amount); }
    interface ReceiptStore { String save(int amount); }
    interface Reader { String read(); }
    interface Writer { void write(String value); }

    static class StandardPrice implements PricePolicy {
        @Override
        public int price(int amount) { return amount; }
    }

    // open/closed adds a policy without editing Checkout and substitution preserves the nonnegative-price contract
    static class DiscountPrice implements PricePolicy {
        @Override
        public int price(int amount) { return amount - amount / 10; }
    }

    static class Checkout {
        private final PricePolicy policy;
        private final ReceiptStore store;

        Checkout(PricePolicy policy, ReceiptStore store) {
            this.policy = java.util.Objects.requireNonNull(policy);
            this.store = java.util.Objects.requireNonNull(store);
        }

        String buy(int amount) {
            if (amount < 0) throw new IllegalArgumentException("amount cannot be negative");
            return store.save(policy.price(amount));
        }
    }

    // Liskov substitution requires preserving behavioral promises rather than merely sharing signatures
    // interface segregation keeps client contracts small and dependency inversion depends on abstractions
    // composition delegates behavior to collaborators instead of inheriting implementation solely for reuse
}
