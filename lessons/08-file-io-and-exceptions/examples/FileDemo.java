import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Lesson 8: reading and writing text files.
 *
 * Run from inside the examples folder, so grades.csv is found:
 *   javac *.java
 *   java FileDemo
 */
public class FileDemo {

    // Reads students from a CSV file, skipping (and reporting) invalid lines.
    // "throws IOException": if the file can't be opened, the CALLER decides what to do (throw early, catch late).
    public static ArrayList<Student> read(File file) throws IOException {
        ArrayList<Student> list = new ArrayList<Student>();
        BufferedReader in = new BufferedReader(new FileReader(file));
        try {
            String line = in.readLine();            // skip the header line
            while ((line = in.readLine()) != null) {
                try {                               // try/catch INSIDE the loop: one bad line doesn't stop the rest
                    String[] v = line.split(",");
                    if (v.length != 2) {
                        throw new IllegalArgumentException("Expected 2 values but found " + v.length);
                    }
                    list.add(new Student(v[0].trim(), Integer.parseInt(v[1].trim())));
                } catch (IllegalArgumentException e) {   // NumberFormatException is a subclass, so it's caught too
                    System.out.println("  Skipping \"" + line + "\": " + e.getMessage());
                }
            }
        } finally {
            in.close();                             // always runs, even if something above throws
        }
        return list;
    }

    // Writes the students who passed to a new file
    public static void writePassed(ArrayList<Student> students, File file) throws IOException {
        PrintWriter out = new PrintWriter(new FileOutputStream(file));
        out.println("name,mark");
        for (Student s : students) {
            if (s.hasPassed()) {
                out.println(s.toCsv());
            }
        }
        out.close();                                // without this, the file may be empty!
    }

    public static void main(String[] args) {
        File input = new File("grades.csv");
        System.out.println("Looking for: " + input.getAbsolutePath());
        System.out.println("Exists? " + input.exists());

        // ---------- Read with BufferedReader, skipping bad lines ----------
        try {
            System.out.println("Reading grades.csv:");
            ArrayList<Student> students = read(input);
            System.out.println("Valid students: " + students);

            // ---------- Write a new file ----------
            File output = new File("passed.csv");
            writePassed(students, output);
            System.out.println("Wrote " + output.getName());

            // ---------- Read it back with Scanner ----------
            Scanner sc = new Scanner(output);
            while (sc.hasNextLine()) {
                System.out.println("  passed.csv: " + sc.nextLine());
            }
            sc.close();
            output.delete();                        // tidy up
        } catch (IOException e) {
            System.out.println("Could not read or write a file: " + e.getMessage());
        }

        // ---------- A file that doesn't exist ----------
        try {
            read(new File("doesNotExist.csv"));
        } catch (FileNotFoundException e) {         // more specific first
            System.out.println("Not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Some other IO problem: " + e.getMessage());
        }
    }
}
