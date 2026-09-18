// object-oriented programming models programs as objects containing state and behavior
// encapsulation controls access to state, abstraction exposes essentials, inheritance reuses behavior and polymorphism supports many forms
// association connects independent objects, aggregation models weak ownership and composition models strong ownership

public class O00_OOPIntroduction {
    public static void main(String[] args) {
        Account account = new Account("Sruthi", 100);
        account.deposit(50);
        System.out.println(account.owner());	// Sruthi
        System.out.println(account.balance());	// 150
    }
}

class Account {
    private final String owner;
    private int balance;

    Account(String owner, int balance) {
        this.owner = owner;
        this.balance = balance;
    }

    void deposit(int amount) {
        if (amount > 0) balance += amount;
    }

    String owner() {
        return owner;
    }

    int balance() {
        return balance;
    }
}
