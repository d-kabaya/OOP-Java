/**
 * Lesson 7: inheritance in action.
 *
 * Compile: javac *.java
 * Run:     java InheritanceDemo
 */
public class InheritanceDemo {

    // Overloading: same name, different parameters. Java picks one by the argument's type.
    public static void describe(int n) {
        System.out.println("describe(int): " + n);
    }

    public static void describe(String s) {
        System.out.println("describe(String): " + s);
    }

    public static void describe(BankAccount b) {
        System.out.println("describe(BankAccount): " + b);
    }

    public static void main(String[] args) {
        // ---------- Inherited and new methods ----------
        SavingsAccount savings = new SavingsAccount(1001, "Aoife");
        savings.deposit(500);            // inherited from BankAccount
        savings.addInterest();           // SavingsAccount's own method
        System.out.println(savings);     // overridden toString

        // ---------- Overriding: the fee is charged automatically ----------
        CheckingAccount checking = new CheckingAccount(1002, "Sean", 100);
        checking.withdraw(20);           // 20 + 1 fee
        checking.deposit(50);            // +50 - 1 fee
        System.out.println(checking);

        // ---------- Polymorphism: variable type vs real class ----------
        BankAccount b = checking;        // a CheckingAccount IS a BankAccount
        b.withdraw(10);                  // CheckingAccount's withdraw runs: 10 + 1 fee
        System.out.println("After b.withdraw(10): " + b.getBalance());
        // b.deductFee();                // would not compile: BankAccount has no deductFee

        // ---------- instanceof ----------
        Object o = checking;
        System.out.println("instanceof CheckingAccount: " + (o instanceof CheckingAccount));
        System.out.println("instanceof BankAccount:     " + (o instanceof BankAccount));
        System.out.println("instanceof Object:          " + (o instanceof Object));
        System.out.println("instanceof SavingsAccount:  " + (o instanceof SavingsAccount));

        // ---------- equals vs == ----------
        BankAccount a1 = new BankAccount(2001, "Mark", 0);
        BankAccount a2 = new BankAccount(2001, "Mark", 0);
        System.out.println("a1 == a2:      " + (a1 == a2));        // different objects
        System.out.println("a1.equals(a2): " + a1.equals(a2));     // same account number

        // ---------- Abstract class: an array of the abstract type ----------
        Drawing[] scene = { new Building(70, 50, 55, 350), new Tree(10, 0, 30) };
        for (Drawing d : scene) {
            d.move(5, 5);                 // shared method from Drawing
            System.out.println(d.draw()); // each subclass's own draw
        }
        // new Drawing(1, 2);             // would not compile: Drawing is abstract

        // ---------- Overloading ----------
        describe(42);
        describe("hello");
        describe(savings);
    }
}
