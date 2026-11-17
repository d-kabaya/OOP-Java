import java.util.ArrayList;

/**
 * Lesson 5: encapsulation and static, in action.
 *
 * Compile: javac *.java
 * Run:     java EncapsulationDemo
 */
public class EncapsulationDemo {

    public static void addTen(int n) {
        n = n + 10;                       // changes only the method's own copy
    }

    public static void addItem(ArrayList<Integer> list) {
        list.add(10);                     // changes the caller's list
    }

    public static void main(String[] args) {
        // ---------- Passing primitives vs objects ----------
        int x = 5;
        ArrayList<Integer> nums = new ArrayList<Integer>();
        addTen(x);
        addItem(nums);
        System.out.println("After the calls: x = " + x + ", nums = " + nums);

        // ---------- A well-encapsulated class ----------
        CashRegister till = new CashRegister(50);
        till.add(20.5);
        till.add(4.5);
        till.remove(25);
        System.out.println("Money: " + till.getMoney() + ", sales: " + till.getSales());

        // Changing the returned copy does NOT change the till
        ArrayList<String> history = till.getTransactions();
        history.clear();
        System.out.println("Transactions still in the till: " + till.getTransactions().size());

        // Validation in action
        try {
            till.add(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        // ---------- Static variable shared by all objects ----------
        new CashRegister(0);
        new CashRegister(100);
        System.out.println("Registers created: " + CashRegister.getRegistersCreated());
        System.out.println("Constant via class name: " + CashRegister.MAX_SINGLE_AMOUNT);

        // ---------- Static methods: no object needed ----------
        System.out.println("Rolled 2D6: " + Dice.roll2D6());

        // ---------- Singleton: both variables get the SAME object ----------
        Logger first = Logger.getLogger();
        Logger second = Logger.getLogger();
        first.log("Hello");
        second.log("World");
        System.out.println("Same Logger object? " + (first == second));
    }
}
