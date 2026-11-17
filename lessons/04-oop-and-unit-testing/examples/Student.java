import java.util.ArrayList;

/**
 * Lesson 4: a student who takes several modules.
 * Student does not store marks itself: it asks each Module for its mark.
 */
public class Student {

    private String name;
    private int id;
    private ArrayList<Module> modules;

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
        this.modules = new ArrayList<Module>();
    }

    public void addModule(Module m) {
        modules.add(m);
    }

    public int getNumModules() {
        return modules.size();
    }

    // The average mark, or 0 if the student has no modules
    public double getAverage() {
        if (modules.size() == 0) {
            return 0;
        }
        int total = 0;
        for (Module m : modules) {
            total += m.getMark();
        }
        return (double) total / modules.size();   // the cast avoids integer division
    }

    public String toString() {
        return name + " (" + id + "), average " + getAverage();
    }
}
