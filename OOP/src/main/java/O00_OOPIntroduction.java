// object-oriented programming models programs as objects containing state and behavior
// encapsulation controls access to state, abstraction exposes essentials, inheritance reuses behavior and polymorphism supports many forms
// association connects independent objects, aggregation models weak ownership and composition models strong ownership

public class O00_OOPIntroduction {
    public static void main(String[] args) {
        Account account = new Account("Sruthi", 100);
        account.deposit(50);
        System.out.println(account.owner());	// Sruthi
        System.out.println(account.balance());	// 150
        // encapsulation protects the invariant rather than merely providing getters and setters
        try {
            account.deposit(-10);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());	// amount must be positive
        }
        System.out.println(account.balance());	// 150
    }
}

class Account {
    private final String owner;
    private int balance;

    Account(String owner, int balance) {
        if (owner == null || owner.isBlank()) throw new IllegalArgumentException("owner required");
        if (balance < 0) throw new IllegalArgumentException("balance cannot be negative");
        this.owner = owner;
        this.balance = balance;
    }

    void deposit(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
        balance = Math.addExact(balance, amount);
    }

    String owner() {
        return owner;
    }

    int balance() {
        return balance;
    }
}
