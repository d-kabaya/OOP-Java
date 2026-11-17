/**
 * Lesson 6: interfaces, polymorphism and casting.
 *
 * Compile: javac *.java
 * Run:     java InterfaceDemo
 */
public class InterfaceDemo {

    public static void main(String[] args) {
        // ---------- One DataSet for bank accounts ----------
        DataSet accounts = new DataSet();
        accounts.add(new BankAccount(100));
        accounts.add(new BankAccount(2500));
        accounts.add(new BankAccount(400));
        System.out.println("Average balance: " + accounts.getAverage());
        System.out.println("Largest: " + accounts.getMaximum());

        // ---------- ...and the SAME DataSet class for coins ----------
        DataSet coins = new DataSet();
        coins.add(new Coin("10 cent", 0.10));
        coins.add(new Coin("2 euro", 2.00));
        coins.add(new Coin("50 cent", 0.50));
        System.out.println("Average coin value: " + coins.getAverage());
        System.out.println("Most valuable: " + coins.getMaximum());

        // ---------- Polymorphism: same call, different code runs ----------
        Shape[] shapes = { new Square(3), new Circle(2), new Square(4) };
        for (Shape s : shapes) {
            s.printArea();                       // default method, uses each shape's own getArea
        }

        // ---------- Static interface method ----------
        Shape largest = Shape.getLargest(Shape.getLargest(shapes[0], shapes[1]), shapes[2]);
        System.out.println("Largest shape's area: " + largest.getArea());

        // ---------- Casting back, safely, with instanceof ----------
        Measurable m = accounts.getMaximum();
        // m.getBalance();                       // would not compile: Measurable has no getBalance
        if (m instanceof BankAccount) {
            BankAccount b = (BankAccount) m;
            System.out.println("Cast back: balance is " + b.getBalance());
        }

        for (Shape s : shapes) {
            if (s instanceof Circle) {
                Circle c = (Circle) s;
                c.grow(1);                       // a Circle-only method
                System.out.println("After grow, circle area is " + c.getArea());
            }
        }

        // ---------- A wrong cast crashes ----------
        try {
            Measurable coin = new Coin("1 euro", 1.0);
            BankAccount wrong = (BankAccount) coin;
        } catch (ClassCastException e) {
            System.out.println("Casting a Coin to BankAccount threw ClassCastException");
        }
    }
}
