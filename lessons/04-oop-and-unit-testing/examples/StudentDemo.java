/**
 * Lesson 4: objects working together, and looping over an enum.
 *
 * Compile: javac Size.java Module.java Student.java StudentDemo.java
 * Run:     java StudentDemo
 */
public class StudentDemo {

    public static void main(String[] args) {
        // ---------- Enums ----------
        Size mine = Size.MEDIUM;
        System.out.println("My size: " + mine + " (" + mine.getAbbreviation() + ")");

        System.out.print("All sizes: ");
        for (Size s : Size.values()) {
            System.out.print(s.getAbbreviation() + " ");
        }
        System.out.println();

        switch (mine) {
            case SMALL:
            case EXTRA_SMALL:
                System.out.println("A small size");
                break;
            default:
                System.out.println("Not a small size");
        }

        // ---------- Objects working together ----------
        Student s = new Student("Simon", 1234556);
        s.addModule(new Module("Java", 60));
        s.addModule(new Module("Python", 45));
        System.out.println(s);

        Student newStudent = new Student("Aoife", 1234557);
        System.out.println(newStudent + " (no modules yet)");

        // ---------- Validation in the Module constructor ----------
        try {
            new Module("Maths", 101);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}
