// inheritance models an is-a relationship by extending one class
// Java supports single, multilevel and hierarchical class inheritance but uses interfaces for multiple type inheritance
// overriding selects an instance method from the runtime object type
// field access and static method hiding use the compile-time reference type

public class O04_InheritanceAndPolymorphism {
    public static void main(String[] args) {
        Payment payment = new CardPayment();
        payment.pay(500);
        System.out.println(payment.label);	// Payment
        Payment.printType();
        CardPayment.printType();
        System.out.println(new CreditCardPayment() instanceof Payment);	// true
        System.out.println(new CashPayment() instanceof Payment);	// true
        System.out.println(new MultiChannelPayment() instanceof AuditedPayment);	// true
        // upcasting is implicit while a checked downcast exposes subtype-specific behavior
        if (payment instanceof CardPayment card) {
            System.out.println(card.label);	// Card payment
        }
        try {
            CardPayment invalid = (CardPayment) new Payment();
        } catch (ClassCastException exception) {
            System.out.println("invalid downcast");	// invalid downcast
        }
        System.out.println(null instanceof Payment);	// false
    }
}

class Payment {
    String label = "Payment";

    void pay(int amount) {
        System.out.println("Paid " + amount);	// printed for a Payment object
    }

    static void printType() {
        System.out.println("Generic payment");	// Generic payment
    }
}

class CardPayment extends Payment {
    String label = "Card payment";

    // overridden instance methods are selected using the runtime object type
    @Override
    void pay(int amount) {
        System.out.println("Card paid " + amount);	// Card paid 500
    }

    // static methods are hidden and selected using the compile-time reference type
    static void printType() {
        System.out.println("Card payment");	// Card payment
    }
}

// multilevel inheritance continues an inheritance chain
class CreditCardPayment extends CardPayment {
}

// hierarchical inheritance gives the same parent multiple direct children
class CashPayment extends Payment {
}

interface AuditedPayment {
}

interface RefundablePayment {
}

// multiple type inheritance is achieved by implementing multiple interfaces
class MultiChannelPayment extends Payment implements AuditedPayment, RefundablePayment {
}
