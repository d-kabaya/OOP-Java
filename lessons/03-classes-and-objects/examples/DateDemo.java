/**
 * Lesson 3: creating and using Date objects.
 *
 * Compile: javac Date.java DateDemo.java
 * Run:     java DateDemo
 */
public class DateDemo {

    public static void main(String[] args) {
        // ---------- Creating objects ----------
        Date birthday = new Date(3, 5, 2006);
        Date newYear = new Date(2027);              // uses the second constructor
        System.out.println("birthday: " + birthday);
        System.out.println("newYear:  " + newYear);

        // ---------- Calling a method changes only that object ----------
        birthday.incrementYear();
        System.out.println("after birthday.incrementYear(): birthday = " + birthday + ", newYear = " + newYear);

        // ---------- Comparing two objects with a method ----------
        Date a = new Date(25, 12, 2026);
        Date b = new Date(25, 12, 2026);
        System.out.println("a.sameDate(b): " + a.sameDate(b));   // true: same contents
        System.out.println("a == b:        " + (a == b));        // false: two different objects

        // ---------- Copying a primitive copies the VALUE ----------
        int x = 120;
        int y = x;
        y++;
        System.out.println("primitives: x = " + x + ", y = " + y);

        // ---------- Copying an object variable copies the REFERENCE ----------
        Date t = new Date(1, 1, 2000);
        Date copyOfReference = t;              // both point to the SAME object
        copyOfReference.incrementYear();
        System.out.println("references: t = " + t + " (changed through the other variable)");
        System.out.println("t == copyOfReference: " + (t == copyOfReference));

        // ---------- A real copy needs a new object ----------
        Date realCopy = new Date(1, 1, 2001);
        realCopy.incrementYear();
        System.out.println("real copy:  t = " + t + ", realCopy = " + realCopy);

        // ---------- null ----------
        Date nothing = null;
        try {
            nothing.incrementYear();
        } catch (NullPointerException e) {
            System.out.println("Calling a method on null threw a NullPointerException");
        }
    }
}
